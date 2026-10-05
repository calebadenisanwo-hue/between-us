package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.HangoutState
import com.example.ui.components.AmbientSoundPickerSheet
import com.example.ui.components.HeartbeatPokeDialog
import com.example.ui.components.UpdateStatusDialog
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LavenderMist
import com.example.ui.theme.RoseCoral
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SoftRose
import com.example.ui.theme.WarmTerracotta
import java.util.concurrent.TimeUnit

@Composable
fun HomeScreen(
    hangoutState: HangoutState,
    currentProfile: String,
    onWaterPlant: () -> Unit,
    onSunPlant: () -> Unit,
    onUpdateMood: (mood: String, activity: String) -> Unit,
    onSendPoke: (message: String) -> Unit,
    onToggleSound: (String) -> Unit,
    onNavigateToArcade: () -> Unit,
    onNavigateToDeepTalks: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showStatusDialog by remember { mutableStateOf(false) }
    var showPokeDialog by remember { mutableStateOf(false) }
    var showSoundDialog by remember { mutableStateOf(false) }

    // Heart pulsation effect
    val heartPulsing = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            heartPulsing.animateTo(1.15f, tween(600, easing = FastOutSlowInEasing))
            heartPulsing.animateTo(1.0f, tween(600, easing = FastOutSlowInEasing))
            heartPulsing.animateTo(1.08f, tween(400, easing = FastOutSlowInEasing))
            heartPulsing.animateTo(1.0f, tween(800, easing = FastOutSlowInEasing))
        }
    }

    // Calculate days until reunion
    val currentTime = System.currentTimeMillis()
    val diffMillis = (hangoutState.nextVisitEpochMillis - currentTime).coerceAtLeast(0)
    val daysUntil = TimeUnit.MILLISECONDS.toDays(diffMillis)
    val hoursUntil = TimeUnit.MILLISECONDS.toHours(diffMillis) % 24

    // Anniversary days
    val togetherMillis = (currentTime - hangoutState.anniversaryEpochMillis).coerceAtLeast(0)
    val daysTogether = TimeUnit.MILLISECONDS.toDays(togetherMillis)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Hero Hangout Illustration with Cozy Living Room Art
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hangout_room),
                        contentDescription = "Our Cozy Living Room",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xCC130F21))
                                )
                            )
                    )
                    // Text caption
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SageGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Kola & Joy's Sanctuary",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            text = "Distance means so little when someone means so much.",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }

                    // Ambiance sound indicator chip
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x99000000))
                            .clickable { showSoundDialog = true }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = if (hangoutState.ambientSound != "None") RoseCoral else Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (hangoutState.ambientSound != "None") hangoutState.ambientSound else "Sound: Off",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Couple Status & Location Presence
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Kola Card
                PartnerPresenceCard(
                    modifier = Modifier.weight(1f),
                    name = "Kola 💙",
                    location = hangoutState.kolaLocation,
                    mood = hangoutState.kolaMood,
                    activity = hangoutState.kolaActivity,
                    isCurrent = currentProfile == "Kola",
                    badgeColor = Color(0xFF5C9DFF),
                    onEdit = {
                        if (currentProfile == "Kola") showStatusDialog = true
                    }
                )

                // Joy Card
                PartnerPresenceCard(
                    modifier = Modifier.weight(1f),
                    name = "Joy 🌸",
                    location = hangoutState.joyLocation,
                    mood = hangoutState.joyMood,
                    activity = hangoutState.joyActivity,
                    isCurrent = currentProfile == "Joy",
                    badgeColor = SoftRose,
                    onEdit = {
                        if (currentProfile == "Joy") showStatusDialog = true
                    }
                )
            }
        }

        // Heartbeat & Poke Interactive Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Heartbeat & Poke",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${hangoutState.pokeCount} touches sent across the distance",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Heartbeat trigger button
                        Box(
                            modifier = Modifier
                                .scale(heartPulsing.value)
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(RoseCoral, Color(0xFFC73659))
                                    )
                                )
                                .clickable { showPokeDialog = true }
                                .testTag("main_heartbeat_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Send Heartbeat",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // Last poke reminder message
                    if (!hangoutState.lastPokedMessage.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "💌", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "${hangoutState.lastPokedBy ?: "Partner"} says:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RoseCoral,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = hangoutState.lastPokedMessage,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Reunion Countdown & Anniversary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlightTakeoff,
                                contentDescription = null,
                                tint = WarmTerracotta,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = hangoutState.nextVisitLabel,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Text(
                            text = "$daysTogether days together ❤️",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Countdown blocks
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CountdownUnit(number = "$daysUntil", label = "DAYS")
                        Text(
                            text = ":",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        CountdownUnit(number = "$hoursUntil", label = "HOURS")
                        Text(
                            text = ":",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        CountdownUnit(number = "Soon", label = "NEXT HUG")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "London 🇬🇧 ✈️ Lagos 🇳🇬 (~5,000 km)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Shared Love Plant Nurture Card
        item {
            val stageName = when (hangoutState.plantStage) {
                1 -> "Tiny Sprout of Love 🌱"
                2 -> "Growing Sweetheart Bonsai 🪴"
                3 -> "Blooming Starlight Jasmine 🌸"
                else -> "Evergreen Love Tree 🌳✨"
            }
            val plantEmoji = when (hangoutState.plantStage) {
                1 -> "🌱"
                2 -> "🪴"
                3 -> "🌸"
                else -> "🌳"
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = plantEmoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Our Shared Plant",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = stageName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SageGreen,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        // Growth points badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SageGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Lvl ${hangoutState.plantStage} (${hangoutState.plantLoveLevel} XP)",
                                color = SageGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Gauges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Water 💧", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = "${hangoutState.plantWaterLevel}%",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { hangoutState.plantWaterLevel / 100f },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = Color(0xFF5C9DFF),
                                trackColor = MaterialTheme.colorScheme.surface
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Sunlight ☀️", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = "${hangoutState.plantSunLevel}%",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { hangoutState.plantSunLevel / 100f },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = GoldenSun,
                                trackColor = MaterialTheme.colorScheme.surface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onWaterPlant,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("water_plant_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4379C2))
                        ) {
                            Icon(Icons.Default.WaterDrop, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Water Together")
                        }

                        Button(
                            onClick = onSunPlant,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sun_plant_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldenSun)
                        ) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color(0xFF4A3400), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Give Sunlight", color = Color(0xFF4A3400))
                        }
                    }
                }
            }
        }

        // Quick Activities Launcher
        item {
            Text(
                text = "Hangout Tonight 🌙",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToArcade() }
                        .testTag("arcade_quick_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = RoseCoral.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "🎮 Play Arcade", fontWeight = FontWeight.Bold, color = RoseCoral)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Canvas doodle, TicTacToe & quizzes",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToDeepTalks() }
                        .testTag("deep_talks_quick_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmTerracotta.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "💭 Deep Talks", fontWeight = FontWeight.Bold, color = WarmTerracotta)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Couple questions deck & memories",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showStatusDialog) {
        val partnerName = currentProfile
        val currentMood = if (partnerName == "Kola") hangoutState.kolaMood else hangoutState.joyMood
        val currentAct = if (partnerName == "Kola") hangoutState.kolaActivity else hangoutState.joyActivity

        UpdateStatusDialog(
            partnerName = partnerName,
            currentMood = currentMood,
            currentActivity = currentAct,
            onDismiss = { showStatusDialog = false },
            onSave = { mood, act ->
                onUpdateMood(mood, act)
                showStatusDialog = false
            }
        )
    }

    if (showPokeDialog) {
        val sender = currentProfile
        val recipient = if (sender == "Kola") "Joy" else "Kola"

        HeartbeatPokeDialog(
            sender = sender,
            recipient = recipient,
            onDismiss = { showPokeDialog = false },
            onSendPoke = { msg ->
                onSendPoke(msg)
                showPokeDialog = false
            }
        )
    }

    if (showSoundDialog) {
        AmbientSoundPickerSheet(
            currentSound = hangoutState.ambientSound,
            onDismiss = { showSoundDialog = false },
            onSelectSound = { sound ->
                onToggleSound(sound)
                showSoundDialog = false
            }
        )
    }
}

@Composable
private fun PartnerPresenceCard(
    name: String,
    location: String,
    mood: String,
    activity: String,
    isCurrent: Boolean,
    badgeColor: Color,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) MaterialTheme.colorScheme.surfaceVariant
            else MaterialTheme.colorScheme.surface
        ),
        border = if (isCurrent) CardDefaults.outlinedCardBorder() else null
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = badgeColor
                )
                if (isCurrent) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(24.dp).testTag("edit_status_$name")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit status",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Text(
                text = location,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = mood,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = activity,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun CountdownUnit(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = RoseCoral
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
