package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.ui.theme.Ink
import com.example.ui.theme.InkMuted
import com.example.ui.theme.JoyMarigold
import com.example.ui.theme.KolaTeal
import com.example.ui.theme.Plum
import com.example.ui.theme.PlumDeep
import com.example.ui.theme.Sage

@Composable
fun QuestionsDeckScreen(
    partnerName: String = "Kola",
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var questionIndex by remember { mutableIntStateOf(24) }
    var myAnswerText by remember { mutableStateOf("Joy type childhood ritual") }
    var partnerAnswerLocked by remember { mutableStateOf(true) }
    var myAnswerLocked by remember { mutableStateOf(false) }
    var guessMode by remember { mutableStateOf(false) }

    val questionPrompt = "What's a small ritual from your childhood you'd want us to have?"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlumDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Header Stack (Screen 5)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Cream,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBack, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Ink)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Set II · Getting closer",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Question $questionIndex of 36",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Ink
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Progress Dots Row
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            for (i in 1..24) {
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Amber))
                            }
                            for (i in 25..36) {
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(CreamDeep))
                            }
                        }
                    }

                    // Stack Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CreamDeep,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "36", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Ink)
                            Text(text = "Questions", fontSize = 10.sp, color = InkMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Stacked Card with Prompt (Screen 5)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .height(210.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background stacked layers effect
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = CreamDeep.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth(0.86f).height(190.dp).padding(top = 16.dp)
                ) {}
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = CreamDeep.copy(alpha = 0.8f),
                    modifier = Modifier.fillMaxWidth(0.92f).height(195.dp).padding(top = 8.dp)
                ) {}

                // Active Front Card
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Cream,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth().height(195.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = questionPrompt,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Ink,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. "Your Answer" Card (Screen 5)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Cream,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your answer",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Ink
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = myAnswerText,
                        onValueChange = { myAnswerText = it },
                        modifier = Modifier.fillMaxWidth().testTag("deck_answer_input"),
                        trailingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = InkMuted, modifier = Modifier.size(16.dp))
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Hidden until you both answer",
                        fontSize = 11.sp,
                        color = InkMuted,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. "{Partner}'s Answer" Blurred / Hidden Card (Screen 5)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Cream,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column {
                        Text(
                            text = "$partnerName's answer",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Ink
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Frosted pill with three dots
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CreamDeep),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "•  •  •",
                                fontSize = 18.sp,
                                color = InkMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Reveals when you both lock in",
                            fontSize = 11.sp,
                            color = InkMuted,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Cute mini peeking Kola chibi avatar on the top right
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(KolaTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("K", color = Cream, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Toggle Row: "Guess his answer instead" & "Skip"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = guessMode,
                        onCheckedChange = { guessMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Amber,
                            checkedTrackColor = Cream
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guess their answer instead",
                        fontSize = 13.sp,
                        color = Cream
                    )
                }

                TextButton(
                    onClick = {
                        questionIndex = (questionIndex % 36) + 1
                    }
                ) {
                    Text(
                        text = "Skip",
                        fontSize = 13.sp,
                        color = CreamDeep,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Button: "Lock in my answer"
            Button(
                onClick = {
                    myAnswerLocked = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("lock_in_answer_button"),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Amber)
            ) {
                Text(
                    text = "Lock in my answer",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Ink
                )
            }
        }
    }
}
