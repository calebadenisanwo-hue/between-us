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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BucketItem
import com.example.data.model.LoveNote
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LavenderMist
import com.example.ui.theme.RoseCoral
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SoftRose
import com.example.ui.theme.WarmTerracotta

@Composable
fun LoveJarScreen(
    currentProfile: String,
    notes: List<LoveNote>,
    bucketItems: List<BucketItem>,
    onAddNote: (title: String, message: String, unlockCondition: String, colorTag: Int) -> Unit,
    onOpenNote: (LoveNote) -> Unit,
    onDeleteNote: (Int) -> Unit,
    onAddBucketItem: (title: String, category: String, notes: String?) -> Unit,
    onToggleBucketItem: (BucketItem) -> Unit,
    onDeleteBucketItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) } // 0: Love Notes, 1: Reunion Bucket List
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var showAddBucketDialog by remember { mutableStateOf(false) }
    var viewingNote by remember { mutableStateOf<LoveNote?>(null) }

    val noteColors = listOf(
        Pair(Color(0xFF4A1F2D), RoseCoral),
        Pair(Color(0xFF1E2D4A), Color(0xFF5C9DFF)),
        Pair(Color(0xFF3B2B15), GoldenSun),
        Pair(Color(0xFF1F3529), SageGreen),
        Pair(Color(0xFF321E42), LavenderMist)
    )

    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedSection,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = RoseCoral
        ) {
            Tab(
                selected = selectedSection == 0,
                onClick = { selectedSection = 0 },
                text = { Text("💌 Jar of Love Notes (${notes.size})", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_love_notes")
            )
            Tab(
                selected = selectedSection == 1,
                onClick = { selectedSection = 1 },
                text = { Text("🎯 Reunion Bucket List (${bucketItems.count { it.isCompleted }}/${bucketItems.size})", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_bucket_list")
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            if (selectedSection == 0) {
                // Love Notes Jar
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Love Capsule Jar",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Write sealed messages to open when lonely, celebrating, or needing love.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Button(
                                    onClick = { showAddNoteDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoseCoral),
                                    modifier = Modifier.testTag("add_love_note_btn")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Drop Note")
                                }
                            }
                        }
                    }

                    items(notes) { note ->
                        val themeColor = noteColors[note.colorTag % noteColors.size]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOpenNote(note)
                                    viewingNote = note
                                }
                                .testTag("love_note_${note.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = themeColor.first),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (note.isOpened) Icons.Default.Drafts else Icons.Default.Mail,
                                            contentDescription = null,
                                            tint = themeColor.second,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = note.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.White
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(themeColor.second.copy(alpha = 0.2f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = if (note.isOpened) "Opened" else note.unlockCondition,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = themeColor.second
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                if (note.isOpened) {
                                    Text(
                                        text = note.message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.9f),
                                        maxLines = 3
                                    )
                                } else {
                                    Text(
                                        text = "🔒 Sealed with love. Tap to break the seal and read.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "From ${note.sender} to ${note.recipient} ❤️",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = themeColor.second
                                    )
                                    IconButton(
                                        onClick = { onDeleteNote(note.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Reunion Bucket List
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "When We Hug Again 🫂",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "All the adventures, foods, and dates to experience once together.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Button(
                                    onClick = { showAddBucketDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = WarmTerracotta),
                                    modifier = Modifier.testTag("add_bucket_btn")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add Goal")
                                }
                            }
                        }
                    }

                    items(bucketItems) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleBucketItem(item) }
                                .testTag("bucket_item_${item.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (item.isCompleted) SageGreen.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { onToggleBucketItem(item) }) {
                                    Icon(
                                        imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = "Toggle",
                                        tint = if (item.isCompleted) SageGreen else Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                        ),
                                        color = if (item.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (!item.notes.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = item.notes,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.category,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = RoseCoral
                                        )
                                        Text(
                                            text = " • Added by ${item.addedBy}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                IconButton(onClick = { onDeleteBucketItem(item.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // View Note Dialog
    viewingNote?.let { note ->
        AlertDialog(
            onDismissRequest = { viewingNote = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = RoseCoral)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(note.title, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "From ${note.sender} to ${note.recipient}",
                        style = MaterialTheme.typography.labelSmall,
                        color = RoseCoral,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = note.message,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 22.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewingNote = null }) {
                    Text("Close with Love ❤️")
                }
            }
        )
    }

    // Add Love Note Dialog
    if (showAddNoteDialog) {
        var noteTitle by remember { mutableStateOf("") }
        var noteMessage by remember { mutableStateOf("") }
        var unlockCond by remember { mutableStateOf("Instant") }
        var colorIndex by remember { mutableIntStateOf(0) }

        val conditions = listOf("Instant", "Open when sad", "Open on date night", "Open when missing me")

        AlertDialog(
            onDismissRequest = { showAddNoteDialog = false },
            title = {
                Text("Drop a Note in the Jar 💌", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        label = { Text("Note Title (e.g. For a hard morning)") },
                        modifier = Modifier.fillMaxWidth().testTag("add_note_title_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = noteMessage,
                        onValueChange = { noteMessage = it },
                        label = { Text("Your heartfelt letter...") },
                        modifier = Modifier.fillMaxWidth().testTag("add_note_msg_input"),
                        minLines = 4,
                        maxLines = 8
                    )
                    Text(text = "When should they open this?", style = MaterialTheme.typography.labelSmall)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(conditions) { cond ->
                            FilterChip(
                                selected = unlockCond == cond,
                                onClick = { unlockCond = cond },
                                label = { Text(cond) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteTitle.isNotBlank() && noteMessage.isNotBlank()) {
                            onAddNote(noteTitle, noteMessage, unlockCond, colorIndex)
                            showAddNoteDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseCoral),
                    modifier = Modifier.testTag("save_note_btn")
                ) {
                    Text("Seal & Drop in Jar 🔒")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddNoteDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Bucket Item Dialog
    if (showAddBucketDialog) {
        var goalTitle by remember { mutableStateOf("") }
        var goalCategory by remember { mutableStateOf("Cozy Date") }
        var goalNotes by remember { mutableStateOf("") }

        val goalCategories = listOf("Cozy Date", "Food & Dining", "Travel & Adventure", "Milestone")

        AlertDialog(
            onDismissRequest = { showAddBucketDialog = false },
            title = { Text("Add Reunion Goal 🎯", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = goalTitle,
                        onValueChange = { goalTitle = it },
                        label = { Text("What will we do together?") },
                        modifier = Modifier.fillMaxWidth().testTag("bucket_title_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = goalNotes,
                        onValueChange = { goalNotes = it },
                        label = { Text("Special notes / details (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Text(text = "Category", style = MaterialTheme.typography.labelSmall)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(goalCategories) { cat ->
                            FilterChip(
                                selected = goalCategory == cat,
                                onClick = { goalCategory = cat },
                                label = { Text(cat) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (goalTitle.isNotBlank()) {
                            onAddBucketItem(goalTitle, goalCategory, goalNotes.ifBlank { null })
                            showAddBucketDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmTerracotta),
                    modifier = Modifier.testTag("save_bucket_btn")
                ) {
                    Text("Add to List")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddBucketDialog = false }) { Text("Cancel") }
            }
        )
    }
}
