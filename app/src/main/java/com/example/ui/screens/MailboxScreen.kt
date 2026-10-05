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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LoveNote
import com.example.ui.theme.Amber
import com.example.ui.theme.Cream
import com.example.ui.theme.CreamDeep
import com.example.ui.theme.Ink
import com.example.ui.theme.InkMuted
import com.example.ui.theme.KolaTeal
import com.example.ui.theme.PlumDeep
import com.example.ui.theme.Rose
import com.example.ui.theme.Sage
import com.example.ui.theme.Terracotta
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MailboxScreen(
    partnerName: String = "Joy",
    notes: List<LoveNote> = emptyList(),
    onSendNote: (title: String, body: String, kind: String, unlock: String) -> Unit = { _, _, _, _ -> },
    onOpenNote: (Int) -> Unit = {},
    onDeleteNote: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Letters", "Voice notes", "Postcards", "Capsules")
    var selectedTab by remember { mutableStateOf("Letters") }

    var showWriteDialog by remember { mutableStateOf(false) }
    var readingNote by remember { mutableStateOf<LoveNote?>(null) }
    var isVoicePlaying by remember { mutableStateOf(false) }
    var voiceProgress by remember { mutableIntStateOf(0) }

    // Voice playback simulation
    LaunchedEffect(isVoicePlaying) {
        if (isVoicePlaying) {
            while (voiceProgress < 42) {
                delay(1000)
                voiceProgress++
            }
            isVoicePlaying = false
            voiceProgress = 0
        }
    }

    val filteredNotes = notes.filter { note ->
        when (selectedTab) {
            "Letters" -> note.unlockCondition == "Instant" || note.unlockCondition.startsWith("Open")
            "Voice notes" -> note.title.contains("voice", ignoreCase = true) || note.message.contains("voice", ignoreCase = true)
            "Postcards" -> note.title.contains("postcard", ignoreCase = true) || note.colorTag == 1
            "Capsules" -> note.unlockCondition.contains("visit", ignoreCase = true) || note.unlockCondition.contains("Capsule", ignoreCase = true)
            else -> true
        }
    }

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
                    Column {
                        Text(
                            text = "Mailbox",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Cream
                        )
                        Text(
                            text = "${notes.size} shared letters & memories",
                            fontSize = 12.sp,
                            color = CreamDeep.copy(alpha = 0.8f)
                        )
                    }
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

            // Always show the featured mockup postcard if on Postcards or Letters
            if (selectedTab == "Postcards" || selectedTab == "Letters") {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Cream,
                        tonalElevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                readingNote = LoveNote(
                                    id = 9991,
                                    sender = partnerName,
                                    recipient = "Kola",
                                    title = "A postcard from my lunch break",
                                    message = "Sitting out in the plaza with a sandwich watching the birds. Wish you were sitting here with me. Sending you so much warmth and sunny energy across the miles! ☀️",
                                    unlockCondition = "Instant",
                                    isOpened = true
                                )
                            }
                            .testTag("mailbox_card_postcard")
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
            }

            // Always show the featured Voice Note card if on Voice notes or Letters
            if (selectedTab == "Voice notes" || selectedTab == "Letters") {
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
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Amber)
                                        .clickable { isVoicePlaying = !isVoicePlaying },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isVoicePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "Play voice note",
                                        tint = Ink,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))

                                // Audio Waveform Visualization with playhead
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val heights = listOf(8, 14, 22, 10, 18, 26, 12, 20, 16, 24, 14, 18, 8, 14, 22, 10, 16, 20)
                                    heights.forEachIndexed { i, barH ->
                                        val active = isVoicePlaying && (i * 2 <= voiceProgress)
                                        Box(
                                            modifier = Modifier
                                                .width(3.dp)
                                                .height(barH.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(if (active) Amber else InkMuted)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isVoicePlaying) "0:${voiceProgress.toString().padStart(2, '0')}" else "0:42",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Ink
                                )
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
            }

            // Always show the sealed Time Capsule card
            if (selectedTab == "Capsules" || selectedTab == "Letters") {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Cream,
                        tonalElevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                readingNote = LoveNote(
                                    id = 9992,
                                    sender = partnerName,
                                    recipient = "Kola",
                                    title = "Opens Dec 14, our first visit",
                                    message = "🔒 Sealed Time Capsule: 'A surprise letter for our airport reunion day.' Unlocks automatically when you are in the same room on Dec 14.",
                                    unlockCondition = "Opens Dec 14, our first visit",
                                    isOpened = false
                                )
                            }
                            .testTag("mailbox_card_capsule")
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
                                        text = "Time capsule · 23 days left",
                                        fontSize = 12.sp,
                                        color = InkMuted
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
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
                }
            }

            // Real database items list
            items(filteredNotes) { note ->
                val dateStr = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(note.createdAt))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            readingNote = note
                            onOpenNote(note.id)
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = note.title,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Ink
                            )
                            IconButton(
                                onClick = { onDeleteNote(note.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = InkMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = note.message.take(90) + if (note.message.length > 90) "..." else "",
                            fontSize = 13.sp,
                            color = Ink,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "From ${note.sender} · $dateStr",
                                fontSize = 11.sp,
                                color = InkMuted
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (note.isOpened) Sage.copy(alpha = 0.25f) else Amber.copy(alpha = 0.25f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (note.isOpened) "Opened ✓" else note.unlockCondition,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Ink
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(90.dp)) // padding for FAB & Tab bar
            }
        }

        // Amber FAB: "Write something"
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

    // Compose Letter Dialog
    if (showWriteDialog) {
        var letterTitle by remember { mutableStateOf("") }
        var letterBody by remember { mutableStateOf("") }
        var selectedType by remember { mutableStateOf("Letter") } // "Letter", "Voice note", "Postcard", "Capsule"
        var unlockCondition by remember { mutableStateOf("Instant") }

        AlertDialog(
            onDismissRequest = { showWriteDialog = false },
            title = {
                Text(
                    text = "Write to $partnerName",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Type selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Letter", "Postcard", "Capsule").forEach { type ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedType == type) Amber else CreamDeep,
                                modifier = Modifier.clickable {
                                    selectedType = type
                                    unlockCondition = if (type == "Capsule") "Opens on Next Visit" else "Instant"
                                }
                            ) {
                                Text(
                                    text = type,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Ink,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = letterTitle,
                        onValueChange = { letterTitle = it },
                        placeholder = { Text("Title (e.g. Thinking of you today)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = letterBody,
                        onValueChange = { letterBody = it },
                        placeholder = { Text("Write your message...") },
                        modifier = Modifier.fillMaxWidth().height(110.dp),
                        maxLines = 5
                    )

                    Text(
                        text = if (selectedType == "Capsule") "🔒 Sealed: will unlock on your reunion date." else "💌 Delivered instantly to the mailbox.",
                        fontSize = 11.sp,
                        color = InkMuted
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (letterBody.isNotBlank()) {
                            val finalTitle = letterTitle.ifBlank { "Letter from Kola" }
                            onSendNote(finalTitle, letterBody, selectedType, unlockCondition)
                            showWriteDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    enabled = letterBody.isNotBlank()
                ) {
                    Text("Seal & Post 💌", color = Ink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWriteDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Reading Note Modal
    readingNote?.let { note ->
        AlertDialog(
            onDismissRequest = { readingNote = null },
            title = {
                Text(
                    text = note.title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "From ${note.sender}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = KolaTeal
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = note.message,
                        fontSize = 14.sp,
                        color = Ink,
                        lineHeight = 22.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { readingNote = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber)
                ) {
                    Text("Close", color = Ink, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
