package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.RoseCoral
import com.example.ui.theme.SoftRose
import com.example.ui.theme.WarmTerracotta

@Composable
fun TopHangoutBar(
    currentProfile: String,
    pokeCount: Int,
    ambientSound: String,
    heartbeatTrigger: Long,
    onSwitchProfile: (String) -> Unit,
    onSendPoke: () -> Unit,
    onToggleSound: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heartScale = remember { Animatable(1f) }

    LaunchedEffect(heartbeatTrigger) {
        if (heartbeatTrigger > 0) {
            heartScale.animateTo(
                targetValue = 1.35f,
                animationSpec = tween(120, easing = FastOutSlowInEasing)
            )
            heartScale.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(200, easing = FastOutSlowInEasing)
            )
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Branding & Couple Identity
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Between Us",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "✨",
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "Kola 💙 & Joy 🌸",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Actions: Soundscape + Quick Poke Heart + Perspective Switcher
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Ambient Sound Indicator / Toggle Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (ambientSound != "None") RoseCoral.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { onToggleSound() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("ambient_sound_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Ambient Sound",
                            tint = if (ambientSound != "None") RoseCoral else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (ambientSound != "None") ambientSound else "Sound",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (ambientSound != "None") RoseCoral else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Heartbeat / Poke Button with Live Counter Badge
                IconButton(
                    onClick = onSendPoke,
                    modifier = Modifier
                        .scale(heartScale.value)
                        .testTag("heartbeat_poke_button")
                ) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = RoseCoral,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = "$pokeCount",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Send Heartbeat",
                            tint = RoseCoral,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Profile Switcher Pill ("Viewing as Kola / Joy")
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = 1.dp,
                            color = if (currentProfile == "Kola") Color(0xFF5C9DFF) else SoftRose,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .background(
                            if (currentProfile == "Kola") Color(0xFF1E2D4A) else Color(0xFF4A1F2D)
                        )
                        .clickable {
                            val next = if (currentProfile == "Kola") "Joy" else "Kola"
                            onSwitchProfile(next)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("switch_profile_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (currentProfile == "Kola") "💙 Kola" else "🌸 Joy",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Switch profile",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
