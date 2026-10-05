package com.example.ui.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.sin

class AmbientSoundManager(private val context: Context) {

    private var audioTrack: AudioTrack? = null
    private var soundJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun startSound(mode: String) {
        stopSound()
        if (mode == "None") return

        soundJob = scope.launch {
            val sampleRate = 22050
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(sampleRate / 2)

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize * 2)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack = track
            track.play()

            val random = Random()
            val buffer = ShortArray(bufferSize)
            var lastSample = 0.0
            var phase = 0.0

            try {
                while (isActive) {
                    when (mode) {
                        "Rain" -> {
                            // Pink noise / rain simulation with low-pass filtering
                            for (i in buffer.indices) {
                                val white = (random.nextDouble() * 2.0 - 1.0)
                                lastSample = (lastSample * 0.95) + (white * 0.05)
                                val amplitude = (lastSample * 6000.0).toInt().coerceIn(-32767, 32767)
                                buffer[i] = amplitude.toShort()
                            }
                        }
                        "Campfire" -> {
                            // Gentle warm rumble + occasional crackle pops
                            for (i in buffer.indices) {
                                val base = (random.nextDouble() * 2.0 - 1.0) * 1200.0
                                val pop = if (random.nextInt(3500) == 0) {
                                    (random.nextDouble() * 18000.0) - 9000.0
                                } else 0.0
                                lastSample = (lastSample * 0.92) + (base * 0.08)
                                val combined = (lastSample + pop).toInt().coerceIn(-32767, 32767)
                                buffer[i] = combined.toShort()
                            }
                        }
                        "Lofi" -> {
                            // Soft soothing pentatonic tones on an ambient pad
                            val pentatonicFrequencies = doubleArrayOf(261.63, 293.66, 329.63, 392.00, 440.00, 523.25)
                            val targetFreq = pentatonicFrequencies[random.nextInt(pentatonicFrequencies.size)]
                            val step = 2.0 * Math.PI * targetFreq / sampleRate

                            for (i in buffer.indices) {
                                phase += step
                                if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI
                                val tone = sin(phase) * 3500.0
                                val gentleHiss = (random.nextDouble() * 2.0 - 1.0) * 300.0
                                buffer[i] = (tone + gentleHiss).toInt().coerceIn(-32767, 32767).toShort()
                            }
                            delay(150)
                        }
                        else -> {
                            buffer.fill(0)
                        }
                    }
                    track.write(buffer, 0, buffer.size)
                }
            } catch (_: Exception) {
            } finally {
                try {
                    track.stop()
                    track.release()
                } catch (_: Exception) {}
            }
        }
    }

    fun stopSound() {
        soundJob?.cancel()
        soundJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
    }

    fun triggerHeartbeatHaptic() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Lub-dub heartbeat pattern: 0ms delay, 120ms rumble, 100ms pause, 180ms rumble
                    val timings = longArrayOf(0, 110, 90, 160)
                    val amplitudes = intArrayOf(0, 180, 0, 240)
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(longArrayOf(0, 110, 90, 160), -1)
                }
            }
        } catch (_: Exception) {}
    }
}
