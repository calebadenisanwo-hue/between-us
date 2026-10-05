package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.Wordmark
import com.example.ui.components.WordmarkSize
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
fun WelcomePairingScreen(
    inviteCode: String = "K7R2M9",
    onProceedToAvatar: () -> Unit,
    onJoinWithCode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showEnterCodeDialog by remember { mutableStateOf(false) }

    // Subtle island bobbing animation
    val islandBob = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        islandBob.animateTo(
            targetValue = 6f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = FastOutSlowInEasing),
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // Wordmark centered at top
            Wordmark(wordmarkSize = WordmarkSize.Large)

            // Night Sky & Two Floating Islands Illustration (Screen 1)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.1f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val bob = islandBob.value

                    // Stars
                    val starPositions = listOf(
                        Offset(w * 0.15f, h * 0.2f),
                        Offset(w * 0.35f, h * 0.15f),
                        Offset(w * 0.5f, h * 0.25f),
                        Offset(w * 0.8f, h * 0.18f),
                        Offset(w * 0.88f, h * 0.35f),
                        Offset(w * 0.12f, h * 0.45f)
                    )
                    for (star in starPositions) {
                        drawCircle(color = Amber.copy(alpha = 0.75f), radius = 2.dp.toPx(), center = star)
                    }

                    // Crescent moon
                    drawCircle(color = Cream.copy(alpha = 0.9f), radius = 12.dp.toPx(), center = Offset(w * 0.6f, h * 0.18f))
                    drawCircle(color = PlumDeep, radius = 10.dp.toPx(), center = Offset(w * 0.63f, h * 0.16f))

                    // Floating Island 1 (Left - Teal Roof House)
                    val island1Center = Offset(w * 0.28f, h * 0.55f + bob)
                    // Floating rock base
                    val rock1 = Path().apply {
                        moveTo(island1Center.x - 70f, island1Center.y)
                        lineTo(island1Center.x + 70f, island1Center.y)
                        lineTo(island1Center.x + 40f, island1Center.y + 60f)
                        lineTo(island1Center.x, island1Center.y + 90f)
                        lineTo(island1Center.x - 40f, island1Center.y + 60f)
                        close()
                    }
                    drawPath(rock1, color = Color(0xFF4A3C52))
                    // Island grass top
                    drawOval(
                        color = Sage,
                        topLeft = Offset(island1Center.x - 80f, island1Center.y - 15f),
                        size = androidx.compose.ui.geometry.Size(160f, 35f)
                    )
                    // Teal house body & roof
                    drawRect(
                        color = CreamDeep,
                        topLeft = Offset(island1Center.x - 30f, island1Center.y - 65f),
                        size = androidx.compose.ui.geometry.Size(60f, 50f)
                    )
                    val roof1 = Path().apply {
                        moveTo(island1Center.x - 40f, island1Center.y - 65f)
                        lineTo(island1Center.x, island1Center.y - 105f)
                        lineTo(island1Center.x + 40f, island1Center.y - 65f)
                        close()
                    }
                    drawPath(roof1, color = KolaTeal)
                    // Warm glowing window
                    drawRect(
                        color = Amber,
                        topLeft = Offset(island1Center.x - 15f, island1Center.y - 45f),
                        size = androidx.compose.ui.geometry.Size(16f, 18f)
                    )

                    // Floating Island 2 (Right - Marigold Roof House)
                    val island2Center = Offset(w * 0.72f, h * 0.52f - bob)
                    // Floating rock base
                    val rock2 = Path().apply {
                        moveTo(island2Center.x - 70f, island2Center.y)
                        lineTo(island2Center.x + 70f, island2Center.y)
                        lineTo(island2Center.x + 40f, island2Center.y + 60f)
                        lineTo(island2Center.x, island2Center.y + 90f)
                        lineTo(island2Center.x - 40f, island2Center.y + 60f)
                        close()
                    }
                    drawPath(rock2, color = Color(0xFF4A3C52))
                    // Island grass top
                    drawOval(
                        color = Sage,
                        topLeft = Offset(island2Center.x - 80f, island2Center.y - 15f),
                        size = androidx.compose.ui.geometry.Size(160f, 35f)
                    )
                    // Marigold house body & roof
                    drawRect(
                        color = CreamDeep,
                        topLeft = Offset(island2Center.x - 30f, island2Center.y - 65f),
                        size = androidx.compose.ui.geometry.Size(60f, 50f)
                    )
                    val roof2 = Path().apply {
                        moveTo(island2Center.x - 40f, island2Center.y - 65f)
                        lineTo(island2Center.x, island2Center.y - 105f)
                        lineTo(island2Center.x + 40f, island2Center.y - 65f)
                        close()
                    }
                    drawPath(roof2, color = JoyMarigold)
                    // Warm glowing window
                    drawRect(
                        color = Amber,
                        topLeft = Offset(island2Center.x - 1f, island2Center.y - 45f),
                        size = androidx.compose.ui.geometry.Size(16f, 18f)
                    )

                    // Dotted Amber Path connecting the islands
                    val archPath = Path().apply {
                        moveTo(island1Center.x + 35f, island1Center.y - 30f)
                        quadraticTo(
                            (island1Center.x + island2Center.x) / 2f,
                            h * 0.35f,
                            island2Center.x - 35f,
                            island2Center.y - 30f
                        )
                    }
                    drawPath(
                        path = archPath,
                        color = Amber.copy(alpha = 0.8f),
                        style = Stroke(
                            width = 3f,
                            cap = StrokeCap.Round,
                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                        )
                    )

                    // Hanging lantern at peak of the arch
                    val lanternPos = Offset((island1Center.x + island2Center.x) / 2f, h * 0.40f)
                    drawCircle(color = Amber.copy(alpha = 0.3f), radius = 16.dp.toPx(), center = lanternPos)
                    drawRect(
                        color = Amber,
                        topLeft = Offset(lanternPos.x - 8f, lanternPos.y - 10f),
                        size = androidx.compose.ui.geometry.Size(16f, 20f)
                    )
                }
            }

            // Cream Bottom Sheet (Screen 1 layout)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Cream,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Build your room together",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Ink,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Share this code with your person.\nWhen they enter it, your room opens.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkMuted,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // 6 Code Tiles
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        inviteCode.forEach { char ->
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Plum)
                                    .clickable {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Between Us Code", inviteCode))
                                        Toast.makeText(context, "Code copied: $inviteCode", Toast.LENGTH_SHORT).show()
                                    }
                                    .testTag("code_tile_$char"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$char",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Amber
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Primary Button: "Copy invite link"
                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Come sit with me in our room: betweenus://join?code=$inviteCode\nCode: $inviteCode"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Between Us Invite"))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("copy_invite_link_button"),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber)
                    ) {
                        Text(
                            text = "Copy invite link",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Ink
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ghost Button: "I have a code instead"
                    TextButton(
                        onClick = { showEnterCodeDialog = true },
                        modifier = Modifier.testTag("have_code_button")
                    ) {
                        Text(
                            text = "I have a code instead",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Ink,
                            textDecoration = TextDecoration.Underline
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Just the two of you. Always.",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3 Page Dots (Step 1 of 3)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Ink))
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(InkMuted.copy(alpha = 0.3f)))
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(InkMuted.copy(alpha = 0.3f)))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Continue step link
                    TextButton(
                        onClick = onProceedToAvatar,
                        modifier = Modifier.testTag("set_up_avatar_link")
                    ) {
                        Text(
                            text = "Set up my avatar →",
                            fontWeight = FontWeight.Bold,
                            color = KolaTeal,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    if (showEnterCodeDialog) {
        var inputCode by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showEnterCodeDialog = false },
            title = {
                Text(
                    text = "Enter Room Code",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter the 6-character code your partner sent you:",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = inputCode,
                        onValueChange = { if (it.length <= 6) inputCode = it.uppercase() },
                        modifier = Modifier.fillMaxWidth().testTag("enter_code_input"),
                        singleLine = true,
                        placeholder = { Text("e.g. K7R2M9") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputCode.length == 6) {
                            onJoinWithCode(inputCode)
                            showEnterCodeDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    enabled = inputCode.length == 6
                ) {
                    Text("Join Room", color = Ink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEnterCodeDialog = false }) { Text("Cancel") }
            }
        )
    }
}
