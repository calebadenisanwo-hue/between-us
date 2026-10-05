package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameRecord
import com.example.data.model.SharedDoodle
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LavenderMist
import com.example.ui.theme.RoseCoral
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SoftRose
import com.example.ui.theme.WarmTerracotta

data class StrokeLine(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun ArcadeScreen(
    currentProfile: String,
    doodles: List<SharedDoodle>,
    recentGames: List<GameRecord>,
    onSaveDoodle: (title: String, strokeJson: String) -> Unit,
    onDeleteDoodle: (Int) -> Unit,
    onRecordGame: (gameType: String, kolaScore: Int, joyScore: Int, winner: String?, summary: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val gameTabs = listOf(
        "🎨 Doodle Canvas",
        "⭕ Love Tic-Tac-Toe",
        "⚖️ Would You Rather",
        "🧠 Couples Trivia",
        "❓ 20 Questions"
    )
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = RoseCoral
        ) {
            gameTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.testTag("arcade_tab_$index")
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> DoodleGameSection(
                    currentProfile = currentProfile,
                    savedDoodles = doodles,
                    onSaveDoodle = onSaveDoodle,
                    onDeleteDoodle = onDeleteDoodle
                )
                1 -> TicTacToeSection(
                    currentProfile = currentProfile,
                    onGameEnd = { k, j, w, s -> onRecordGame("TicTacToe", k, j, w, s) }
                )
                2 -> WouldYouRatherSection(
                    currentProfile = currentProfile,
                    onGameEnd = { k, j, w, s -> onRecordGame("WouldYouRather", k, j, w, s) }
                )
                3 -> CouplesTriviaSection(
                    currentProfile = currentProfile,
                    onGameEnd = { k, j, w, s -> onRecordGame("Trivia", k, j, w, s) }
                )
                4 -> TwentyQuestionsSection(
                    currentProfile = currentProfile,
                    onGameEnd = { k, j, w, s -> onRecordGame("TwentyQuestions", k, j, w, s) }
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 1. DOODLE CANVAS
// -----------------------------------------------------------------------------
@Composable
private fun DoodleGameSection(
    currentProfile: String,
    savedDoodles: List<SharedDoodle>,
    onSaveDoodle: (String, String) -> Unit,
    onDeleteDoodle: (Int) -> Unit
) {
    val strokes = remember { mutableStateListOf<StrokeLine>() }
    var currentPoints = remember { mutableStateListOf<Offset>() }
    var currentColor by remember { mutableStateOf(RoseCoral) }
    var strokeWidth by remember { mutableStateOf(10f) }
    var doodleTitle by remember { mutableStateOf("") }
    var savedNotice by remember { mutableStateOf(false) }

    val drawingPrompts = listOf(
        "Draw our first breakfast in bed once reunited! 🥞",
        "Draw what my voice sounds like to you 🎵",
        "Draw Kola as a dramatic cartoon superhero 🦸‍♂️",
        "Draw Joy's signature smile when she is happy 🌸",
        "Draw our dream cozy home by the ocean 🏖️",
        "Draw a secret alien disguised as a cup of coffee ☕👽"
    )
    var currentPrompt by remember { mutableStateOf(drawingPrompts[0]) }

    val palette = listOf(
        RoseCoral,
        Color(0xFF5C9DFF),
        GoldenSun,
        SageGreen,
        LavenderMist,
        WarmTerracotta,
        Color.White
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Prompt card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldenSun)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Couple Drawing Idea", style = MaterialTheme.typography.labelSmall, color = GoldenSun)
                        Text(text = currentPrompt, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                    }
                    IconButton(onClick = {
                        val next = drawingPrompts.filter { it != currentPrompt }.random()
                        currentPrompt = next
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Shuffle prompt")
                    }
                }
            }
        }

        item {
            // Live Drawing Canvas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161226)),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(currentColor, strokeWidth) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPoints = mutableStateListOf(offset)
                                },
                                onDrag = { change, _ ->
                                    currentPoints.add(change.position)
                                },
                                onDragEnd = {
                                    if (currentPoints.isNotEmpty()) {
                                        strokes.add(
                                            StrokeLine(
                                                points = currentPoints.toList(),
                                                color = currentColor,
                                                strokeWidth = strokeWidth
                                            )
                                        )
                                        currentPoints.clear()
                                    }
                                }
                            )
                        }
                        .testTag("doodle_drawing_canvas")
                ) {
                    // Draw established strokes
                    for (stroke in strokes) {
                        if (stroke.points.size > 1) {
                            val path = Path().apply {
                                moveTo(stroke.points[0].x, stroke.points[0].y)
                                for (i in 1 until stroke.points.size) {
                                    lineTo(stroke.points[i].x, stroke.points[i].y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = stroke.color,
                                style = Stroke(
                                    width = stroke.strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }

                    // Draw active drag
                    if (currentPoints.size > 1) {
                        val path = Path().apply {
                            moveTo(currentPoints[0].x, currentPoints[0].y)
                            for (i in 1 until currentPoints.size) {
                                lineTo(currentPoints[i].x, currentPoints[i].y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = currentColor,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }
        }

        item {
            // Palette & Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    palette.forEach { col ->
                        val isSelected = currentColor == col
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(col)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color.White else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { currentColor = col }
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = {
                            if (strokes.isNotEmpty()) strokes.removeAt(strokes.lastIndex)
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Undo")
                    }
                    IconButton(onClick = { strokes.clear() }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            }
        }

        item {
            // Save Doodle Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = doodleTitle,
                    onValueChange = { doodleTitle = it },
                    label = { Text("Title your art") },
                    modifier = Modifier.weight(1f).testTag("doodle_title_input"),
                    singleLine = true
                )

                Button(
                    onClick = {
                        val title = if (doodleTitle.isNotBlank()) doodleTitle else "Sweet Doodle by $currentProfile"
                        // Save lightweight representation (stroke count and author)
                        val summaryJson = "strokes:${strokes.size}|points:${strokes.sumOf { it.points.size }}"
                        onSaveDoodle(title, summaryJson)
                        savedNotice = true
                        strokes.clear()
                        doodleTitle = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseCoral),
                    modifier = Modifier.testTag("save_doodle_button")
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save")
                }
            }

            AnimatedVisibility(visible = savedNotice) {
                Text(
                    text = "Saved to Our Couple Gallery! 🖼️✨",
                    color = SageGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        if (savedDoodles.isNotEmpty()) {
            item {
                Text(
                    text = "Our Living Room Art Wall (${savedDoodles.size})",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(savedDoodles) { doodle ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = doodle.title, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "Drawn with love by ${doodle.drawnBy}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { onDeleteDoodle(doodle.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 2. LOVE TIC-TAC-TOE
// -----------------------------------------------------------------------------
@Composable
private fun TicTacToeSection(
    currentProfile: String,
    onGameEnd: (kola: Int, joy: Int, winner: String?, summary: String) -> Unit
) {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentTurn by remember { mutableStateOf("Kola") } // Kola: 💙, Joy: 🌸
    var winner by remember { mutableStateOf<String?>(null) }
    var winningIndices by remember { mutableStateOf<List<Int>>(emptyList()) }
    var kolaWins by remember { mutableIntStateOf(0) }
    var joyWins by remember { mutableIntStateOf(0) }
    var againstAi by remember { mutableStateOf(false) }

    fun checkWinner(b: List<String>): Pair<String?, List<Int>> {
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (line in lines) {
            val (a, c, d) = line
            if (b[a].isNotEmpty() && b[a] == b[c] && b[a] == b[d]) {
                return Pair(if (b[a] == "💙") "Kola" else "Joy", line)
            }
        }
        if (b.none { it.isEmpty() }) return Pair("Draw", emptyList())
        return Pair(null, emptyList())
    }

    fun makeMove(index: Int) {
        if (board[index].isNotEmpty() || winner != null) return

        val symbol = if (currentTurn == "Kola") "💙" else "🌸"
        val newBoard = board.toMutableList().also { it[index] = symbol }
        board = newBoard

        val (w, line) = checkWinner(newBoard)
        if (w != null) {
            winner = w
            winningIndices = line
            if (w == "Kola") kolaWins++
            if (w == "Joy") joyWins++
            onGameEnd(
                kolaWins,
                joyWins,
                if (w == "Draw") null else w,
                "TicTacToe result: $w won! 🏆"
            )
        } else {
            val nextTurn = if (currentTurn == "Kola") "Joy" else "Kola"
            currentTurn = nextTurn

            // AI Partner auto-move if enabled
            if (againstAi && nextTurn != currentProfile) {
                val emptySpots = newBoard.indices.filter { newBoard[it].isEmpty() }
                if (emptySpots.isNotEmpty()) {
                    val aiChoice = emptySpots.random()
                    val aiSymbol = if (nextTurn == "Kola") "💙" else "🌸"
                    val aiBoard = newBoard.toMutableList().also { it[aiChoice] = aiSymbol }
                    board = aiBoard
                    val (aiWinner, aiLine) = checkWinner(aiBoard)
                    if (aiWinner != null) {
                        winner = aiWinner
                        winningIndices = aiLine
                        if (aiWinner == "Kola") kolaWins++
                        if (aiWinner == "Joy") joyWins++
                    } else {
                        currentTurn = currentProfile
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Scoreboard
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "💙 Kola", fontWeight = FontWeight.Bold, color = Color(0xFF5C9DFF))
                        Text(text = "$kolaWins wins", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Text(text = "VS", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🌸 Joy", fontWeight = FontWeight.Bold, color = SoftRose)
                        Text(text = "$joyWins wins", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        item {
            // Mode toggle & status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        winner == "Draw" -> "It's a Tie! Both are equally cute! 🥰"
                        winner != null -> "🎉 $winner wins with all the love!"
                        else -> "Turn: ${if (currentTurn == "Kola") "💙 Kola's turn" else "🌸 Joy's turn"}"
                    },
                    fontWeight = FontWeight.Bold,
                    color = RoseCoral
                )

                FilterChip(
                    selected = againstAi,
                    onClick = { againstAi = !againstAi },
                    label = { Text(if (againstAi) "AI Partner: On" else "Pass & Play") }
                )
            }
        }

        item {
            // 3x3 Grid Board
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (row in 0..2) {
                        Row(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (col in 0..2) {
                                val idx = row * 3 + col
                                val isHighlight = winningIndices.contains(idx)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isHighlight) GoldenSun.copy(alpha = 0.35f)
                                            else MaterialTheme.colorScheme.surfaceVariant
                                        )
                                        .clickable { makeMove(idx) }
                                        .testTag("tictactoe_cell_$idx"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = board[idx],
                                        fontSize = 32.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    board = List(9) { "" }
                    winner = null
                    winningIndices = emptyList()
                    currentTurn = if (currentTurn == "Kola") "Joy" else "Kola"
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoseCoral),
                modifier = Modifier.testTag("reset_tictactoe_button")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Play Next Round")
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 3. WOULD YOU RATHER? (Couples Edition)
// -----------------------------------------------------------------------------
@Composable
private fun WouldYouRatherSection(
    currentProfile: String,
    onGameEnd: (Int, Int, String?, String) -> Unit
) {
    val scenarios = listOf(
        Pair("Live together in a beach villa in Zanzibar 🏝️", "Live in a vibrant high-rise apartment in Tokyo 🏙️"),
        Pair("Never have to do dishes again for life 🍽️", "Never have to do laundry again for life 🧺"),
        Pair("A surprise weekend getaway planned by Kola 🧳", "A luxury candlelit dining experience planned by Joy 🍷"),
        Pair("Fast forward to our reunion hug tomorrow ✈️", "Rewind and relive our first in-person date all over again 🕰️"),
        Pair("Cook an extravagant 4-course meal together that burns 🍳", "Eat midnight roadside street food in our pajamas 🌮"),
        Pair("Always have the same music taste 🎧", "Always agree on which movie or show to watch next 🍿"),
        Pair("Build a cozy blanket fortress and binge Netflix all weekend 🛌", "Go on an adventurous hiking and waterfall roadtrip 🏕️")
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var kolaChoice by remember { mutableStateOf<Int?>(null) }
    var joyChoice by remember { mutableStateOf<Int?>(null) }
    var isRevealed by remember { mutableStateOf(false) }

    val currentPair = scenarios[currentIndex]

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Scenario ${currentIndex + 1} of ${scenarios.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Playing as $currentProfile",
                    style = MaterialTheme.typography.labelSmall,
                    color = RoseCoral,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            // Option A
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (currentProfile == "Kola") kolaChoice = 1 else joyChoice = 1
                    }
                    .testTag("wyr_option_1"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        currentProfile == "Kola" && kolaChoice == 1 -> Color(0xFF1F355A)
                        currentProfile == "Joy" && joyChoice == 1 -> Color(0xFF522135)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
                border = if ((currentProfile == "Kola" && kolaChoice == 1) || (currentProfile == "Joy" && joyChoice == 1))
                    CardDefaults.outlinedCardBorder() else null
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "OPTION A",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseCoral
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentPair.first,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }

        item {
            Text(
                text = "— OR —",
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            // Option B
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (currentProfile == "Kola") kolaChoice = 2 else joyChoice = 2
                    }
                    .testTag("wyr_option_2"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        currentProfile == "Kola" && kolaChoice == 2 -> Color(0xFF1F355A)
                        currentProfile == "Joy" && joyChoice == 2 -> Color(0xFF522135)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
                border = if ((currentProfile == "Kola" && kolaChoice == 2) || (currentProfile == "Joy" && joyChoice == 2))
                    CardDefaults.outlinedCardBorder() else null
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "OPTION B",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmTerracotta
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentPair.second,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }

        item {
            // Reveal / Status
            if (!isRevealed) {
                Button(
                    onClick = {
                        // If one hasn't voted yet, auto-simulate sweet partner choice
                        if (kolaChoice == null) kolaChoice = listOf(1, 2).random()
                        if (joyChoice == null) joyChoice = listOf(1, 2).random()
                        isRevealed = true
                        val isMatch = kolaChoice == joyChoice
                        onGameEnd(
                            if (isMatch) 10 else 5,
                            if (isMatch) 10 else 5,
                            null,
                            if (isMatch) "100% Sync on Scenario ${currentIndex + 1}!" else "Opposites attract!"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseCoral),
                    modifier = Modifier.fillMaxWidth().testTag("reveal_wyr_button")
                ) {
                    Text("Reveal Both Answers & Compatibility 🔮")
                }
            } else {
                val isMatch = kolaChoice == joyChoice
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isMatch) SageGreen.copy(alpha = 0.2f) else LavenderMist.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isMatch) "🎉 100% Soulmate Match!" else "💫 Cute Contrast!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isMatch) SageGreen else WarmTerracotta
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Kola chose Option ${if (kolaChoice == 1) "A" else "B"} 💙 | Joy chose Option ${if (joyChoice == 1) "A" else "B"} 🌸",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                currentIndex = (currentIndex + 1) % scenarios.size
                                kolaChoice = null
                                joyChoice = null
                                isRevealed = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Next Dilemma ➡️")
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 4. COUPLES TRIVIA QUIZ
// -----------------------------------------------------------------------------
@Composable
private fun CouplesTriviaSection(
    currentProfile: String,
    onGameEnd: (Int, Int, String?, String) -> Unit
) {
    data class QuizItem(
        val question: String,
        val options: List<String>,
        val correctIndex: Int,
        val explanation: String
    )

    val questions = listOf(
        QuizItem(
            question = "When Kola is stressed out, what is his number one comfort snack?",
            options = listOf("Hot chocolate with marshmallows", "Spicy suya & cold soda", "Chocolate chip cookies", "Plain tea with milk"),
            correctIndex = 1,
            explanation = "Spicy suya and cold soda fixes any rough day for Kola! 🌶️🥤"
        ),
        QuizItem(
            question = "What was the very first movie/series Kola and Joy watched simultaneously on video call?",
            options = listOf("Inception", "Spider-Man: Across the Spider-Verse", "Love Is Blind", "Black Panther"),
            correctIndex = 1,
            explanation = "Across the Spider-Verse! You both talked about the animation for three days straight!"
        ),
        QuizItem(
            question = "What is Joy's signature reaction when she receives an unexpected sweet text?",
            options = listOf("Gasps and covers her face with hands", "Sends 10 heart emojis instantly", "Calls immediately without texting", "Screenshots and re-reads it three times"),
            correctIndex = 3,
            explanation = "Joy screenshots it and re-reads it to smile all afternoon! 🥰"
        ),
        QuizItem(
            question = "If Kola and Joy were to adopt a pet together, what would it be?",
            options = listOf("A fluffy Golden Retriever", "A cheeky Calico kitten", "A baby turtle", "A cute parrot that repeats 'I love you'"),
            correctIndex = 0,
            explanation = "A big energetic Golden Retriever named Simba! 🐕"
        )
    )

    var currentQIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var quizFinished by remember { mutableStateOf(false) }

    val currentQ = questions[currentQIndex]

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (!quizFinished) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Question ${currentQIndex + 1} of ${questions.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Score: $score",
                        fontWeight = FontWeight.Bold,
                        color = GoldenSun
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = currentQ.question,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            itemsIndexed(currentQ.options) { idx, option ->
                val isSelected = selectedOption == idx
                val isCorrect = idx == currentQ.correctIndex
                val backgroundColor = when {
                    answered && isCorrect -> SageGreen.copy(alpha = 0.3f)
                    answered && isSelected && !isCorrect -> RoseCoral.copy(alpha = 0.3f)
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(enabled = !answered) {
                            selectedOption = idx
                        }
                        .testTag("trivia_option_$idx"),
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${'A' + idx}.",
                            fontWeight = FontWeight.Bold,
                            color = RoseCoral
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        if (answered && isCorrect) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SageGreen)
                        }
                    }
                }
            }

            item {
                if (!answered) {
                    Button(
                        onClick = {
                            if (selectedOption != null) {
                                answered = true
                                if (selectedOption == currentQ.correctIndex) {
                                    score++
                                }
                            }
                        },
                        enabled = selectedOption != null,
                        modifier = Modifier.fillMaxWidth().testTag("trivia_submit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = RoseCoral)
                    ) {
                        Text("Confirm Answer")
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = currentQ.explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = {
                                if (currentQIndex < questions.size - 1) {
                                    currentQIndex++
                                    selectedOption = null
                                    answered = false
                                } else {
                                    quizFinished = true
                                    onGameEnd(score, score, currentProfile, "Couples Trivia score: $score/${questions.size}")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (currentQIndex < questions.size - 1) "Next Question ➡️" else "See Results 🏆")
                        }
                    }
                }
            }
        } else {
            // Results screen
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "👑", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Trivia Complete!",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "You scored $score out of ${questions.size}",
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldenSun
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (score >= 3)
                                "Certified Soulmate Mind Reader! You two know each other inside out ❤️✨"
                            else
                                "Sweet connection! Still so many fun quirks to discover on your next video date! 💬",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                currentQIndex = 0
                                selectedOption = null
                                score = 0
                                answered = false
                                quizFinished = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoseCoral)
                        ) {
                            Text("Play Again 🔄")
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 5. 20 QUESTIONS
// -----------------------------------------------------------------------------
@Composable
private fun TwentyQuestionsSection(
    currentProfile: String,
    onGameEnd: (Int, Int, String?, String) -> Unit
) {
    var secretWord by remember { mutableStateOf("Greenwich Sunset") }
    var isWordLocked by remember { mutableStateOf(true) }
    var questionsLeft by remember { mutableIntStateOf(20) }
    var guessInput by remember { mutableStateOf("") }
    var gameStatus by remember { mutableStateOf<String?>(null) } // "Won", "Lost", null
    val questionLog = remember { mutableStateListOf<Pair<String, String>>() }
    var currentQuestionText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "20 Questions Game",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "$questionsLeft Questions Left",
                            fontWeight = FontWeight.ExtraBold,
                            color = if (questionsLeft <= 5) RoseCoral else GoldenSun
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "One partner locks in a secret memory or object. The other asks yes/no questions to deduce it before turns run out!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            // Secret Word Lock or Reveal
            if (!isWordLocked) {
                OutlinedTextField(
                    value = secretWord,
                    onValueChange = { secretWord = it },
                    label = { Text("Set Secret Memory / Item") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        isWordLocked = true
                        questionsLeft = 20
                        gameStatus = null
                        questionLog.clear()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RoseCoral)
                ) {
                    Text("Lock Secret Word & Start Game 🔒")
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Secret word is locked 🔒",
                        style = MaterialTheme.typography.labelMedium,
                        color = SageGreen,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { isWordLocked = false }) {
                        Text("Change Word")
                    }
                }
            }
        }

        if (isWordLocked && gameStatus == null) {
            item {
                // Ask Question Box
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = currentQuestionText,
                        onValueChange = { currentQuestionText = it },
                        label = { Text("Ask a Yes/No question (e.g. 'Is it food?')") },
                        modifier = Modifier.fillMaxWidth().testTag("twenty_q_input"),
                        singleLine = true
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (currentQuestionText.isNotBlank()) {
                                    questionLog.add(Pair(currentQuestionText, "YES ✅"))
                                    currentQuestionText = ""
                                    questionsLeft = (questionsLeft - 1).coerceAtLeast(0)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SageGreen)
                        ) {
                            Text("Answer: YES")
                        }
                        Button(
                            onClick = {
                                if (currentQuestionText.isNotBlank()) {
                                    questionLog.add(Pair(currentQuestionText, "NO ❌"))
                                    currentQuestionText = ""
                                    questionsLeft = (questionsLeft - 1).coerceAtLeast(0)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = RoseCoral)
                        ) {
                            Text("Answer: NO")
                        }
                    }
                }
            }

            item {
                // Submit Guess Box
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = guessInput,
                        onValueChange = { guessInput = it },
                        label = { Text("Ready to solve? Make final guess!") },
                        modifier = Modifier.weight(1f).testTag("twenty_q_guess_input"),
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            if (guessInput.trim().equals(secretWord.trim(), ignoreCase = true)) {
                                gameStatus = "Won"
                                onGameEnd(1, 1, currentProfile, "Guessed '$secretWord' in 20 Questions!")
                            } else {
                                questionsLeft = (questionsLeft - 2).coerceAtLeast(0)
                                if (questionsLeft <= 0) {
                                    gameStatus = "Lost"
                                }
                            }
                            guessInput = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldenSun)
                    ) {
                        Text("Guess", color = Color(0xFF332000), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (gameStatus != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (gameStatus == "Won") SageGreen.copy(alpha = 0.2f) else RoseCoral.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (gameStatus == "Won") "🎉 BINGO! YOU GUESSED IT!" else "😢 Out of Questions!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "The secret was: $secretWord",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = {
                            isWordLocked = false
                            gameStatus = null
                            questionsLeft = 20
                        }) {
                            Text("New Round")
                        }
                    }
                }
            }
        }

        if (questionLog.isNotEmpty()) {
            item {
                Text(
                    text = "Question Clues Log",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            items(questionLog) { (q, ans) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Q: $q", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                        Text(text = ans, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
