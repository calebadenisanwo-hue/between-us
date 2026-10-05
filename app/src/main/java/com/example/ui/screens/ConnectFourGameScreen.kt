package com.example.ui.screens

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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.DuskBlue
import com.example.ui.theme.Ink
import com.example.ui.theme.InkMuted
import com.example.ui.theme.JoyMarigold
import com.example.ui.theme.KolaTeal
import com.example.ui.theme.Plum
import com.example.ui.theme.PlumDeep
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConnectFourGameScreen(
    partnerName: String = "Joy",
    onBack: () -> Unit,
    onGameFinished: (winner: String, score: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    // 7 cols x 6 rows: 0 = empty, 1 = Kola (Teal), 2 = Joy (Marigold)
    val grid = remember { mutableStateListOf(*Array(42) { 0 }) }
    var currentTurn by remember { mutableIntStateOf(1) } // 1 = Kola, 2 = Joy
    var winner by remember { mutableStateOf<String?>(null) }
    var winningIndices by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var lastMoveSummary by remember { mutableStateOf("Kola drops first disc.") }
    var kolaWins by remember { mutableIntStateOf(3) }
    var joyWins by remember { mutableIntStateOf(2) }
    var autoPlayJoy by remember { mutableStateOf(true) }

    var showMenuSheet by remember { mutableStateOf(false) }
    var showReactSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Check 4-in-a-row
    fun checkWin(seat: Int): Set<Int>? {
        fun at(r: Int, c: Int) = if (r in 0..5 && c in 0..6) grid[r * 7 + c] else 0

        // Horizontal
        for (r in 0..5) {
            for (c in 0..3) {
                if (at(r, c) == seat && at(r, c + 1) == seat && at(r, c + 2) == seat && at(r, c + 3) == seat) {
                    return setOf(r * 7 + c, r * 7 + c + 1, r * 7 + c + 2, r * 7 + c + 3)
                }
            }
        }
        // Vertical
        for (r in 0..2) {
            for (c in 0..6) {
                if (at(r, c) == seat && at(r + 1, c) == seat && at(r + 2, c) == seat && at(r + 3, c) == seat) {
                    return setOf(r * 7 + c, (r + 1) * 7 + c, (r + 2) * 7 + c, (r + 3) * 7 + c)
                }
            }
        }
        // Diagonal down-right
        for (r in 0..2) {
            for (c in 0..3) {
                if (at(r, c) == seat && at(r + 1, c + 1) == seat && at(r + 2, c + 2) == seat && at(r + 3, c + 3) == seat) {
                    return setOf(r * 7 + c, (r + 1) * 7 + c + 1, (r + 2) * 7 + c + 2, (r + 3) * 7 + c + 3)
                }
            }
        }
        // Diagonal up-right
        for (r in 3..5) {
            for (c in 0..3) {
                if (at(r, c) == seat && at(r - 1, c + 1) == seat && at(r - 2, c + 2) == seat && at(r - 3, c + 3) == seat) {
                    return setOf(r * 7 + c, (r - 1) * 7 + c + 1, (r - 2) * 7 + c + 2, (r - 3) * 7 + c + 3)
                }
            }
        }
        return null
    }

    // Drop Disc in column
    fun dropDisc(col: Int) {
        if (winner != null) return

        // Find lowest available row in this column
        var targetRow = -1
        for (r in 5 downTo 0) {
            if (grid[r * 7 + col] == 0) {
                targetRow = r
                break
            }
        }
        if (targetRow == -1) return // Column full

        val idx = targetRow * 7 + col
        grid[idx] = currentTurn

        val playerName = if (currentTurn == 1) "Kola" else partnerName
        val winCells = checkWin(currentTurn)

        if (winCells != null) {
            winner = playerName
            winningIndices = winCells
            if (currentTurn == 1) kolaWins++ else joyWins++
            lastMoveSummary = "🎉 $playerName connected four!"
            onGameFinished(playerName, "$kolaWins-$joyWins")
            return
        }

        if (grid.none { it == 0 }) {
            winner = "Draw"
            lastMoveSummary = "Board full. It's a draw! 🤝"
            onGameFinished("Draw", "$kolaWins-$joyWins")
            return
        }

        lastMoveSummary = "$playerName dropped disc in column ${col + 1}"
        currentTurn = if (currentTurn == 1) 2 else 1

        // Joy AI turn
        if (currentTurn == 2 && autoPlayJoy && winner == null) {
            coroutineScope.launch {
                delay(600)
                // Pick a column with space
                val validCols = (0..6).filter { c -> grid[c] == 0 }
                if (validCols.isNotEmpty()) {
                    dropDisc(validCols.random())
                }
            }
        }
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

            // 1. Status Banner Card
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
                                text = if (winner != null) "$winner Wins!"
                                else if (currentTurn == 1) "Your turn (Teal)" else "$partnerName's turn (Marigold)",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Ink
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (currentTurn == 1) KolaTeal else JoyMarigold)
                            )
                        }

                        IconButton(onClick = { showMenuSheet = true }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.MoreHoriz, contentDescription = "Menu", tint = Ink)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (winner != null) "Game finished!" else "Tap any column to drop your disc",
                        fontSize = 12.sp,
                        color = InkMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Score bar: Played 5 times · Joy leads 3-2 (From Mockup Screen 4)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Cream,
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(KolaTeal))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Kola: $kolaWins", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
                    }

                    Text("Played ${kolaWins + joyWins} times", fontSize = 11.sp, color = InkMuted)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(JoyMarigold))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("$partnerName: $joyWins", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. 7x6 Interactive Connect Four Grid
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = DuskBlue,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(7f / 6f)
                    .testTag("connect_four_board")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cellW = size.width / 7f
                        val cellH = size.height / 6f

                        for (r in 0..5) {
                            for (c in 0..6) {
                                val idx = r * 7 + c
                                val v = grid[idx]
                                val center = Offset((c + 0.5f) * cellW, (r + 0.5f) * cellH)
                                val radius = minOf(cellW, cellH) * 0.40f

                                val discColor = when (v) {
                                    1 -> KolaTeal
                                    2 -> JoyMarigold
                                    else -> Color(0xFF1E2838) // Empty slot
                                }

                                drawCircle(color = discColor, radius = radius, center = center)

                                // Highlight winning 4 discs
                                if (winningIndices.contains(idx)) {
                                    drawCircle(
                                        color = Amber,
                                        radius = radius * 1.15f,
                                        center = center,
                                        style = Stroke(width = 4.dp.toPx())
                                    )
                                }
                            }
                        }
                    }

                    // 7 Column click targets
                    Row(modifier = Modifier.fillMaxSize()) {
                        for (c in 0..6) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .clickable {
                                        if (currentTurn == 1 || !autoPlayJoy) {
                                            dropDisc(c)
                                        }
                                    }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Last move summary line
            Text(
                text = lastMoveSummary,
                fontSize = 13.sp,
                color = Cream,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Bottom controls
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
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
                        autoPlayJoy = !autoPlayJoy
                        lastMoveSummary = if (autoPlayJoy) "Joy AI auto drop enabled" else "Pass-and-play enabled"
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (autoPlayJoy) "🤖 Auto Joy" else "👥 2-Player", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Ink)
                }
            }
        }
    }

    if (showMenuSheet) {
        AlertDialog(
            onDismissRequest = { showMenuSheet = false },
            title = { Text("Connect Four Options", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = {
                        grid.fill(0)
                        currentTurn = 1
                        winner = null
                        winningIndices = emptySet()
                        lastMoveSummary = "Game reset. Kola drops first."
                        showMenuSheet = false
                    }) { Text("New Round", color = Amber, fontWeight = FontWeight.Bold) }
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
        val emojis = listOf("Blocked! 🛡️", "Nice drop! 🔴", "Four in a row! 🎉", "Rematch? 🔥")
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
