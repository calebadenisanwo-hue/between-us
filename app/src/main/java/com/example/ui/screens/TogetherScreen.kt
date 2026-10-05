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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
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

data class DateEvent(
    val title: String,
    val dayOfWeek: String,
    val dayOfMonth: Int,
    val myTime: String,
    val partnerTime: String
)

@Composable
fun TogetherScreen(
    daysUntilVisit: Int = 23,
    partnerName: String = "Joy",
    onPlanDate: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showPlanDateDialog by remember { mutableStateOf(false) }

    val events = remember {
        mutableListOf(
            DateEvent("Cook-along night", "FRI", 9, "You 8:00 PM", "$partnerName 2:00 PM"),
            DateEvent("Game night", "SUN", 18, "You 7:00 PM", "$partnerName 1:00 PM")
        )
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
                                    text = "$daysUntilVisit",
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
                            // Mini walking couple avatars icon
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
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // 2. Shared Calendar Card (Screen 9: October 2026)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth().testTag("together_calendar_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Month Header with Prev/Next
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

                        // Days of week
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

                        // Calendar Grid Weeks
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
                                            // Dots indicator
                                            if (dayStr == "9" || dayStr == "18") {
                                                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Amber))
                                                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Sage))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Legend row: ● Date night  ● Game night  ● Visit
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

            // 3. "Coming Up" Section (Screen 9)
            item {
                Text(
                    text = "Coming up",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Cream
                )
            }

            items(events) { ev ->
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
                        // Date badge
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
                                Text(text = ev.dayOfWeek, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = InkMuted)
                                Text(text = "${ev.dayOfMonth}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = ev.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = ev.myTime, fontSize = 11.sp, color = InkMuted)
                                Text(text = ev.partnerTime, fontSize = 11.sp, color = InkMuted)
                            }
                        }
                    }
                }
            }

            item {
                // Primary Button: "Plan a date" (Screen 9)
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

    if (showPlanDateDialog) {
        var dateTitle by remember { mutableStateOf("") }
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
                Column {
                    Text(
                        text = "Set an event to look forward to (e.g. Movie night, Cooking session).",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = dateTitle,
                        onValueChange = { dateTitle = it },
                        placeholder = { Text("e.g. Afrobeats Dance & Dinner") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dateTitle.isNotBlank()) {
                            onPlanDate(dateTitle)
                            showPlanDateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber)
                ) {
                    Text("Add to Calendar", color = Ink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPlanDateDialog = false }) { Text("Cancel") }
            }
        )
    }
}
