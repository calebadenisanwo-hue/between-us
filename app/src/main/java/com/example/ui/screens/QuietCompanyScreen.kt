package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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

@Composable
fun QuietCompanyScreen(
    partnerName: String = "Joy",
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var rainVol by remember { mutableFloatStateOf(0.8f) }
    var fireVol by remember { mutableFloatStateOf(0.3f) }
    var lofiVol by remember { mutableFloatStateOf(0.5f) }
    var cricketsVol by remember { mutableFloatStateOf(0f) }
    var cityVol by remember { mutableFloatStateOf(0f) }

    var syncWithPartner by remember { mutableStateOf(true) }
    var focusTimerOn by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlumDeep)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header (Screen 12)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Cream)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Quiet company",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Cream
                    )
                    Text(
                        text = "You've both been here 38 min",
                        fontSize = 12.sp,
                        color = CreamDeep.copy(alpha = 0.8f)
                    )
                }
            }

            // Visual Area: Kola and Joy Studying Together at Desks (Screen 12)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Rainy Window behind desk
                    val winLeft = w * 0.15f
                    val winRight = w * 0.85f
                    val winTop = h * 0.05f
                    val winBottom = h * 0.60f
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF1E2838), Color(0xFF38465C)),
                            startY = winTop,
                            endY = winBottom
                        ),
                        topLeft = Offset(winLeft, winTop),
                        size = androidx.compose.ui.geometry.Size(winRight - winLeft, winBottom - winTop)
                    )
                    // Rain streaks
                    for (i in 0..14) {
                        val rx = winLeft + (i * 24f)
                        val ry = winTop + 20f + (i * 15f) % (winBottom - winTop - 40f)
                        drawLine(
                            color = Color(0x66FFFFFF),
                            start = Offset(rx, ry),
                            end = Offset(rx - 6f, ry + 20f),
                            strokeWidth = 1.5f
                        )
                    }

                    // Window Frame
                    drawRect(
                        color = Color(0xFF5E4534),
                        topLeft = Offset(winLeft, winTop),
                        size = androidx.compose.ui.geometry.Size(winRight - winLeft, winBottom - winTop),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8f)
                    )
                    drawLine(
                        color = Color(0xFF5E4534),
                        start = Offset(w * 0.5f, winTop),
                        end = Offset(w * 0.5f, winBottom),
                        strokeWidth = 8f
                    )

                    // Cozy Wooden Desk Surface
                    val deskY = h * 0.68f
                    drawRect(
                        color = Color(0xFF8B5A3D),
                        topLeft = Offset(0f, deskY),
                        size = androidx.compose.ui.geometry.Size(w, h - deskY)
                    )

                    // Kola (Left) with Glasses & Laptop under Teal Desk Lamp
                    val kolaDeskX = w * 0.30f
                    // Warm glowing desk lamp (Teal base, glow)
                    drawCircle(color = Amber.copy(alpha = 0.4f), radius = 28.dp.toPx(), center = Offset(kolaDeskX - 60f, deskY - 35f))
                    drawRoundRect(
                        color = KolaTeal,
                        topLeft = Offset(kolaDeskX - 70f, deskY - 45f),
                        size = androidx.compose.ui.geometry.Size(20f, 45f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f)
                    )
                    // Kola avatar (head & shoulders studying)
                    drawCircle(color = Color(0xFF7F4D2E), radius = 32f, center = Offset(kolaDeskX, deskY - 50f))
                    drawCircle(color = Color(0xFF1B1B1B), radius = 34f, center = Offset(kolaDeskX, deskY - 58f))
                    // Mustard knit sweater
                    drawRoundRect(
                        color = JoyMarigold,
                        topLeft = Offset(kolaDeskX - 42f, deskY - 20f),
                        size = androidx.compose.ui.geometry.Size(84f, 60f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f)
                    )
                    // Open laptop
                    drawRoundRect(
                        color = Color(0xFFB5BAC9),
                        topLeft = Offset(kolaDeskX - 25f, deskY - 14f),
                        size = androidx.compose.ui.geometry.Size(50f, 32f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                    )

                    // Joy (Right) with Braids & Notebook under Yellow Desk Lamp
                    val joyDeskX = w * 0.70f
                    // Yellow desk lamp
                    drawCircle(color = Amber.copy(alpha = 0.4f), radius = 28.dp.toPx(), center = Offset(joyDeskX + 60f, deskY - 35f))
                    drawRoundRect(
                        color = Amber,
                        topLeft = Offset(joyDeskX + 50f, deskY - 45f),
                        size = androidx.compose.ui.geometry.Size(20f, 45f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f)
                    )
                    // Joy avatar (head & braids)
                    drawCircle(color = Color(0xFF98603A), radius = 32f, center = Offset(joyDeskX, deskY - 50f))
                    drawCircle(color = Color(0xFF1B1B1B), radius = 38f, center = Offset(joyDeskX, deskY - 65f))
                    // Sage green cardigan
                    drawRoundRect(
                        color = Sage,
                        topLeft = Offset(joyDeskX - 42f, deskY - 20f),
                        size = androidx.compose.ui.geometry.Size(84f, 60f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f)
                    )
                    // Open notebook and highlighter pen
                    drawRoundRect(
                        color = Cream,
                        topLeft = Offset(joyDeskX - 22f, deskY - 10f),
                        size = androidx.compose.ui.geometry.Size(44f, 28f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f)
                    )
                    drawRect(color = Amber, topLeft = Offset(joyDeskX + 10f, deskY - 18f), size = androidx.compose.ui.geometry.Size(6f, 18f))
                }
            }

            // Bottom Soundscape Mixer Panel (Screen 12)
            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Cream,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    // Header: Soundscape + Focus timer switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Soundscape",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Ink
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Focus timer", fontSize = 12.sp, color = InkMuted)
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = focusTimerOn,
                                onCheckedChange = { focusTimerOn = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Amber, checkedTrackColor = CreamDeep)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sound Sliders (Rain, Fireplace, Lo-fi, Crickets, City)
                    SoundSliderRow("🌧️", "Rain", rainVol, { rainVol = it })
                    SoundSliderRow("🔥", "Fireplace", fireVol, { fireVol = it })
                    SoundSliderRow("🎧", "Lo-fi", lofiVol, { lofiVol = it })
                    SoundSliderRow("🦗", "Crickets", cricketsVol, { cricketsVol = it })
                    SoundSliderRow("🏙️", "City", cityVol, { cityVol = it })

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sync with Joy toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sync with $partnerName", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                        Switch(
                            checked = syncWithPartner,
                            onCheckedChange = { syncWithPartner = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Amber, checkedTrackColor = CreamDeep)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons: "Say good luck" | "Take a break together"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier.weight(1f).height(46.dp),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Text("Say good luck", color = Ink, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1.3f).height(46.dp),
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Plum)
                        ) {
                            Text("Take a break together", color = Cream, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SoundSliderRow(
    emoji: String,
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Ink, modifier = Modifier.width(65.dp))

        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(thumbColor = CreamDeep, activeTrackColor = Ink, inactiveTrackColor = CreamDeep)
        )

        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = when {
                value >= 0.7f -> "High"
                value >= 0.4f -> "Medium"
                value > 0.05f -> "Low"
                else -> "Off"
            },
            fontSize = 11.sp,
            color = InkMuted,
            modifier = Modifier.width(45.dp),
            textAlign = TextAlign.End
        )
    }
}
