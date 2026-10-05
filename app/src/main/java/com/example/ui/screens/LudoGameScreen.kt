package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber
import com.example.ui.theme.Cream
import com.example.ui.theme.CreamDeep
import com.example.ui.theme.Ink
import com.example.ui.theme.InkMuted
import com.example.ui.theme.JoyMarigold
import com.example.ui.theme.KolaTeal
import com.example.ui.theme.Plum
import com.example.ui.theme.PlumDeep

@Composable
fun LudoGameScreen(
    onBack: () -> Unit,
    partnerName: String = "Joy",
    onGameFinished: (winner: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var diceValue by remember { mutableIntStateOf(5) }
    var hasRolledThisTurn by remember { mutableStateOf(true) }
    var currentTurn by remember { mutableStateOf("Kola") }
    var lastMoveSummary by remember { mutableStateOf("$partnerName rolled 4 and moved a piece.") }
    var kolaHomeCount by remember { mutableIntStateOf(1) }
    var joyHomeCount by remember { mutableIntStateOf(2) }

    var showMenuSheet by remember { mutableStateOf(false) }
    var showReactSheet by remember { mutableStateOf(false) }

    // Pulsing amber ring for legal pieces (Screen 6)
    val ringPulse = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        ringPulse.animateTo(
            targetValue = 1.25f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlumDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // 1. Status Banner Card (Screen 6)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Cream,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Ink)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (currentTurn == "Kola") "Your turn" else "$partnerName's turn",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Ink
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Amber))
                        }

                        IconButton(onClick = { showMenuSheet = true }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.MoreHoriz, contentDescription = "Menu", tint = Ink)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (hasRolledThisTurn) "Tap a glowing piece to move $diceValue spaces" else "Roll the die to start your move",
                        fontSize = 12.sp,
                        color = InkMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Players Bar: Kola (You) vs Joy (Screen 6)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // You (Kola) Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    modifier = Modifier.weight(1f),
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(2.dp, KolaTeal, CircleShape)
                                .background(Color(0xFF7F4D2E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("K", color = Cream, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("You", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Ink)
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                for (i in 1..4) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (i <= kolaHomeCount) KolaTeal else CreamDeep)
                                    )
                                }
                            }
                            Text("Home $kolaHomeCount of 4", fontSize = 10.sp, color = InkMuted)
                        }
                    }
                }

                // Joy Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    modifier = Modifier.weight(1f),
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(2.dp, JoyMarigold, CircleShape)
                                .background(Color(0xFF98603A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("J", color = Cream, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(partnerName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Ink)
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                for (i in 1..4) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (i <= joyHomeCount) JoyMarigold else CreamDeep)
                                    )
                                }
                            }
                            Text("Home $joyHomeCount of 4", fontSize = 10.sp, color = InkMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. 15x15 Ludo Board (Screen 6)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Cream,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .testTag("ludo_game_board")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val cellW = w / 15f
                        val cellH = h / 15f

                        // Draw 4 Large Yards:
                        // Top-Left: Unused Gray
                        drawRoundRect(
                            color = Color(0xFFDED6C8),
                            topLeft = Offset(0f, 0f),
                            size = androidx.compose.ui.geometry.Size(cellW * 6, cellH * 6),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
                        )
                        // Top-Right: Joy's Marigold Yard
                        drawRoundRect(
                            color = JoyMarigold,
                            topLeft = Offset(cellW * 9, 0f),
                            size = androidx.compose.ui.geometry.Size(cellW * 6, cellH * 6),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
                        )
                        // Bottom-Left: Kola's Teal Yard
                        drawRoundRect(
                            color = KolaTeal,
                            topLeft = Offset(0f, cellH * 9),
                            size = androidx.compose.ui.geometry.Size(cellW * 6, cellH * 6),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
                        )
                        // Bottom-Right: Unused Gray
                        drawRoundRect(
                            color = Color(0xFFDED6C8),
                            topLeft = Offset(cellW * 9, cellH * 9),
                            size = androidx.compose.ui.geometry.Size(cellW * 6, cellH * 6),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
                        )

                        // Center Finish Box & Triangles
                        drawRect(
                            color = CreamDeep,
                            topLeft = Offset(cellW * 6, cellH * 6),
                            size = androidx.compose.ui.geometry.Size(cellW * 3, cellH * 3)
                        )
                        val topTri = Path().apply {
                            moveTo(cellW * 6, cellH * 6)
                            lineTo(cellW * 9, cellH * 6)
                            lineTo(cellW * 7.5f, cellH * 7.5f)
                            close()
                        }
                        drawPath(topTri, color = Color(0xFFE07A5F))
                        val leftTri = Path().apply {
                            moveTo(cellW * 6, cellH * 6)
                            lineTo(cellW * 6, cellH * 9)
                            lineTo(cellW * 7.5f, cellH * 7.5f)
                            close()
                        }
                        drawPath(leftTri, color = KolaTeal)
                        val rightTri = Path().apply {
                            moveTo(cellW * 9, cellH * 6)
                            lineTo(cellW * 9, cellH * 9)
                            lineTo(cellW * 7.5f, cellH * 7.5f)
                            close()
                        }
                        drawPath(rightTri, color = JoyMarigold)
                        val botTri = Path().apply {
                            moveTo(cellW * 6, cellH * 9)
                            lineTo(cellW * 9, cellH * 9)
                            lineTo(cellW * 7.5f, cellH * 7.5f)
                            close()
                        }
                        drawPath(botTri, color = Color(0xFF3D5A80))

                        // Draw Grid lines for middle tracks
                        for (r in 0..15) {
                            for (c in 0..15) {
                                val inYard = (r < 6 && c < 6) || (r < 6 && c >= 9) || (r >= 9 && c < 6) || (r >= 9 && c >= 9)
                                val inCenter = r in 6..8 && c in 6..8
                                if (!inYard && !inCenter) {
                                    // Track square
                                    val isJoyTrack = (c == 7 && r < 6) || (r == 1 && c == 8)
                                    val isKolaTrack = (c == 7 && r >= 9) || (r == 13 && c == 6)
                                    val tileColor = when {
                                        isJoyTrack -> JoyMarigold.copy(alpha = 0.5f)
                                        isKolaTrack -> KolaTeal.copy(alpha = 0.5f)
                                        else -> CreamDeep
                                    }
                                    drawRect(
                                        color = tileColor,
                                        topLeft = Offset(c * cellW + 1f, r * cellH + 1f),
                                        size = androidx.compose.ui.geometry.Size(cellW - 2f, cellH - 2f)
                                    )
                                }
                            }
                        }

                        // Joy Yard Tokens (J)
                        val joyYardTokens = listOf(
                            Offset(cellW * 10.5f, cellH * 2f),
                            Offset(cellW * 13.5f, cellH * 2f),
                            Offset(cellW * 10.5f, cellH * 4f),
                            Offset(cellW * 13.5f, cellH * 4f)
                        )
                        for (pos in joyYardTokens) {
                            drawCircle(color = JoyMarigold, radius = cellW * 0.45f, center = pos)
                            drawCircle(color = Color(0x33000000), radius = cellW * 0.45f, center = pos, style = Stroke(width = 2f))
                        }

                        // Kola Yard Tokens (K)
                        val kolaYardTokens = listOf(
                            Offset(cellW * 2f, cellH * 10.5f),
                            Offset(cellW * 4.5f, cellH * 10.5f),
                            Offset(cellW * 2f, cellH * 13f),
                            Offset(cellW * 4.5f, cellH * 13f)
                        )
                        for (pos in kolaYardTokens) {
                            drawCircle(color = KolaTeal, radius = cellW * 0.45f, center = pos)
                        }

                        // Active Pieces on Track
                        // Kola glowing piece at [10.5, 7.5]
                        val kolaActivePos = Offset(cellW * 4.5f, cellH * 10.5f)
                        drawCircle(
                            color = Amber.copy(alpha = 0.6f),
                            radius = cellW * 0.6f * ringPulse.value,
                            center = kolaActivePos,
                            style = Stroke(width = 3.dp.toPx())
                        )

                        val kolaActive2 = Offset(cellW * 7.5f, cellH * 11.5f)
                        drawCircle(color = KolaTeal, radius = cellW * 0.45f, center = kolaActive2)
                        drawCircle(
                            color = Amber.copy(alpha = 0.6f),
                            radius = cellW * 0.6f * ringPulse.value,
                            center = kolaActive2,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }

                    // Interactive touch overlays for moving pieces
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                if (hasRolledThisTurn) {
                                    hasRolledThisTurn = false
                                    lastMoveSummary = "You moved your piece $diceValue spaces!"
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Controls Card: Die & Roll Button (Screen 6)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Cream,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 3D Die face with 5 dots
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = when (diceValue) {
                                        1 -> "⚀"
                                        2 -> "⚁"
                                        3 -> "⚂"
                                        4 -> "⚃"
                                        5 -> "⚄"
                                        else -> "⚅"
                                    },
                                    fontSize = 32.sp,
                                    color = Ink
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "You rolled $diceValue",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Ink
                        )
                    }

                    // Disabled Roll button when already rolled (Screen 6 exact style)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedButton(
                            onClick = {
                                if (!hasRolledThisTurn) {
                                    diceValue = (1..6).random()
                                    hasRolledThisTurn = true
                                }
                            },
                            enabled = !hasRolledThisTurn,
                            shape = RoundedCornerShape(999.dp),
                            modifier = Modifier.height(40.dp).testTag("ludo_roll_button")
                        ) {
                            Text(
                                text = "Roll",
                                fontWeight = FontWeight.Bold,
                                color = if (!hasRolledThisTurn) Ink else InkMuted
                            )
                        }
                        if (hasRolledThisTurn) {
                            Text(
                                text = "(already rolled this turn)",
                                fontSize = 10.sp,
                                color = InkMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5. Last Move Summary Line
            Text(
                text = lastMoveSummary,
                fontSize = 12.sp,
                color = CreamDeep,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 6. Bottom Action Bar: React & Talk Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Cream)
                    .padding(vertical = 6.dp, horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { showReactSheet = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.SentimentSatisfied, contentDescription = null, tint = Ink, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("React", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Ink)
                }

                Box(modifier = Modifier.width(1.dp).height(18.dp).background(InkMuted.copy(alpha = 0.3f)))

                Row(
                    modifier = Modifier.clickable { },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Mic, contentDescription = null, tint = Ink, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Talk", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Ink)
                }
            }
        }
    }

    if (showMenuSheet) {
        AlertDialog(
            onDismissRequest = { showMenuSheet = false },
            title = { Text("Game Options", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { showMenuSheet = false }) { Text("Game Rules") }
                    TextButton(onClick = { showMenuSheet = false }) { Text("Offer a Draw") }
                    TextButton(onClick = { showMenuSheet = false }) { Text("Pause Game") }
                    TextButton(onClick = {
                        showMenuSheet = false
                        onBack()
                    }) { Text("Exit to Play Hub", color = Color(0xFFC8553D)) }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMenuSheet = false }) { Text("Close") }
            }
        )
    }

    if (showReactSheet) {
        val emojis = listOf("Nice one! 🎉", "Ouch! 🙈", "Oh no! 😅", "Well played! 👏", "Rematch? 🔥", "Take your time ☕")
        AlertDialog(
            onDismissRequest = { showReactSheet = false },
            title = { Text("Send Quick Reaction", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    emojis.forEach { react ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CreamDeep,
                            modifier = Modifier.fillMaxWidth().clickable {
                                lastMoveSummary = "You reacted: $react"
                                showReactSheet = false
                            }
                        ) {
                            Text(text = react, modifier = Modifier.padding(12.dp), fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showReactSheet = false }) { Text("Cancel") }
            }
        )
    }
}
