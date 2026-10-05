package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.geometry.Offset
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
import com.example.ui.theme.Terracotta

@Composable
fun MailboxScreen(
    partnerName: String = "Joy",
    onSendNote: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Letters", "Voice notes", "Postcards", "Capsules")
    var selectedTab by remember { mutableStateOf("Letters") }
    var showWriteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlumDeep)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Header: 3D Mailbox Art + "Mailbox" (Screen 8)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Wooden Mailbox Illustration
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF6B4E3D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📬", fontSize = 32.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "Mailbox",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = Cream
                    )
                }
            }

            item {
                // Horizontal category filter bar
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        tabs.forEach { tab ->
                            val isSelected = selectedTab == tab
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) Amber else Color.Transparent)
                                    .clickable { selectedTab = tab }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("mailbox_tab_$tab")
                            ) {
                                Text(
                                    text = tab,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Ink else InkMuted
                                )
                            }
                        }
                    }
                }
            }

            // 1. Postcard Card (Screen 8)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth().testTag("mailbox_card_postcard")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "A postcard from\nmy lunch break",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Ink,
                                    lineHeight = 24.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "$partnerName · today",
                                    fontSize = 12.sp,
                                    color = InkMuted
                                )
                            }

                            // Stamp illustration
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CreamDeep,
                                shadowElevation = 2.dp,
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "🏙️", fontSize = 28.sp)
                                }
                            }
                        }
                    }
                }
            }

            // 2. Voice Note Card with Waveform (Screen 8)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth().testTag("mailbox_card_voicenote")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Play circle button
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Amber),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Ink,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))

                            // Audio Waveform Visualization
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val heights = listOf(8, 14, 22, 10, 18, 26, 12, 20, 16, 24, 14, 18, 8, 14, 22, 10, 16, 20)
                                heights.forEach { barH ->
                                    Box(
                                        modifier = Modifier
                                            .width(3.dp)
                                            .height(barH.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(InkMuted)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "0:42", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Say hi to the sun for me",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Ink
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "$partnerName · yesterday", fontSize = 12.sp, color = InkMuted)
                    }
                }
            }

            // 3. Sealed Time Capsule (Screen 8)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth().testTag("mailbox_card_capsule")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Opens Dec 14,\nour first visit",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Ink,
                                    lineHeight = 24.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Time capsule",
                                    fontSize = 12.sp,
                                    color = InkMuted
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Red Wax Seal
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Terracotta),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "✦", color = Cream, fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = InkMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(90.dp)) // padding for FAB & Tab bar
            }
        }

        // Amber FAB: "Write something" (Screen 8)
        FloatingActionButton(
            onClick = { showWriteDialog = true },
            containerColor = Amber,
            contentColor = Ink,
            shape = RoundedCornerShape(999.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 85.dp, end = 20.dp)
                .testTag("mailbox_write_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Write something",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }

    if (showWriteDialog) {
        var letterText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showWriteDialog = false },
            title = {
                Text(
                    text = "Write a letter for $partnerName",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Leave something sweet in the mailbox for when they wake up.",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = letterText,
                        onValueChange = { letterText = it },
                        placeholder = { Text("Dear $partnerName...") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (letterText.isNotBlank()) {
                            onSendNote(letterText)
                            showWriteDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber)
                ) {
                    Text("Seal & Post 💌", color = Ink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWriteDialog = false }) { Text("Cancel") }
            }
        )
    }
}
