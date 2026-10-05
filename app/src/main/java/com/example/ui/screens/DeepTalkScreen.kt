package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionAnswer
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LavenderMist
import com.example.ui.theme.RoseCoral
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SoftRose
import com.example.ui.theme.WarmTerracotta

@Composable
fun DeepTalkScreen(
    questions: List<QuestionAnswer>,
    currentProfile: String,
    onSaveAnswer: (questionId: Int, answer: String) -> Unit,
    onToggleFavorite: (QuestionAnswer) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Favorites", "Romance", "Deep", "Silly", "Future", "Memories")
    var selectedCategory by remember { mutableStateOf("All") }

    var answeringQuestion by remember { mutableStateOf<QuestionAnswer?>(null) }
    var answerInput by remember { mutableStateOf("") }

    val filteredQuestions = questions.filter { q ->
        when (selectedCategory) {
            "All" -> true
            "Favorites" -> q.isFavorite
            else -> q.category.equals(selectedCategory, ignoreCase = true)
        }
    }

    val dailyQuestion = questions.firstOrNull { it.isDaily }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Daily Connection Question
            dailyQuestion?.let { daily ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = RoseCoral.copy(alpha = 0.15f)),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = GoldenSun,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "QUESTION OF THE DAY",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = RoseCoral
                                )
                            }
                            IconButton(onClick = { onToggleFavorite(daily) }) {
                                Icon(
                                    imageVector = if (daily.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = RoseCoral
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = daily.questionText,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Kola's Answer
                        AnswerBubble(
                            partnerName = "Kola 💙",
                            answer = daily.kolaAnswer,
                            isCurrent = currentProfile == "Kola",
                            onTapToAnswer = {
                                answeringQuestion = daily
                                answerInput = daily.kolaAnswer ?: ""
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Joy's Answer
                        AnswerBubble(
                            partnerName = "Joy 🌸",
                            answer = daily.joyAnswer,
                            isCurrent = currentProfile == "Joy",
                            onTapToAnswer = {
                                answeringQuestion = daily
                                answerInput = daily.joyAnswer ?: ""
                            }
                        )
                    }
                }
            }
        }

        item {
            // Category Filter Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) },
                        modifier = Modifier.testTag("filter_chip_$cat")
                    )
                }
            }
        }

        item {
            Text(
                text = "$selectedCategory Deck (${filteredQuestions.size} cards)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(filteredQuestions) { q ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(RoseCoral.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = q.category.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoseCoral
                            )
                        }

                        IconButton(
                            onClick = { onToggleFavorite(q) },
                            modifier = Modifier.size(28.dp).testTag("fav_btn_${q.id}")
                        ) {
                            Icon(
                                imageVector = if (q.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (q.isFavorite) RoseCoral else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = q.questionText,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AnswerBubble(
                        partnerName = "Kola 💙",
                        answer = q.kolaAnswer,
                        isCurrent = currentProfile == "Kola",
                        onTapToAnswer = {
                            answeringQuestion = q
                            answerInput = q.kolaAnswer ?: ""
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AnswerBubble(
                        partnerName = "Joy 🌸",
                        answer = q.joyAnswer,
                        isCurrent = currentProfile == "Joy",
                        onTapToAnswer = {
                            answeringQuestion = q
                            answerInput = q.joyAnswer ?: ""
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Answer Dialog
    if (answeringQuestion != null) {
        AlertDialog(
            onDismissRequest = { answeringQuestion = null },
            title = {
                Text(
                    text = "$currentProfile's Answer",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = answeringQuestion?.questionText ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = answerInput,
                        onValueChange = { answerInput = it },
                        label = { Text("Your sincere answer...") },
                        modifier = Modifier.fillMaxWidth().testTag("answer_input_field"),
                        minLines = 3,
                        maxLines = 6
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        answeringQuestion?.let { q ->
                            onSaveAnswer(q.id, answerInput)
                        }
                        answeringQuestion = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseCoral),
                    modifier = Modifier.testTag("save_answer_confirm_button")
                ) {
                    Text("Save Answer ❤️", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { answeringQuestion = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AnswerBubble(
    partnerName: String,
    answer: String?,
    isCurrent: Boolean,
    onTapToAnswer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (answer.isNullOrBlank()) MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.surface
            )
            .clickable { onTapToAnswer() }
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = partnerName,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (partnerName.contains("Kola")) Color(0xFF5C9DFF) else SoftRose
                )
                if (isCurrent) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = RoseCoral,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (answer.isNullOrBlank()) "Tap to answer" else "Edit",
                            fontSize = 11.sp,
                            color = RoseCoral,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (answer.isNullOrBlank()) "Waiting for ${if (partnerName.contains("Kola")) "Kola" else "Joy"}'s words..."
                else answer,
                style = MaterialTheme.typography.bodySmall,
                color = if (answer.isNullOrBlank()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
