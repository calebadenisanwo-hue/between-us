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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.PlumDeep
import com.example.ui.theme.Sage

data class DecorItem(
    val name: String,
    val emoji: String,
    val status: String,
    val isLocked: Boolean = false,
    val unlockText: String? = null
)

@Composable
fun DecorateScreen(
    partnerName: String = "Joy",
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("Furniture", "Walls", "Floor", "Lighting", "Plants", "Sounds")
    var selectedCategory by remember { mutableStateOf("Furniture") }

    val catalog = listOf(
        DecorItem("Round rug", "🧶", "In your room"),
        DecorItem("Velvet armchair", "🪑", "Place"),
        DecorItem("Floor lamp", "🏮", "Locked", isLocked = true, unlockText = "Unlocks after 3 games"),
        DecorItem("Bookshelf", "📚", "Place"),
        DecorItem("Hanging plant", "🪴", "Place"),
        DecorItem("Record player", "📻", "Place")
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlumDeep)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header: Cancel | Decorate | Done (Screen 11)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onClose) {
                    Text("Cancel", fontSize = 16.sp, color = Cream)
                }

                Text(
                    text = "Decorate",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Cream
                )

                Button(
                    onClick = onClose,
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    modifier = Modifier.height(38.dp).testTag("decorate_done_button")
                ) {
                    Text("Done", color = Ink, fontWeight = FontWeight.Bold)
                }
            }

            // Top action buttons: Undo | Redo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp)) {
                            Icon(Icons.Default.Undo, contentDescription = "Undo", tint = Ink, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Undo", fontSize = 12.sp, color = Ink, fontWeight = FontWeight.SemiBold)
                        }
                        Box(modifier = Modifier.width(1.dp).height(16.dp).background(InkMuted.copy(alpha = 0.3f)))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp)) {
                            Icon(Icons.Default.Redo, contentDescription = "Redo", tint = Ink, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Redo", fontSize = 12.sp, color = Ink, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Isometric Scene with Grid Overlay (Screen 11)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid lines over the floor
                    val gridCenter = Offset(w * 0.5f, h * 0.55f)
                    for (i in -4..4) {
                        drawLine(
                            color = Color(0x33FFFFFF),
                            start = Offset(gridCenter.x + (i * 40f), gridCenter.y - (i * 20f) - 60f),
                            end = Offset(gridCenter.x + (i * 40f) - 120f, gridCenter.y + (i * 20f) + 60f),
                            strokeWidth = 1f
                        )
                        drawLine(
                            color = Color(0x33FFFFFF),
                            start = Offset(gridCenter.x - (i * 40f) - 120f, gridCenter.y - (i * 20f) + 60f),
                            end = Offset(gridCenter.x - (i * 40f), gridCenter.y + (i * 20f) - 60f),
                            strokeWidth = 1f
                        )
                    }

                    // Selected item bounding box with glowing corners (Round Rug selected)
                    val boxCenter = Offset(w * 0.5f, h * 0.58f)
                    val boxW = 160f
                    val boxH = 90f
                    drawOval(
                        color = Color(0x33FFB547),
                        topLeft = Offset(boxCenter.x - boxW / 2, boxCenter.y - boxH / 2),
                        size = androidx.compose.ui.geometry.Size(boxW, boxH)
                    )
                    drawOval(
                        color = Amber,
                        topLeft = Offset(boxCenter.x - boxW / 2, boxCenter.y - boxH / 2),
                        size = androidx.compose.ui.geometry.Size(boxW, boxH),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                // Edit Action Pill over selected item: Rotate | Flip | Remove
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    tonalElevation = 6.dp,
                    modifier = Modifier.align(Alignment.Center).padding(bottom = 90.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.RotateRight, contentDescription = "Rotate", tint = Ink, modifier = Modifier.size(16.dp))
                            Text("Rotate", fontSize = 10.sp, color = Ink, fontWeight = FontWeight.SemiBold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SwapHoriz, contentDescription = "Flip", tint = Ink, modifier = Modifier.size(16.dp))
                            Text("Flip", fontSize = 10.sp, color = Ink, fontWeight = FontWeight.SemiBold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Ink, modifier = Modifier.size(16.dp))
                            Text("Remove", fontSize = 10.sp, color = Ink, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Partner interaction notice bubble: "{Partner} moved the rug. Keep it?" (Screen 11)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    tonalElevation = 6.dp,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$partnerName moved the rug. Keep it?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Ink
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(999.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Amber),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Keep", color = Ink, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            TextButton(onClick = {}, modifier = Modifier.height(32.dp)) {
                                Text("Put it back", color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Bottom Inventory Panel (Screen 11)
            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Cream,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                    // Category filter row
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { cat ->
                            val isSelected = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) Amber else Color.Transparent)
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Ink else InkMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3-Column Items Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(180.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(catalog) { item ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (item.isLocked) CreamDeep.copy(alpha = 0.6f) else CreamDeep,
                                modifier = Modifier.height(115.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = item.emoji, fontSize = 30.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Ink,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))

                                    if (item.isLocked) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Lock, contentDescription = null, tint = InkMuted, modifier = Modifier.size(10.dp))
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = item.unlockText ?: "Locked",
                                                fontSize = 8.sp,
                                                color = InkMuted,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (item.status == "In your room") Sage.copy(alpha = 0.3f) else Amber.copy(alpha = 0.3f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = item.status,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Ink
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
