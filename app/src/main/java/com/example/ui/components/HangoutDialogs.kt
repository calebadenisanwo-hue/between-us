package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LavenderMist
import com.example.ui.theme.RoseCoral
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SoftRose
import com.example.ui.theme.WarmTerracotta

@Composable
fun UpdateStatusDialog(
    partnerName: String,
    currentMood: String,
    currentActivity: String,
    onDismiss: () -> Unit,
    onSave: (mood: String, activity: String) -> Unit
) {
    var mood by remember { mutableStateOf(currentMood) }
    var activity by remember { mutableStateOf(currentActivity) }

    val quickMoods = listOf(
        "Missing you 💭", "Cozy & smiling ☕", "Loving you so much ❤️",
        "Craving boba 🧋", "Sleepy 😴", "Hyped & happy ✨", "Need a Kola hug 🫂", "Listening to us 🎵"
    )

    val quickActivities = listOf(
        "Listening to our playlist 🎵", "Wrapped in a blanket 🛌", "Studying/Working 💻",
        "Making tea 🫖", "Stargazing ✨", "Thinking of our next trip ✈️", "Getting ready for bed 🌙"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Update $partnerName's Status",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Current Mood / Vibe", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = mood,
                    onValueChange = { mood = it },
                    modifier = Modifier.fillMaxWidth().testTag("status_mood_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(quickMoods) { item ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { mood = item }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = item, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "What are you up to right now?", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = activity,
                    onValueChange = { activity = it },
                    modifier = Modifier.fillMaxWidth().testTag("status_activity_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(quickActivities) { act ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { activity = act }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = act, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(mood, activity) },
                colors = ButtonDefaults.buttonColors(containerColor = RoseCoral),
                modifier = Modifier.testTag("save_status_button")
            ) {
                Text("Save Status", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun HeartbeatPokeDialog(
    sender: String,
    recipient: String,
    onDismiss: () -> Unit,
    onSendPoke: (message: String) -> Unit
) {
    var customMessage by remember {
        mutableStateOf("$sender sent lingering heartbeats and a gentle hug! 🫂✨")
    }

    val quickPokes = listOf(
        "$sender is thinking of you right this second 💭❤️",
        "Gentle butterfly kisses on your cheek 🦋",
        "Pinching your cheek across the ocean! 🤏🥰",
        "A warm squeeze until you can't breathe! 🫂",
        "Just a reminder: I love you more than yesterday 💖"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = RoseCoral,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send a Poke to $recipient", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Sending a poke will trigger a tactile heartbeat pulse on both of your screens!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = customMessage,
                    onValueChange = { customMessage = it },
                    label = { Text("Note with your poke") },
                    modifier = Modifier.fillMaxWidth().testTag("poke_message_input"),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Quick templates:", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    quickPokes.forEach { poke ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { customMessage = poke }
                                .padding(8.dp)
                        ) {
                            Text(text = poke, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSendPoke(customMessage) },
                colors = ButtonDefaults.buttonColors(containerColor = RoseCoral),
                modifier = Modifier.testTag("send_poke_confirm_button")
            ) {
                Text("Send Heartbeat 💓", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AmbientSoundPickerSheet(
    currentSound: String,
    onDismiss: () -> Unit,
    onSelectSound: (String) -> Unit
) {
    val soundOptions = listOf(
        Triple("Rain", "Gentle Rain on Window", "🌧️ Soothing soft rain outside your shared room"),
        Triple("Campfire", "Cozy Fireplace Crackle", "🔥 Warm embers and gentle hearth glow"),
        Triple("Lofi", "Starlight Lofi Chimes", "✨ Peaceful chime chords under the stars"),
        Triple("None", "Silence / Mute", "🔇 Pure quiet moment")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = RoseCoral)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Room Soundscape", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Play gentle procedural sounds to feel like you're sitting in the same room together. 100% offline & battery friendly.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                soundOptions.forEach { (mode, name, desc) ->
                    val isSelected = currentSound == mode
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSound(mode) }
                            .testTag("sound_option_$mode"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) RoseCoral.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = name,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) RoseCoral else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (isSelected) {
                                Text(text = "Playing 🎵", color = RoseCoral, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        }
    )
}
