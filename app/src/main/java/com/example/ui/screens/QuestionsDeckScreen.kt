package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.ui.theme.Rose
import com.example.ui.theme.Sage

// Deck Item Model
data class DeckQuestion(
    val id: String,
    val prompt: String,
    val level: String, // Light, Closer, Deep
    val optionA: String? = null,
    val optionB: String? = null
)

// Prompt 7 Exact 36 Questions Seed
val DECK_CLOSER_36 = listOf(
    DeckQuestion("c1", "What's a meal that instantly takes you back to being a kid?", "Light"),
    DeckQuestion("c2", "What's a tiny thing that always makes your day better?", "Light"),
    DeckQuestion("c3", "Which song would you pick as the soundtrack to this week?", "Light"),
    DeckQuestion("c4", "What's a harmless habit of yours that I might find funny?", "Light"),
    DeckQuestion("c5", "If we had a Sunday with no phones, what would the first hour look like?", "Light"),
    DeckQuestion("c6", "What's the best compliment you've ever received?", "Light"),
    DeckQuestion("c7", "What's a place you could spend hours in without getting bored?", "Light"),
    DeckQuestion("c8", "What was your favorite thing to do on a rainy day when you were small?", "Light"),
    DeckQuestion("c9", "What's something you're weirdly good at?", "Light"),
    DeckQuestion("c10", "What's a smell that feels like home to you?", "Light"),
    DeckQuestion("c11", "If you could instantly learn any skill, what would you pick and why?", "Light"),
    DeckQuestion("c12", "What's a small thing I do that you've secretly noticed and liked?", "Light"),
    DeckQuestion("c13", "What's something you've changed your mind about in the last year?", "Closer"),
    DeckQuestion("c14", "When do you feel most like yourself?", "Closer"),
    DeckQuestion("c15", "What's a risk you're glad you took?", "Closer"),
    DeckQuestion("c16", "What's something you're proud of that you rarely mention?", "Closer"),
    DeckQuestion("c17", "What's a moment from our time so far that you replay in your head?", "Closer"),
    DeckQuestion("c18", "What does a really good day off look like for you, hour by hour?", "Closer"),
    DeckQuestion("c19", "Who taught you the most about how to love people?", "Closer"),
    DeckQuestion("c20", "What do you wish people asked you about more often?", "Closer"),
    DeckQuestion("c21", "What's something that stresses you out that you hide well?", "Closer"),
    DeckQuestion("c22", "What's a tradition you'd want us to start?", "Closer"),
    DeckQuestion("c23", "What's something you're still figuring out about yourself?", "Closer"),
    DeckQuestion("c24", "What's a small ritual from your childhood you'd want us to have?", "Closer"),
    DeckQuestion("c25", "What's a fear you've never said out loud?", "Deep"),
    DeckQuestion("c26", "What do you need more of from me on hard days?", "Deep"),
    DeckQuestion("c27", "When was the last time you felt really lonely, and what helped?", "Deep"),
    DeckQuestion("c28", "What's something you forgave that was hard to forgive?", "Deep"),
    DeckQuestion("c29", "What's a version of your life you sometimes grieve?", "Deep"),
    DeckQuestion("c30", "What does feeling safe with someone mean to you?", "Deep"),
    DeckQuestion("c31", "What's a belief about love you grew up with that you're unlearning?", "Deep"),
    DeckQuestion("c32", "What would you want us to be doing in ten years on an ordinary Tuesday?", "Deep"),
    DeckQuestion("c33", "What part of the distance has surprised you the most?", "Deep"),
    DeckQuestion("c34", "What's something you want to ask me but haven't known how?", "Deep"),
    DeckQuestion("c35", "What would you want me to remember about you if we ever lost touch?", "Deep"),
    DeckQuestion("c36", "What's the first thing you want to say when we're finally in the same room?", "Deep")
)

// Prompt 7 Exact 12 Guess My Answer Seed
val DECK_GUESS_12 = listOf(
    DeckQuestion("g1", "What's your go-to comfort meal?", "Guess"),
    DeckQuestion("g2", "How do you spend the first hour after waking up?", "Guess"),
    DeckQuestion("g3", "What's the one song you can't help singing along to?", "Guess"),
    DeckQuestion("g4", "What do you do when you're stressed?", "Guess"),
    DeckQuestion("g5", "What's your dream holiday?", "Guess"),
    DeckQuestion("g6", "What's your most-used emoji?", "Guess"),
    DeckQuestion("g7", "Which room in a house do you love most?", "Guess"),
    DeckQuestion("g8", "What's your favorite season and why?", "Guess"),
    DeckQuestion("g9", "What's the first thing you do when you get home?", "Guess"),
    DeckQuestion("g10", "What's your guilty-pleasure show?", "Guess"),
    DeckQuestion("g11", "What's the one thing you can't live without?", "Guess"),
    DeckQuestion("g12", "What would you do with a free afternoon and no obligations?", "Guess")
)

// Prompt 7 Exact 12 Would You Rather Seed
val DECK_WYR_12 = listOf(
    DeckQuestion("w1", "Would You Rather...", "Dilemma", "Live somewhere it's always autumn 🍁", "Live somewhere it's always the first warm day of spring 🌸"),
    DeckQuestion("w2", "Would You Rather...", "Dilemma", "A perfect pizza every Friday forever 🍕", "A surprise trip every year ✈️"),
    DeckQuestion("w3", "Would You Rather...", "Dilemma", "Be able to talk to animals 🐾", "Be able to understand every language 🌍"),
    DeckQuestion("w4", "Would You Rather...", "Dilemma", "Never have to cook again 🍳", "Never have to clean again 🧹"),
    DeckQuestion("w5", "Would You Rather...", "Dilemma", "A cozy cabin in the woods 🌲", "A tiny flat above a bakery in a big city 🥐"),
    DeckQuestion("w6", "Would You Rather...", "Dilemma", "Always know what to say 💭", "Always know the right moment to say it ⏱️"),
    DeckQuestion("w7", "Would You Rather...", "Dilemma", "A surprise package in the mail every month 📦", "A surprise video call at a random moment every week 📱"),
    DeckQuestion("w8", "Would You Rather...", "Dilemma", "Fall asleep on a call every night 🌙", "Wake up to a voice note every morning ☀️"),
    DeckQuestion("w9", "Would You Rather...", "Dilemma", "Meet halfway for a weekend every month 🚆", "One long visit every six months 🏖️"),
    DeckQuestion("w10", "Would You Rather...", "Dilemma", "Cook the same recipe together over video 🍝", "Watch the same film at the same moment 🎬"),
    DeckQuestion("w11", "Would You Rather...", "Dilemma", "Write each other letters by hand 💌", "Record each other voice notes 🎙️"),
    DeckQuestion("w12", "Would You Rather...", "Dilemma", "Be great at karaoke 🎤", "Be great at dancing 💃")
)

@Composable
fun QuestionsDeckScreen(
    partnerName: String = "Joy",
    initialDeckId: String = "36questions", // "36questions", "guess", "wyr", "rosebudthorn"
    onBack: () -> Unit,
    onSaveToMemories: (question: String, answer: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var selectedDeckId by remember { mutableStateOf(initialDeckId) }
    var questionIdx by remember { mutableIntStateOf(23) } // 0-indexed, question 24

    // Input States
    var answerText by remember { mutableStateOf("") }
    var guessText by remember { mutableStateOf("") }
    var activeInputTab by remember { mutableIntStateOf(0) } // 0 = Your Answer, 1 = Guess Partner

    // Would You Rather selection
    var wyrSelectedOption by remember { mutableIntStateOf(0) } // 1 = A, 2 = B
    var wyrPartnerOption by remember { mutableIntStateOf(1) }

    // Rose Bud Thorn inputs
    var roseText by remember { mutableStateOf("") }
    var budText by remember { mutableStateOf("") }
    var thornText by remember { mutableStateOf("") }

    // Lock and Reveal States
    var isMyAnswerLocked by remember { mutableStateOf(false) }
    var isPartnerAnswerLocked by remember { mutableStateOf(true) }
    var isStarred by remember { mutableStateOf(false) }

    // Sample partner preset answers for real interactive feel
    val partnerPresetAnswers = listOf(
        "Sunday morning pancakes with highlife music playing on the radio.",
        "A steaming cup of rooibos tea whenever it rains.",
        "Walking to the corner store together in oversized hoodies.",
        "Listening to vinyl records while chopping vegetables for dinner."
    )
    val partnerAnswerText = partnerPresetAnswers[questionIdx % partnerPresetAnswers.size]

    val currentDeck = when (selectedDeckId) {
        "guess" -> DECK_GUESS_12
        "wyr" -> DECK_WYR_12
        else -> DECK_CLOSER_36
    }
    val currentQuestion = currentDeck[questionIdx.coerceIn(0, currentDeck.size - 1)]

    // Reveal condition: Both locked!
    val isRevealed = isMyAnswerLocked && isPartnerAnswerLocked

    // Flip animation on reveal
    val revealScale = remember { Animatable(1f) }
    LaunchedEffect(isRevealed) {
        if (isRevealed) {
            revealScale.animateTo(1.08f, tween(180, easing = FastOutSlowInEasing))
            revealScale.animateTo(1.0f, tween(200, easing = FastOutSlowInEasing))
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
                                text = when (selectedDeckId) {
                                    "guess" -> "Guess My Answer · Set I"
                                    "wyr" -> "Would You Rather · Fun"
                                    "rosebudthorn" -> "Daily Check-in"
                                    else -> "Set II · ${currentQuestion.level}"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (selectedDeckId == "rosebudthorn") "Rose, Bud, Thorn"
                            else "Question ${questionIdx + 1} of ${currentDeck.size}",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Ink
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Progress dots row
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            for (i in 0 until minOf(currentDeck.size, 36)) {
                                val isDone = i <= questionIdx
                                Box(
                                    modifier = Modifier
                                        .size(if (currentDeck.size > 20) 4.dp else 6.dp)
                                        .clip(CircleShape)
                                        .background(if (isDone) Amber else CreamDeep)
                                )
                            }
                        }
                    }

                    // Deck Type Switcher Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CreamDeep,
                        modifier = Modifier
                            .clickable {
                                selectedDeckId = when (selectedDeckId) {
                                    "36questions" -> "guess"
                                    "guess" -> "wyr"
                                    "wyr" -> "rosebudthorn"
                                    else -> "36questions"
                                }
                                questionIdx = 0
                                isMyAnswerLocked = false
                                answerText = ""
                            }
                            .padding(start = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (selectedDeckId) {
                                    "guess" -> "💬 Guess"
                                    "wyr" -> "🪧 WYR"
                                    "rosebudthorn" -> "🌹 Daily"
                                    else -> "🃏 36 Qs"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Ink
                            )
                            Text("Tap to switch", fontSize = 9.sp, color = InkMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // MAIN QUESTION CARD (Screen 5)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Cream,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (selectedDeckId == "wyr") 210.dp else 160.dp)
                    .scale(revealScale.value)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedDeckId == "wyr") {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Would you rather...",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = InkMuted
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            // Option A Button
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (wyrSelectedOption == 1) Amber else CreamDeep,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { wyrSelectedOption = 1 }
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = currentQuestion.optionA ?: "",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Ink,
                                    modifier = Modifier.padding(10.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            // Option B Button
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (wyrSelectedOption == 2) Amber else CreamDeep,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { wyrSelectedOption = 2 }
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = currentQuestion.optionB ?: "",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Ink,
                                    modifier = Modifier.padding(10.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else if (selectedDeckId == "rosebudthorn") {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Today's Evening Check-in",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Ink
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Share your Rose (highlight), Bud (looking forward to), and Thorn (challenge)",
                                fontSize = 12.sp,
                                color = InkMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Text(
                            text = currentQuestion.prompt,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = Ink,
                            textAlign = TextAlign.Center,
                            lineHeight = 26.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // REVEALED STATE: Side-by-side answers
            if (isRevealed) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Both Answers Unlocked! ✨", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Ink)
                            IconButton(onClick = {
                                isStarred = !isStarred
                                onSaveToMemories(currentQuestion.prompt, answerText)
                            }) {
                                Icon(
                                    imageVector = if (isStarred) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = "Star",
                                    tint = if (isStarred) Amber else InkMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Your Answer
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = KolaTeal.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Your Answer (Kola):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = KolaTeal)
                                Text(
                                    text = if (selectedDeckId == "wyr") (if (wyrSelectedOption == 1) currentQuestion.optionA ?: "" else currentQuestion.optionB ?: "")
                                    else if (selectedDeckId == "rosebudthorn") "🌹 $roseText\n🌱 $budText\n🌵 $thornText"
                                    else answerText.ifBlank { "Sunday morning slow pancakes." },
                                    fontSize = 13.sp,
                                    color = Ink,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Partner's Answer
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = JoyMarigold.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("$partnerName's Answer:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = JoyMarigold)
                                Text(
                                    text = if (selectedDeckId == "wyr") (if (wyrPartnerOption == 1) currentQuestion.optionA ?: "" else currentQuestion.optionB ?: "")
                                    else if (selectedDeckId == "rosebudthorn") "🌹 Morning coffee on balcony\n🌱 Our video call tonight\n🌵 Long spreadsheet at work"
                                    else partnerAnswerText,
                                    fontSize = 13.sp,
                                    color = Ink,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                isMyAnswerLocked = false
                                answerText = ""
                                guessText = ""
                                wyrSelectedOption = 0
                                questionIdx = (questionIdx + 1) % currentDeck.size
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Amber)
                        ) {
                            Text("Next Question →", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                        }
                    }
                }
            } else {
                // UNREVEALED / ANSWERING STATE:
                if (selectedDeckId == "rosebudthorn") {
                    // 3 Fields for Rose, Bud, Thorn
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Cream,
                        tonalElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            OutlinedTextField(
                                value = roseText,
                                onValueChange = { roseText = it },
                                placeholder = { Text("🌹 Rose (something good today)...") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = budText,
                                onValueChange = { budText = it },
                                placeholder = { Text("🌱 Bud (looking forward to)...") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = thornText,
                                onValueChange = { thornText = it },
                                placeholder = { Text("🌵 Thorn (something challenging)...") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                    }
                } else if (selectedDeckId != "wyr") {
                    // Standard Answer & Guess Tabs
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Cream,
                        tonalElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            TabRow(
                                selectedTabIndex = activeInputTab,
                                containerColor = CreamDeep,
                                contentColor = Ink,
                                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                            ) {
                                Tab(
                                    selected = activeInputTab == 0,
                                    onClick = { activeInputTab = 0 },
                                    text = { Text("Your answer", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                                )
                                Tab(
                                    selected = activeInputTab == 1,
                                    onClick = { activeInputTab = 1 },
                                    text = { Text("Guess $partnerName's", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (activeInputTab == 0) {
                                OutlinedTextField(
                                    value = answerText,
                                    onValueChange = { answerText = it },
                                    placeholder = { Text("Write your honest answer...") },
                                    modifier = Modifier.fillMaxWidth().height(80.dp),
                                    maxLines = 3
                                )
                            } else {
                                OutlinedTextField(
                                    value = guessText,
                                    onValueChange = { guessText = it },
                                    placeholder = { Text("What do you think $partnerName will say? 🤔") },
                                    modifier = Modifier.fillMaxWidth().height(80.dp),
                                    maxLines = 3
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🔒 Hidden until both lock in",
                                fontSize = 11.sp,
                                color = InkMuted,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Partner's Locked Status Strip
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Cream,
                        tonalElevation = 3.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(JoyMarigold))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("$partnerName's answer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Sage.copy(alpha = 0.25f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text("Answered ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Sage)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Primary Lock Button
                Button(
                    onClick = {
                        isMyAnswerLocked = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("lock_in_answer_button"),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    enabled = if (selectedDeckId == "wyr") wyrSelectedOption > 0
                    else if (selectedDeckId == "rosebudthorn") roseText.isNotBlank() || budText.isNotBlank()
                    else answerText.isNotBlank()
                ) {
                    Text(
                        text = "Lock in my answer",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Ink
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                TextButton(
                    onClick = {
                        questionIdx = (questionIdx + 1) % currentDeck.size
                        answerText = ""
                        guessText = ""
                        isMyAnswerLocked = false
                    }
                ) {
                    Text("Skip this question", color = CreamDeep, fontSize = 13.sp)
                }
            }
        }
    }
}
