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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BucketItem
import com.example.ui.theme.Amber
import com.example.ui.theme.Cream
import com.example.ui.theme.CreamDeep
import com.example.ui.theme.Ink
import com.example.ui.theme.InkMuted
import com.example.ui.theme.JoyMarigold
import com.example.ui.theme.KolaTeal
import com.example.ui.theme.PlumDeep
import com.example.ui.theme.Rose
import com.example.ui.theme.Sage

@Composable
fun TogetherScreen(
    daysUntilVisit: Int = 23,
    partnerName: String = "Joy",
    eventsList: List<BucketItem> = emptyList(),
    onPlanDate: (title: String, category: String, targetDate: String) -> Unit = { _, _, _ -> },
    onToggleEventCompleted: (BucketItem) -> Unit = {},
    onDeleteEvent: (Int) -> Unit = {},
    onChangeVisitDays: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showPlanDateDialog by remember { mutableStateOf(false) }
    var showChangeVisitDialog by remember { mutableStateOf(false) }
    var currentVisitDays by remember { mutableIntStateOf(daysUntilVisit) }

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
                // Title "Together" (Screen 9)
                Text(
                    text = "Together",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Cream
                )
            }

            // 1. Next Visit Countdown Card (Screen 9)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth().testTag("together_countdown_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$currentVisitDays",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 44.sp,
                                    color = Ink,
                                    lineHeight = 44.sp
                                )
                                Text(
                                    text = "days",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Ink
                                )
                            }

                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text(
                                    text = "until you're in the\nsame room",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Ink,
                                    lineHeight = 22.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Wed, Oct 28 · Your next visit",
                                    fontSize = 12.sp,
                                    color = InkMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Walking couple timeline
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(KolaTeal))
                            Box(
                                modifier = Modifier
                                    .weight(0.7f)
                                    .height(2.dp)
                                    .background(InkMuted.copy(alpha = 0.3f))
                            )
                            Text(text = "👫", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 4.dp))
                            Box(
                                modifier = Modifier
                                    .weight(0.3f)
                                    .height(2.dp)
                                    .background(InkMuted.copy(alpha = 0.3f))
                            )
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(JoyMarigold))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Change date",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Ink,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showChangeVisitDialog = true },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // 2. Shared Calendar Card (October 2026)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth().testTag("together_calendar_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "October 2026",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Ink
                            )
                            Row {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Prev", tint = InkMuted)
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next", tint = InkMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            daysOfWeek.forEach { d ->
                                Text(
                                    text = d,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = InkMuted,
                                    modifier = Modifier.width(36.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val calendarRows = listOf(
                            listOf("", "1", "2", "3", "4", "5", "6"),
                            listOf("7", "8", "9", "10", "11", "12", "13"),
                            listOf("14", "15", "16", "17", "18", "19", "20"),
                            listOf("21", "22", "23", "24", "25", "26", "27"),
                            listOf("28", "29", "30", "31", "", "", "")
                        )

                        calendarRows.forEach { week ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                week.forEach { dayStr ->
                                    val isVisitDay = dayStr == "28"
                                    val hasEvent = dayStr == "9" || dayStr == "18" || eventsList.any { it.targetDate?.contains(dayStr) == true }

                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (isVisitDay) Rose.copy(alpha = 0.35f) else Color.Transparent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = dayStr,
                                                fontSize = 12.sp,
                                                fontWeight = if (isVisitDay) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isVisitDay) Rose else Ink
                                            )
                                            if (hasEvent) {
                                                Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Amber))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Amber))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Date night", fontSize = 11.sp, color = Ink)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Sage))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Game night", fontSize = 11.sp, color = Ink)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Rose))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Visit", fontSize = 11.sp, color = Ink)
                            }
                        }
                    }
                }
            }

            // 3. "Coming up" Section (Screen 9)
            item {
                Text(
                    text = "Coming up",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Cream
                )
            }

            // Mockup Preset 1: Cook-along night
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CreamDeep,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "FRI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = InkMuted)
                                Text(text = "9", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Cook-along night", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "You 8:00 PM (Manchester)", fontSize = 11.sp, color = InkMuted)
                                Text(text = "$partnerName 2:00 PM (Houston)", fontSize = 11.sp, color = InkMuted)
                            }
                        }
                    }
                }
            }

            // Mockup Preset 2: Game night
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CreamDeep,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "SUN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = InkMuted)
                                Text(text = "18", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Game night", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "You 7:00 PM (Manchester)", fontSize = 11.sp, color = InkMuted)
                                Text(text = "$partnerName 1:00 PM (Houston)", fontSize = 11.sp, color = InkMuted)
                            }
                        }
                    }
                }
            }

            // Custom Database Items
            items(eventsList) { item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (item.isCompleted) Sage.copy(alpha = 0.3f) else CreamDeep,
                            modifier = Modifier
                                .size(46.dp)
                                .clickable { onToggleEventCompleted(item) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (item.isCompleted) {
                                    Icon(Icons.Default.Check, contentDescription = "Done", tint = Sage)
                                } else {
                                    Text(text = "DATE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Ink)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Ink,
                                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${item.category} · ${item.targetDate ?: "Coming up"}",
                                fontSize = 11.sp,
                                color = InkMuted
                            )
                        }

                        IconButton(onClick = { onDeleteEvent(item.id) }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = InkMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            item {
                // Primary Button: "Plan a date"
                Button(
                    onClick = { showPlanDateDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("plan_a_date_button"),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Amber)
                ) {
                    Text(
                        text = "Plan a date",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Ink
                    )
                }
                Spacer(modifier = Modifier.height(90.dp)) // padding for tab bar
            }
        }
    }

    // Plan Date Dialog
    if (showPlanDateDialog) {
        var dateTitle by remember { mutableStateOf("") }
        var dateCategory by remember { mutableStateOf("Date Night") }

        AlertDialog(
            onDismissRequest = { showPlanDateDialog = false },
            title = {
                Text(
                    text = "Plan a date with $partnerName",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Set an event to look forward to. Times will convert automatically between your two cities.",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkMuted
                    )
                    OutlinedTextField(
                        value = dateTitle,
                        onValueChange = { dateTitle = it },
                        placeholder = { Text("e.g. Movie night & Ice cream") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    // Category Chips
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Date Night", "Cook-Along", "Game Night").forEach { cat ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (dateCategory == cat) Amber else CreamDeep,
                                modifier = Modifier.clickable { dateCategory = cat }
                            ) {
                                Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dateTitle.isNotBlank()) {
                            onPlanDate(dateTitle, dateCategory, "Fri, Oct 23 · You 8:00 PM | $partnerName 2:00 PM")
                            showPlanDateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    enabled = dateTitle.isNotBlank()
                ) {
                    Text("Add to Calendar", color = Ink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPlanDateDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Change Visit Countdown Dialog
    if (showChangeVisitDialog) {
        var newDaysText by remember { mutableStateOf("$currentVisitDays") }
        AlertDialog(
            onDismissRequest = { showChangeVisitDialog = false },
            title = { Text("Next Reunion Date", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Enter days remaining until your next visit in the same room:", fontSize = 12.sp, color = InkMuted)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newDaysText,
                        onValueChange = { newDaysText = it.filter { ch -> ch.isDigit() } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val d = newDaysText.toIntOrNull() ?: currentVisitDays
                        currentVisitDays = d
                        onChangeVisitDays(d)
                        showChangeVisitDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber)
                ) {
                    Text("Save", color = Ink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangeVisitDialog = false }) { Text("Cancel") }
            }
        )
    }
}
