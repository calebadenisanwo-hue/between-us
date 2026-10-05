package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.ui.theme.Sage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 52-cell track as defined in Prompt 6 (15x15 board coordinate map)
val LUDO_TRACK_COORDS = listOf(
    Pair(6, 1), Pair(6, 2), Pair(6, 3), Pair(6, 4), Pair(6, 5), Pair(5, 6), Pair(4, 6), Pair(3, 6), Pair(2, 6), Pair(1, 6), Pair(0, 6), Pair(0, 7), Pair(0, 8),
    Pair(1, 8), Pair(2, 8), Pair(3, 8), Pair(4, 8), Pair(5, 8), Pair(6, 9), Pair(6, 10), Pair(6, 11), Pair(6, 12), Pair(6, 13), Pair(6, 14), Pair(7, 14), Pair(8, 14),
    Pair(8, 13), Pair(8, 12), Pair(8, 11), Pair(8, 10), Pair(8, 9), Pair(9, 8), Pair(10, 8), Pair(11, 8), Pair(12, 8), Pair(13, 8), Pair(14, 8), Pair(14, 7), Pair(14, 6),
    Pair(13, 6), Pair(12, 6), Pair(11, 6), Pair(10, 6), Pair(9, 6), Pair(8, 5), Pair(8, 4), Pair(8, 3), Pair(8, 2), Pair(8, 1), Pair(8, 0), Pair(7, 0), Pair(6, 0)
)

val LUDO_SAFE_CELLS = setOf(0, 8, 13, 21, 26, 34, 39, 47)
val LUDO_START_CELLS = listOf(39, 13) // Seat 0 (Kola) starts at 39, Seat 1 (Joy) starts at 13

// Home columns (5 steps to center)
val LUDO_HOME_COLUMNS = listOf(
    listOf(Pair(13, 7), Pair(12, 7), Pair(11, 7), Pair(10, 7), Pair(9, 7)), // Seat 0 (Kola)
    listOf(Pair(1, 7), Pair(2, 7), Pair(3, 7), Pair(4, 7), Pair(5, 7))     // Seat 1 (Joy)
)

// Yard positions
val LUDO_YARD_SLOTS = listOf(
    listOf(Pair(10, 1), Pair(10, 4), Pair(13, 1), Pair(13, 4)), // Seat 0 (Kola, bottom-left)
    listOf(Pair(1, 10), Pair(1, 13), Pair(4, 10), Pair(4, 13))  // Seat 1 (Joy, top-right)
)

@Composable
fun LudoGameScreen(
    partnerName: String = "Joy",
    onBack: () -> Unit,
    onGameFinished: (winner: String, score: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    // 0 = Kola (Teal), 1 = Joy (Marigold)
    var currentSeat by remember { mutableIntStateOf(0) }
    var phase by remember { mutableStateOf("roll") } // "roll" or "move"
    var diceValue by remember { mutableIntStateOf(0) }
    var isRollingAnimation by remember { mutableStateOf(false) }

    // Piece positions: -1 (yard), 0..50 (track), 51..55 (home column), 56 (finished)
    val kolaPieces = remember { mutableStateListOf(-1, -1, 0, -1) }
    val joyPieces = remember { mutableStateListOf(-1, -1, 0, -1) }

    var lastMoveSummary by remember { mutableStateOf("Game started. Roll the die to begin!") }
    var winner by remember { mutableStateOf<String?>(null) }
    var autoPlayPartner by remember { mutableStateOf(true) }

    var showMenuSheet by remember { mutableStateOf(false) }
    var showReactSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Pulse animation for movable pieces
    val ringPulse = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        ringPulse.animateTo(
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    // Helper: Check which pieces can legally move
    fun getLegalPieces(seat: Int, roll: Int): List<Int> {
        val pieces = if (seat == 0) kolaPieces else joyPieces
        val legal = mutableListOf<Int>()
        for (i in 0 until 4) {
            val p = pieces[i]
            if (p == -1 && roll == 6) {
                legal.add(i)
            } else if (p in 0..55) {
                if (p + roll <= 56) {
                    legal.add(i)
                }
            }
        }
        return legal
    }

    var triggerRoll: () -> Unit = {}

    // Execute Move Logic with capturing and extra turns
    fun executeMove(seat: Int, pieceIdx: Int, roll: Int) {
        val pieces = if (seat == 0) kolaPieces else joyPieces
        val opponentPieces = if (seat == 0) joyPieces else kolaPieces
        val currentP = pieces[pieceIdx]

        var extraTurn = false
        val newP = if (currentP == -1) 0 else currentP + roll
        pieces[pieceIdx] = newP

        val playerName = if (seat == 0) "Kola" else partnerName
        val oppName = if (seat == 0) partnerName else "Kola"

        if (currentP == -1) {
            lastMoveSummary = "$playerName rolled 6 and brought a piece out! 🚀"
            extraTurn = true // rolling 6 grants another turn
        } else if (newP == 56) {
            lastMoveSummary = "$playerName moved a piece safely home! 🏆"
            extraTurn = true // reaching home grants another turn
        } else if (newP in 1..50) {
            // Check capture
            val absTrack = (LUDO_START_CELLS[seat] + newP) % 52
            if (!LUDO_SAFE_CELLS.contains(absTrack)) {
                // Check if opponent piece is here
                for (opIdx in 0 until 4) {
                    val oppP = opponentPieces[opIdx]
                    if (oppP in 1..50) {
                        val oppTrack = (LUDO_START_CELLS[1 - seat] + oppP) % 52
                        if (oppTrack == absTrack) {
                            opponentPieces[opIdx] = -1 // Captured!
                            lastMoveSummary = "$playerName captured $oppName's piece! 💥 Extra turn!"
                            extraTurn = true
                            break
                        }
                    }
                }
            }
            if (!extraTurn) {
                lastMoveSummary = "$playerName moved piece $roll spaces."
            }
        } else {
            lastMoveSummary = "$playerName moved closer to home."
        }

        // Check Win Condition
        if (pieces.all { it == 56 }) {
            winner = playerName
            lastMoveSummary = "🎉 $playerName wins the Ludo match!"
            onGameFinished(playerName, "4-0")
            return
        }

        // Handle Turn Transition
        if (extraTurn) {
            phase = "roll"
            // If it's AI turn, auto roll
            if (seat == 1 && autoPlayPartner) {
                coroutineScope.launch {
                    delay(1000)
                    triggerRoll()
                }
            }
        } else {
            currentSeat = 1 - currentSeat
            phase = "roll"
            if (currentSeat == 1 && autoPlayPartner) {
                coroutineScope.launch {
                    delay(1200)
                    triggerRoll()
                }
            }
        }
    }

    // Roll Function
    triggerRoll = {
        if (phase == "roll" && !isRollingAnimation) {
            isRollingAnimation = true

            coroutineScope.launch {
                // Roll animation cycles dice
                for (i in 0..6) {
                    diceValue = (1..6).random()
                    delay(60)
                }
                isRollingAnimation = false

                val legal = getLegalPieces(currentSeat, diceValue)
                val playerName = if (currentSeat == 0) "Kola" else partnerName

                if (legal.isEmpty()) {
                    lastMoveSummary = "$playerName rolled $diceValue. No legal move available."
                    if (diceValue == 6) {
                        phase = "roll"
                        if (currentSeat == 1 && autoPlayPartner) {
                            delay(1000)
                            triggerRoll()
                        }
                    } else {
                        currentSeat = 1 - currentSeat
                        phase = "roll"
                        if (currentSeat == 1 && autoPlayPartner) {
                            delay(1200)
                            triggerRoll()
                        }
                    }
                } else if (legal.size == 1 && (currentSeat == 1 && autoPlayPartner)) {
                    phase = "move"
                    delay(600)
                    executeMove(currentSeat, legal.first(), diceValue)
                } else if (currentSeat == 1 && autoPlayPartner) {
                    phase = "move"
                    delay(800)
                    val best = legal.maxByOrNull { joyPieces[it] } ?: legal.first()
                    executeMove(currentSeat, best, diceValue)
                } else {
                    phase = "move"
                }
            }
        }
    }

    val legalPiecesNow = if (phase == "move") getLegalPieces(currentSeat, diceValue) else emptyList()

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
                                text = if (winner != null) "Game Over"
                                else if (currentSeat == 0) "Your turn" else "$partnerName's turn",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Ink
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (currentSeat == 0) KolaTeal else JoyMarigold)
                            )
                        }

                        IconButton(onClick = { showMenuSheet = true }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.MoreHoriz, contentDescription = "Menu", tint = Ink)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (winner != null) "🎉 $winner won the match!"
                        else if (phase == "roll") "Tap 'Roll' to roll the die"
                        else "Tap a glowing piece to move $diceValue spaces",
                        fontSize = 12.sp,
                        color = InkMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Players Bar: Kola vs Joy
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val kolaHome = kolaPieces.count { it == 56 }
                val joyHome = joyPieces.count { it == 56 }

                // Kola Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (currentSeat == 0) Cream else CreamDeep.copy(alpha = 0.8f),
                    modifier = Modifier.weight(1f),
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(KolaTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("K", color = Cream, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Kola (You)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                for (i in 1..4) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (i <= kolaHome) KolaTeal else Color(0x332F9E9A))
                                    )
                                }
                            }
                            Text("Home $kolaHome of 4", fontSize = 10.sp, color = InkMuted)
                        }
                    }
                }

                // Joy Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (currentSeat == 1) Cream else CreamDeep.copy(alpha = 0.8f),
                    modifier = Modifier.weight(1f),
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(JoyMarigold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("J", color = Cream, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(partnerName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                for (i in 1..4) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (i <= joyHome) JoyMarigold else Color(0x33F4A93A))
                                    )
                                }
                            }
                            Text("Home $joyHome of 4", fontSize = 10.sp, color = InkMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. 15x15 Interactive Ludo Board
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
                        .padding(8.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val cellW = w / 15f
                        val cellH = h / 15f

                        // Top-Left: Unused Gray
                        drawRoundRect(
                            color = Color(0xFFDDD5C7),
                            topLeft = Offset(0f, 0f),
                            size = androidx.compose.ui.geometry.Size(cellW * 6, cellH * 6),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f)
                        )
                        // Top-Right: Joy's Marigold Yard
                        drawRoundRect(
                            color = JoyMarigold,
                            topLeft = Offset(cellW * 9, 0f),
                            size = androidx.compose.ui.geometry.Size(cellW * 6, cellH * 6),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f)
                        )
                        // Bottom-Left: Kola's Teal Yard
                        drawRoundRect(
                            color = KolaTeal,
                            topLeft = Offset(0f, cellH * 9),
                            size = androidx.compose.ui.geometry.Size(cellW * 6, cellH * 6),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f)
                        )
                        // Bottom-Right: Unused Gray
                        drawRoundRect(
                            color = Color(0xFFDDD5C7),
                            topLeft = Offset(cellW * 9, cellH * 9),
                            size = androidx.compose.ui.geometry.Size(cellW * 6, cellH * 6),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f)
                        )

                        // Center Finish Box & Triangles
                        drawRect(
                            color = CreamDeep,
                            topLeft = Offset(cellW * 6, cellH * 6),
                            size = androidx.compose.ui.geometry.Size(cellW * 3, cellH * 3)
                        )
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

                        // Draw Grid Tracks
                        for (r in 0..14) {
                            for (c in 0..14) {
                                val inYard = (r < 6 && c < 6) || (r < 6 && c >= 9) || (r >= 9 && c < 6) || (r >= 9 && c >= 9)
                                val inCenter = r in 6..8 && c in 6..8
                                if (!inYard && !inCenter) {
                                    val isJoyHomeTrack = (c == 7 && r in 1..5)
                                    val isKolaHomeTrack = (c == 7 && r in 9..13)
                                    val isJoyStart = (r == 1 && c == 8)
                                    val isKolaStart = (r == 13 && c == 6)

                                    val tileColor = when {
                                        isJoyHomeTrack || isJoyStart -> JoyMarigold.copy(alpha = 0.4f)
                                        isKolaHomeTrack || isKolaStart -> KolaTeal.copy(alpha = 0.4f)
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

                        // Draw Star on Safe cells
                        LUDO_SAFE_CELLS.forEach { trackIdx ->
                            val (r, c) = LUDO_TRACK_COORDS[trackIdx]
                            drawCircle(
                                color = Amber,
                                radius = cellW * 0.2f,
                                center = Offset((c + 0.5f) * cellW, (r + 0.5f) * cellH)
                            )
                        }

                        // Draw Kola Pieces
                        for (i in 0 until 4) {
                            val p = kolaPieces[i]
                            val coords = when {
                                p == -1 -> LUDO_YARD_SLOTS[0][i]
                                p in 0..50 -> {
                                    val trackIdx = (LUDO_START_CELLS[0] + p) % 52
                                    LUDO_TRACK_COORDS[trackIdx]
                                }
                                p in 51..55 -> LUDO_HOME_COLUMNS[0][p - 51]
                                else -> Pair(7, 7) // in finish
                            }

                            val center = Offset((coords.second + 0.5f) * cellW, (coords.first + 0.5f) * cellH)
                            val isLegal = currentSeat == 0 && phase == "move" && legalPiecesNow.contains(i)

                            if (isLegal) {
                                drawCircle(
                                    color = Amber,
                                    radius = cellW * 0.55f * ringPulse.value,
                                    center = center,
                                    style = Stroke(width = 3.dp.toPx())
                                )
                            }

                            drawCircle(color = KolaTeal, radius = cellW * 0.42f, center = center)
                            drawCircle(color = Color.White, radius = cellW * 0.42f, center = center, style = Stroke(width = 1.5f))
                        }

                        // Draw Joy Pieces
                        for (i in 0 until 4) {
                            val p = joyPieces[i]
                            val coords = when {
                                p == -1 -> LUDO_YARD_SLOTS[1][i]
                                p in 0..50 -> {
                                    val trackIdx = (LUDO_START_CELLS[1] + p) % 52
                                    LUDO_TRACK_COORDS[trackIdx]
                                }
                                p in 51..55 -> LUDO_HOME_COLUMNS[1][p - 51]
                                else -> Pair(7, 7)
                            }

                            val center = Offset((coords.second + 0.5f) * cellW, (coords.first + 0.5f) * cellH)
                            val isLegal = currentSeat == 1 && phase == "move" && legalPiecesNow.contains(i)

                            if (isLegal) {
                                drawCircle(
                                    color = Amber,
                                    radius = cellW * 0.55f * ringPulse.value,
                                    center = center,
                                    style = Stroke(width = 3.dp.toPx())
                                )
                            }

                            drawCircle(color = JoyMarigold, radius = cellW * 0.42f, center = center)
                            drawCircle(color = Color.White, radius = cellW * 0.42f, center = center, style = Stroke(width = 1.5f))
                        }
                    }

                    // Touch Handlers for Legal Movable Pieces
                    if (phase == "move") {
                        val activePieces = if (currentSeat == 0) kolaPieces else joyPieces
                        legalPiecesNow.forEach { pieceIdx ->
                            val p = activePieces[pieceIdx]
                            val coords = when {
                                p == -1 -> LUDO_YARD_SLOTS[currentSeat][pieceIdx]
                                p in 0..50 -> {
                                    val trackIdx = (LUDO_START_CELLS[currentSeat] + p) % 52
                                    LUDO_TRACK_COORDS[trackIdx]
                                }
                                p in 51..55 -> LUDO_HOME_COLUMNS[currentSeat][p - 51]
                                else -> Pair(7, 7)
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        executeMove(currentSeat, pieceIdx, diceValue)
                                    }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Controls Card: Die & Roll Button
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
                    // 3D Die face
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    if (phase == "roll" && (currentSeat == 0 || !autoPlayPartner)) {
                                        triggerRoll()
                                    }
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = when (diceValue) {
                                        1 -> "⚀"
                                        2 -> "⚁"
                                        3 -> "⚂"
                                        4 -> "⚃"
                                        5 -> "⚄"
                                        6 -> "⚅"
                                        else -> "🎲"
                                    },
                                    fontSize = 32.sp,
                                    color = Ink
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (diceValue > 0) "Rolled: $diceValue" else "Roll to start",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Ink
                            )
                            if (phase == "move") {
                                Text(
                                    text = "Tap a glowing piece",
                                    fontSize = 11.sp,
                                    color = KolaTeal,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Roll button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = { triggerRoll() },
                            enabled = phase == "roll" && !isRollingAnimation && (currentSeat == 0 || !autoPlayPartner),
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Amber),
                            modifier = Modifier.height(42.dp).testTag("ludo_roll_button")
                        ) {
                            Text(
                                text = if (isRollingAnimation) "Rolling..." else "Roll",
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        }
                        if (phase == "move") {
                            Text(
                                text = "(already rolled)",
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
                color = Cream,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 6. Bottom Action Bar: React & Talk
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.70f)
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
                    modifier = Modifier.clickable {
                        // Toggle partner auto play for local testing
                        autoPlayPartner = !autoPlayPartner
                        lastMoveSummary = if (autoPlayPartner) "Joy AI companion enabled" else "Pass-and-play enabled"
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (autoPlayPartner) "🤖 Auto Joy" else "👥 2-Player", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Ink)
                }
            }
        }
    }

    if (showMenuSheet) {
        AlertDialog(
            onDismissRequest = { showMenuSheet = false },
            title = { Text("Ludo Match Options", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Need 6 to leave yard.\n• Rolling 6, capturing, or reaching home grants an extra turn.\n• Stars are safe zones (no capture).", fontSize = 12.sp, color = InkMuted)
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(onClick = {
                        kolaPieces.fill(-1)
                        joyPieces.fill(-1)
                        kolaPieces[0] = 0
                        joyPieces[0] = 0
                        currentSeat = 0
                        phase = "roll"
                        diceValue = 0
                        winner = null
                        lastMoveSummary = "Game reset. Kola to roll."
                        showMenuSheet = false
                    }) { Text("Restart Game", color = Amber, fontWeight = FontWeight.Bold) }
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    lastMoveSummary = "You sent: $react"
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
