package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber
import com.example.ui.theme.Cream
import com.example.ui.theme.CreamDeep
import com.example.ui.theme.Ink
import com.example.ui.theme.InkMuted
import com.example.ui.theme.PlumDeep
import com.example.ui.theme.Rose
import com.example.ui.theme.Sage

@Composable
fun PlayHubScreen(
    partnerName: String = "Joy",
    partnerIsPresent: Boolean = true,
    onOpenLudo: () -> Unit,
    onOpenChess: () -> Unit,
    onOpenConnectFour: () -> Unit,
    onOpen36Questions: () -> Unit,
    onOpenWouldYouRather: () -> Unit,
    onOpenRoseBudThorn: () -> Unit,
    onOpenGuessMyAnswer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filterTabs = listOf("All", "Quick", "Cozy", "Competitive", "Deep")
    var selectedFilter by remember { mutableStateOf("All") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PlumDeep)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Header: "Play" + "{Partner} is here" pill (Screen 4)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Play",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    color = Cream
                )

                if (partnerIsPresent) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Cream,
                        tonalElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF98603A))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Sage)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$partnerName is here",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        }
                    }
                }
            }
        }

        item {
            // Filter Chips (All, Quick, Cozy, Competitive, Deep)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filterTabs) { tab ->
                    val isSelected = selectedFilter == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Amber else Color(0x33FFF4E0))
                            .clickable { selectedFilter = tab }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("filter_chip_$tab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Ink else Cream
                        )
                    }
                }
            }
        }

        // SECTION 1: Continue (Screen 4)
        item {
            Column {
                Text(
                    text = "SECTION 1",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CreamDeep.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Continue",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ludo Continue Card
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenLudo() }
                        .testTag("continue_ludo_card"),
                    tonalElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎲", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "Ludo", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Amber)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "Your turn", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Ink)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Move 14 · $partnerName leads", fontSize = 11.sp, color = InkMuted)
                    }
                }

                // Chess Continue Card
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenChess() }
                        .testTag("continue_chess_card"),
                    tonalElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "♟️", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "Chess", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CreamDeep)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "$partnerName's turn", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = InkMuted)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Move 22 · 2d ago", fontSize = 11.sp, color = InkMuted)
                    }
                }
            }
        }

        // SECTION 2: Get to know you
        item {
            Column {
                Text(
                    text = "SECTION 2",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CreamDeep.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Get to know you",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 36 Questions Card
                    PlayGameCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🃏",
                        title = "36 Questions",
                        statusText = "Q24 of 36",
                        statusIsChip = true,
                        statusChipColor = Sage,
                        onClick = onOpen36Questions,
                        tag = "game_36_questions"
                    )

                    // Guess My Answer Card
                    PlayGameCard(
                        modifier = Modifier.weight(1f),
                        emoji = "💬",
                        title = "Guess My\nAnswer",
                        statusText = "New",
                        statusIsChip = true,
                        statusChipColor = Sage,
                        onClick = onOpenGuessMyAnswer,
                        tag = "game_guess_my_answer"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Would You Rather Card
                    PlayGameCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🪧",
                        title = "Would You\nRather",
                        statusText = "✔ Played 3 times",
                        statusIsChip = false,
                        onClick = onOpenWouldYouRather,
                        tag = "game_wyr"
                    )

                    // Rose, Bud, Thorn Card
                    PlayGameCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🌹",
                        title = "Rose, Bud,\nThorn",
                        statusText = "Today's check-in",
                        statusIsChip = true,
                        statusChipColor = Amber,
                        onClick = onOpenRoseBudThorn,
                        tag = "game_rose_bud_thorn"
                    )
                }
            }
        }

        // SECTION 3: Board games
        item {
            Column {
                Text(
                    text = "SECTION 3",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CreamDeep.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Board games",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Connect Four
                    PlayGameCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🔴",
                        title = "Connect\nFour",
                        statusText = "Played 5 times · $partnerName leads 3-2",
                        statusIsChip = false,
                        onClick = onOpenConnectFour,
                        tag = "game_connect_four"
                    )

                    // Checkers
                    PlayGameCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🔘",
                        title = "Checkers",
                        statusText = "New",
                        statusIsChip = true,
                        statusChipColor = Sage,
                        onClick = onOpenConnectFour,
                        tag = "game_checkers"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Mancala
                    PlayGameCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🪵",
                        title = "Mancala",
                        statusText = "New",
                        statusIsChip = true,
                        statusChipColor = Sage,
                        onClick = onOpenLudo,
                        tag = "game_mancala"
                    )

                    // Backgammon (Coming soon, 50% opacity)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Cream.copy(alpha = 0.5f),
                        modifier = Modifier.weight(1f),
                        tonalElevation = 2.dp
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "♟️", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Backgammon", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CreamDeep.copy(alpha = 0.8f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = "Coming soon", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = InkMuted)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(90.dp)) // padding for floating tab bar
        }
    }
}

@Composable
private fun PlayGameCard(
    emoji: String,
    title: String,
    statusText: String,
    statusIsChip: Boolean,
    statusChipColor: Color = Amber,
    onClick: () -> Unit,
    tag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Cream,
        tonalElevation = 4.dp,
        modifier = modifier
            .clickable { onClick() }
            .testTag(tag)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Ink,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            if (statusIsChip) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusChipColor.copy(alpha = 0.25f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = statusText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                }
            } else {
                Text(
                    text = statusText,
                    fontSize = 10.sp,
                    color = InkMuted,
                    lineHeight = 13.sp
                )
            }
        }
    }
}
