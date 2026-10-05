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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.ui.theme.Ink
import com.example.ui.theme.InkMuted
import com.example.ui.theme.JoyMarigold
import com.example.ui.theme.KolaTeal
import com.example.ui.theme.Plum
import com.example.ui.theme.PlumDeep
import com.example.ui.theme.Rose
import com.example.ui.theme.Sage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

// Chess piece model
data class ChessPiece(
    val type: Char, // 'P', 'R', 'N', 'B', 'Q', 'K'
    val isWhite: Boolean // White = Kola, Black = Joy
)

@Composable
fun ChessGameScreen(
    partnerName: String = "Joy",
    onBack: () -> Unit,
    onGameFinished: (winner: String, moves: Int) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    // 8x8 Board state (row 0: Black back rank, row 7: White back rank)
    val board = remember {
        mutableStateListOf<ChessPiece?>().apply {
            // Row 0: Black pieces
            addAll(listOf(
                ChessPiece('R', false), ChessPiece('N', false), ChessPiece('B', false), ChessPiece('Q', false),
                ChessPiece('K', false), ChessPiece('B', false), ChessPiece('N', false), ChessPiece('R', false)
            ))
            // Row 1: Black pawns
            for (i in 0..7) add(ChessPiece('P', false))
            // Rows 2..5: Empty
            for (i in 0..31) add(null)
            // Row 6: White pawns
            for (i in 0..7) add(ChessPiece('P', true))
            // Row 7: White pieces
            addAll(listOf(
                ChessPiece('R', true), ChessPiece('N', true), ChessPiece('B', true), ChessPiece('Q', true),
                ChessPiece('K', true), ChessPiece('B', true), ChessPiece('N', true), ChessPiece('R', true)
            ))
        }
    }

    var isWhiteTurn by remember { mutableStateOf(true) }
    var selectedSquare by remember { mutableStateOf<Int?>(null) }
    val capturedByWhite = remember { mutableStateListOf<Char>() }
    val capturedByBlack = remember { mutableStateListOf<Char>() }
    val moveHistory = remember { mutableStateListOf<String>() }

    var lastMoveSummary by remember { mutableStateOf("White (Kola) to move.") }
    var winner by remember { mutableStateOf<String?>(null) }
    var autoPlayJoy by remember { mutableStateOf(true) }

    var showMenuSheet by remember { mutableStateOf(false) }
    var showReactSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Compute legal moves for a given square
    fun computeLegalMoves(sq: Int): List<Int> {
        val piece = board[sq] ?: return emptyList()
        val r = sq / 8
        val c = sq % 8
        val moves = mutableListOf<Int>()

        fun addIfValid(nr: Int, nc: Int): Boolean {
            if (nr !in 0..7 || nc !in 0..7) return false
            val target = board[nr * 8 + nc]
            if (target == null) {
                moves.add(nr * 8 + nc)
                return true
            } else if (target.isWhite != piece.isWhite) {
                moves.add(nr * 8 + nc)
                return false // blocked after capture
            }
            return false // blocked by own piece
        }

        when (piece.type) {
            'P' -> {
                val dir = if (piece.isWhite) -1 else 1
                val startRow = if (piece.isWhite) 6 else 1
                // Forward 1
                val f1 = (r + dir) * 8 + c
                if (r + dir in 0..7 && board[f1] == null) {
                    moves.add(f1)
                    // Forward 2 from start
                    val f2 = (r + 2 * dir) * 8 + c
                    if (r == startRow && board[f2] == null) {
                        moves.add(f2)
                    }
                }
                // Diagonals for capture
                for (dc in listOf(-1, 1)) {
                    val nc = c + dc
                    val nr = r + dir
                    if (nr in 0..7 && nc in 0..7) {
                        val target = board[nr * 8 + nc]
                        if (target != null && target.isWhite != piece.isWhite) {
                            moves.add(nr * 8 + nc)
                        }
                    }
                }
            }
            'N' -> {
                val offsets = listOf(
                    Pair(-2, -1), Pair(-2, 1), Pair(-1, -2), Pair(-1, 2),
                    Pair(1, -2), Pair(1, 2), Pair(2, -1), Pair(2, 1)
                )
                for ((dr, dc) in offsets) addIfValid(r + dr, c + dc)
            }
            'B' -> {
                for ((dr, dc) in listOf(Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1))) {
                    var nr = r + dr; var nc = c + dc
                    while (addIfValid(nr, nc)) { nr += dr; nc += dc }
                }
            }
            'R' -> {
                for ((dr, dc) in listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))) {
                    var nr = r + dr; var nc = c + dc
                    while (addIfValid(nr, nc)) { nr += dr; nc += dc }
                }
            }
            'Q' -> {
                for ((dr, dc) in listOf(
                    Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1),
                    Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)
                )) {
                    var nr = r + dr; var nc = c + dc
                    while (addIfValid(nr, nc)) { nr += dr; nc += dc }
                }
            }
            'K' -> {
                for (dr in -1..1) {
                    for (dc in -1..1) {
                        if (dr != 0 || dc != 0) addIfValid(r + dr, c + dc)
                    }
                }
            }
        }
        return moves
    }

    // Execute move
    fun makeMove(from: Int, to: Int) {
        val movingPiece = board[from] ?: return
        val target = board[to]

        // Record capture
        if (target != null) {
            if (target.type == 'K') {
                winner = if (movingPiece.isWhite) "Kola" else partnerName
                onGameFinished(winner!!, moveHistory.size + 1)
            }
            if (movingPiece.isWhite) {
                capturedByWhite.add(target.type)
            } else {
                capturedByBlack.add(target.type)
            }
        }

        board[to] = movingPiece
        board[from] = null

        val colNames = "abcdefgh"
        val fromNotation = "${colNames[from % 8]}${8 - from / 8}"
        val toNotation = "${colNames[to % 8]}${8 - to / 8}"
        val notation = "${movingPiece.type}$toNotation"
        moveHistory.add(notation)

        val player = if (movingPiece.isWhite) "Kola" else partnerName
        lastMoveSummary = "$player moved $fromNotation to $toNotation"

        isWhiteTurn = !isWhiteTurn
        selectedSquare = null

        // Joy AI turn if enabled
        if (!isWhiteTurn && autoPlayJoy && winner == null) {
            coroutineScope.launch {
                delay(800)
                // Find all black pieces with legal moves
                val blackPieces = (0..63).filter { board[it]?.isWhite == false }
                val candidates = mutableListOf<Pair<Int, Int>>()
                for (fromSq in blackPieces) {
                    val legals = computeLegalMoves(fromSq)
                    for (toSq in legals) {
                        candidates.add(Pair(fromSq, toSq))
                    }
                }
                if (candidates.isNotEmpty()) {
                    // Prefer captures if available
                    val captureMove = candidates.firstOrNull { board[it.second] != null }
                    val chosen = captureMove ?: candidates.random()
                    makeMove(chosen.first, chosen.second)
                }
            }
        }
    }

    val legalMoves = selectedSquare?.let { computeLegalMoves(it) } ?: emptyList()

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
                                else if (isWhiteTurn) "Kola's turn (White)" else "$partnerName's turn (Black)",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Ink
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isWhiteTurn) Amber else Ink)
                            )
                        }

                        IconButton(onClick = { showMenuSheet = true }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.MoreHoriz, contentDescription = "Menu", tint = Ink)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (winner != null) "Checkmate! Victory for $winner."
                        else "Move ${moveHistory.size + 1} · Tap a piece to view legal squares",
                        fontSize = 12.sp,
                        color = InkMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Players & Captured Pieces Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Kola (White)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    modifier = Modifier.weight(1f),
                    tonalElevation = 3.dp
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.dp, InkMuted, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Kola", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (capturedByWhite.isEmpty()) "No captures" else "Captured: " + capturedByWhite.joinToString(" "),
                            fontSize = 10.sp,
                            color = InkMuted
                        )
                    }
                }

                // Joy (Black)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    modifier = Modifier.weight(1f),
                    tonalElevation = 3.dp
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Ink)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(partnerName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (capturedByBlack.isEmpty()) "No captures" else "Captured: " + capturedByBlack.joinToString(" "),
                            fontSize = 10.sp,
                            color = InkMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. 8x8 Interactive Chess Board
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Cream,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .testTag("chess_game_board")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cell = size.width / 8f
                        for (r in 0..7) {
                            for (c in 0..7) {
                                val isLight = (r + c) % 2 == 0
                                val sq = r * 8 + c
                                val isSelected = selectedSquare == sq
                                val isLegalTarget = legalMoves.contains(sq)

                                val color = when {
                                    isSelected -> Amber.copy(alpha = 0.7f)
                                    isLegalTarget -> Sage.copy(alpha = 0.5f)
                                    isLight -> Color(0xFFF3E4C8)
                                    else -> Color(0xFFB9805A)
                                }

                                drawRect(
                                    color = color,
                                    topLeft = Offset(c * cell, r * cell),
                                    size = androidx.compose.ui.geometry.Size(cell, cell)
                                )

                                // Legal target dot
                                if (isLegalTarget) {
                                    drawCircle(
                                        color = Ink,
                                        radius = cell * 0.15f,
                                        center = Offset((c + 0.5f) * cell, (r + 0.5f) * cell)
                                    )
                                }
                            }
                        }
                    }

                    // Render Piece Symbols and click overlay
                    Column(modifier = Modifier.fillMaxSize()) {
                        for (r in 0..7) {
                            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                                for (c in 0..7) {
                                    val sq = r * 8 + c
                                    val piece = board[sq]

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .clickable {
                                                if (winner != null) return@clickable

                                                if (selectedSquare != null && legalMoves.contains(sq)) {
                                                    // Move selected piece here
                                                    makeMove(selectedSquare!!, sq)
                                                } else if (piece != null && piece.isWhite == isWhiteTurn) {
                                                    // Select this piece
                                                    selectedSquare = sq
                                                } else {
                                                    selectedSquare = null
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (piece != null) {
                                            val glyph = when (piece.type) {
                                                'K' -> if (piece.isWhite) "♔" else "♚"
                                                'Q' -> if (piece.isWhite) "♕" else "♛"
                                                'R' -> if (piece.isWhite) "♖" else "♜"
                                                'B' -> if (piece.isWhite) "♗" else "♝"
                                                'N' -> if (piece.isWhite) "♘" else "♞"
                                                'P' -> if (piece.isWhite) "♙" else "♟"
                                                else -> ""
                                            }
                                            Text(
                                                text = glyph,
                                                fontSize = 28.sp,
                                                color = if (piece.isWhite) Color.White else Ink,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Move history row
            if (moveHistory.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth().height(32.dp)
                ) {
                    items(moveHistory) { move ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CreamDeep,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = move,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Ink,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5. Summary Line
            Text(
                text = lastMoveSummary,
                fontSize = 12.sp,
                color = Cream,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 6. Bottom Controls: React | Pass-and-play toggle
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
                        lastMoveSummary = if (autoPlayJoy) "Joy AI companion active" else "2-Player Pass-and-Play active"
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
            title = { Text("Chess Options", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = {
                        // Undo last move
                        if (moveHistory.isNotEmpty()) {
                            moveHistory.removeLast()
                            isWhiteTurn = !isWhiteTurn
                            lastMoveSummary = "Took back last move."
                        }
                        showMenuSheet = false
                    }) { Text("Take Back Move") }
                    TextButton(onClick = {
                        winner = "Draw"
                        onGameFinished("Draw", moveHistory.size)
                        lastMoveSummary = "Game ended in a friendly draw. 🤝"
                        showMenuSheet = false
                    }) { Text("Offer Draw") }
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
        val emojis = listOf("Brilliant move! 👏", "Checking... 🤔", "Check! ♟️", "Good game! 🤝", "Rematch? 🔥")
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
