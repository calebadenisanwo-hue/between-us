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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber
import com.example.ui.theme.Cream
import com.example.ui.theme.CreamDeep
import com.example.ui.theme.DuskBlue
import com.example.ui.theme.Ink
import com.example.ui.theme.InkMuted
import com.example.ui.theme.PlumDeep
import com.example.ui.theme.Rose
import com.example.ui.theme.Sage

@Composable
fun MemoriesShelfScreen(
    partnerName: String = "Joy",
    modifier: Modifier = Modifier
) {
    val filterTabs = listOf("All", "Games", "Answers", "Photos", "Letters")
    var selectedFilter by remember { mutableStateOf("All") }
    var viewMode by remember { mutableStateOf("Shelf") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PlumDeep)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Header: "Memories" + Search + List/Shelf toggle (Screen 10)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Memories",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Cream
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Cream)
                    }

                    // List / Shelf Toggle Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x33FFF4E0),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Row(modifier = Modifier.padding(3.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (viewMode == "List") Amber else Color.Transparent)
                                    .clickable { viewMode = "List" }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("List", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (viewMode == "List") Ink else Cream)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (viewMode == "Shelf") Amber else Color.Transparent)
                                    .clickable { viewMode = "Shelf" }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Shelf", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (viewMode == "Shelf") Ink else Cream)
                            }
                        }
                    }
                }
            }
        }

        item {
            // Category filter chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filterTabs) { tab ->
                    val isSelected = selectedFilter == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Amber else Color(0x33FFF4E0))
                            .clickable { selectedFilter = tab }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("memory_filter_$tab")
                    ) {
                        Text(
                            text = tab,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Ink else Cream
                        )
                    }
                }
            }
        }

        // Section: Pinned (Screen 10)
        item {
            Text(
                text = "Pinned",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Cream
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Chess Night Pinned
                MemoryGridTile(
                    modifier = Modifier.weight(1f),
                    emoji = "♟️",
                    title = "Chess night",
                    subtitle = "Sep 12"
                )

                // 36 Questions Pinned
                MemoryGridTile(
                    modifier = Modifier.weight(1f),
                    emoji = "🃏",
                    title = "36 Questions",
                    subtitle = "Sep 8"
                )
            }
        }

        // Section: September 2026 (Screen 10)
        item {
            Text(
                text = "SEPTEMBER 2026",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CreamDeep.copy(alpha = 0.6f),
                letterSpacing = 1.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Chess game
                MemoryGridTile(
                    modifier = Modifier.weight(1f),
                    emoji = "🎲",
                    title = "Chess night",
                    subtitle = "$partnerName won in 41 moves\n· Sep 12",
                    chipText = "Game",
                    chipColor = Sage
                )

                // Question 24
                MemoryGridTile(
                    modifier = Modifier.weight(1f),
                    emoji = "✉️",
                    title = "Question 24",
                    subtitle = "Sep 8",
                    chipText = "Answer",
                    chipColor = DuskBlue
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Skyline polaroid
                MemoryGridTile(
                    modifier = Modifier.weight(1f),
                    emoji = "📷",
                    title = "Skyline polaroid",
                    subtitle = "Sep 3",
                    chipText = "Photo",
                    chipColor = Rose
                )

                // First letter
                MemoryGridTile(
                    modifier = Modifier.weight(1f),
                    emoji = "💌",
                    title = "First letter",
                    subtitle = "Aug 20",
                    chipText = "Letter",
                    chipColor = Sage
                )
            }
            Spacer(modifier = Modifier.height(90.dp)) // padding for tab bar
        }
    }
}

@Composable
private fun MemoryGridTile(
    emoji: String,
    title: String,
    subtitle: String,
    chipText: String? = null,
    chipColor: Color = Amber,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Cream,
        tonalElevation = 4.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 36.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Ink,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = InkMuted,
                textAlign = TextAlign.Center
            )

            if (chipText != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(chipColor.copy(alpha = 0.25f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = chipText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                }
            }
        }
    }
}
