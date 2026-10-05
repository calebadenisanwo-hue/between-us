package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.Wordmark
import com.example.ui.components.WordmarkSize
import com.example.ui.theme.Amber
import com.example.ui.theme.Cream
import com.example.ui.theme.CreamDeep
import com.example.ui.theme.DuskBlue
import com.example.ui.theme.Ink
import com.example.ui.theme.InkMuted
import com.example.ui.theme.JoyMarigold
import com.example.ui.theme.KolaTeal
import com.example.ui.theme.Plum
import com.example.ui.theme.PlumDeep
import com.example.ui.theme.Rose
import com.example.ui.theme.Sage
import com.example.ui.theme.Terracotta

@Composable
fun TheRoomScreen(
    daysOfUs: Int = 47,
    distanceMiles: Int = 4740,
    nextVisitDays: Int = 23,
    partnerName: String = "Joy",
    partnerIsAsleep: Boolean = false,
    partnerLeftDoodle: Boolean = true,
    onLightLamp: () -> Unit,
    onSendEmote: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onTapDoodle: () -> Unit,
    onTapRecordPlayer: () -> Unit,
    onTapTogetherCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Lamp glow pulse animation
    val lampGlow = remember { Animatable(1f) }
    var lampLitTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(lampLitTrigger) {
        if (lampLitTrigger) {
            lampGlow.animateTo(1.6f, tween(400, easing = FastOutSlowInEasing))
            lampGlow.animateTo(1.0f, tween(800, easing = FastOutSlowInEasing))
            lampLitTrigger = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlumDeep)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar over the scene (Screen 3)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left cream pill with Wordmark
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp
                ) {
                    Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Wordmark(wordmarkSize = WordmarkSize.Small, color = Ink)
                    }
                }

                // Center soft pill "{N} days of us"
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp
                ) {
                    Text(
                        text = "$daysOfUs days of us",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Ink,
                        modifier = Modifier
                            .clickable { onTapTogetherCard() }
                            .padding(horizontal = 16.dp, vertical = 7.dp)
                            .testTag("days_of_us_pill")
                    )
                }

                // Right round cream gear IconButton (Settings)
                Surface(
                    shape = CircleShape,
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier.size(38.dp)
                ) {
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier.testTag("room_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Ink,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // The Main Room Canvas: Split Window, Clocks, Sofa, Beanbag, Lamp (Screen 3 & 3b)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // 1. Triangular Roof Beams & Ceiling
                    val roofPath = Path().apply {
                        moveTo(0f, h * 0.15f)
                        lineTo(w * 0.5f, 0f)
                        lineTo(w, h * 0.15f)
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(roofPath, color = Color(0xFF382944))

                    // 2. Dual Split Window Panes (Left: Manchester, Right: Houston)
                    val winTop = h * 0.08f
                    val winBottom = h * 0.42f
                    val winMidX = w * 0.5f
                    val winLeft = w * 0.10f
                    val winRight = w * 0.90f

                    // Left Pane (Manchester: Rain/Drizzle)
                    val leftWinPath = Path().apply {
                        moveTo(winLeft, winTop + 40f)
                        lineTo(winMidX - 6f, winTop)
                        lineTo(winMidX - 6f, winBottom)
                        lineTo(winLeft, winBottom)
                        close()
                    }
                    drawPath(
                        path = leftWinPath,
                        brush = Brush.verticalGradient(
                            colors = if (partnerIsAsleep) listOf(Color(0xFF161C2C), Color(0xFF2C394F))
                            else listOf(Color(0xFF233144), Color(0xFF4C5F79)),
                            startY = winTop,
                            endY = winBottom
                        )
                    )
                    // Rain streak particles in left window
                    for (i in 0..18) {
                        val rx = winLeft + (i * 20f) % (winMidX - winLeft - 10f)
                        val ry = winTop + 30f + (i * 18f) % (winBottom - winTop - 40f)
                        drawLine(
                            color = Color(0x77FFFFFF),
                            start = Offset(rx, ry),
                            end = Offset(rx - 8f, ry + 22f),
                            strokeWidth = 1.5f
                        )
                    }

                    // Right Pane (Houston: Sunny Day / Clear Night)
                    val rightWinPath = Path().apply {
                        moveTo(winMidX + 6f, winTop)
                        lineTo(winRight, winTop + 40f)
                        lineTo(winRight, winBottom)
                        lineTo(winMidX + 6f, winBottom)
                        close()
                    }
                    drawPath(
                        path = rightWinPath,
                        brush = Brush.verticalGradient(
                            colors = if (partnerIsAsleep) listOf(Color(0xFF0F1224), Color(0xFF1F2847))
                            else listOf(Color(0xFF74B2E8), Color(0xFFF7DEB0)),
                            startY = winTop,
                            endY = winBottom
                        )
                    )
                    if (!partnerIsAsleep) {
                        // Golden Sun in Houston window
                        drawCircle(color = Amber, radius = 22.dp.toPx(), center = Offset(winRight - 45f, winTop + 70f))
                    } else {
                        // Moon & stars in Houston night window
                        drawCircle(color = Cream, radius = 14.dp.toPx(), center = Offset(winRight - 45f, winTop + 65f))
                    }

                    // Window Frame dividers
                    drawLine(color = Color(0xFF5E4534), start = Offset(winMidX, winTop), end = Offset(winMidX, winBottom), strokeWidth = 8f)
                    drawLine(color = Color(0xFF5E4534), start = Offset(winLeft, winBottom), end = Offset(winRight, winBottom), strokeWidth = 8f)

                    // String fairy lights along triangular roof
                    val lightCount = 12
                    for (i in 0..lightCount) {
                        val lx = (w / lightCount) * i
                        val ly = if (i <= lightCount / 2) (h * 0.15f) - (i * 10f) else (h * 0.15f) - ((lightCount - i) * 10f)
                        drawCircle(color = Amber, radius = 4.dp.toPx(), center = Offset(lx, ly + 15f))
                    }

                    // 3. Wall Decor & Two Brass Clocks
                    val clockLeftCenter = Offset(w * 0.42f, h * 0.47f)
                    val clockRightCenter = Offset(w * 0.58f, h * 0.47f)
                    // Clocks brass rim & face
                    drawCircle(color = Color(0xFFD4AF37), radius = 22.dp.toPx(), center = clockLeftCenter)
                    drawCircle(color = Cream, radius = 19.dp.toPx(), center = clockLeftCenter)
                    // Clock hands (9:20)
                    drawLine(color = Ink, start = clockLeftCenter, end = Offset(clockLeftCenter.x - 10f, clockLeftCenter.y - 2f), strokeWidth = 2.5f)
                    drawLine(color = Ink, start = clockLeftCenter, end = Offset(clockLeftCenter.x + 8f, clockLeftCenter.y + 12f), strokeWidth = 2f)

                    drawCircle(color = Color(0xFFD4AF37), radius = 22.dp.toPx(), center = clockRightCenter)
                    drawCircle(color = Cream, radius = 19.dp.toPx(), center = clockRightCenter)
                    // Clock hands (3:20)
                    drawLine(color = Ink, start = clockRightCenter, end = Offset(clockRightCenter.x + 10f, clockRightCenter.y + 2f), strokeWidth = 2.5f)
                    drawLine(color = Ink, start = clockRightCenter, end = Offset(clockRightCenter.x + 8f, clockRightCenter.y + 12f), strokeWidth = 2f)

                    // Shelf on the left with polaroid, trophy, plant
                    val shelfTop = h * 0.48f
                    drawRoundRect(
                        color = Color(0xFF704A30),
                        topLeft = Offset(w * 0.08f, shelfTop),
                        size = androidx.compose.ui.geometry.Size(120f, 10f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                    )
                    // Polaroid
                    drawRect(color = Cream, topLeft = Offset(w * 0.10f, shelfTop - 35f), size = androidx.compose.ui.geometry.Size(26f, 32f))
                    drawRect(color = DuskBlue, topLeft = Offset(w * 0.12f, shelfTop - 32f), size = androidx.compose.ui.geometry.Size(22f, 20f))
                    // Trophy
                    drawCircle(color = Amber, radius = 8f, center = Offset(w * 0.22f, shelfTop - 18f))
                    // Potted plant on shelf
                    drawRect(color = Terracotta, topLeft = Offset(w * 0.28f, shelfTop - 18f), size = androidx.compose.ui.geometry.Size(16f, 16f))
                    drawCircle(color = Sage, radius = 12f, center = Offset(w * 0.30f, shelfTop - 24f))

                    // 4. Wooden Floor Planks (Isometric Perspective Base)
                    val floorTop = h * 0.58f
                    val floorPath = Path().apply {
                        moveTo(w * 0.5f, floorTop)
                        lineTo(w * 0.95f, floorTop + 90f)
                        lineTo(w * 0.5f, h * 0.96f)
                        lineTo(w * 0.05f, floorTop + 90f)
                        close()
                    }
                    drawPath(floorPath, color = Color(0xFF8B5A3D))

                    // Woven Rug on floor
                    val rugPath = Path().apply {
                        moveTo(w * 0.5f, floorTop + 45f)
                        lineTo(w * 0.82f, floorTop + 105f)
                        lineTo(w * 0.5f, floorTop + 165f)
                        lineTo(w * 0.18f, floorTop + 105f)
                        close()
                    }
                    drawPath(rugPath, color = Color(0xFFE5D5B8))

                    // 5. Living Room Furniture: Mustard Sofa (Left) & Teal Beanbag (Right)
                    // Mustard Sofa on the left
                    val sofaX = w * 0.22f
                    val sofaY = floorTop + 30f
                    // Sofa backrest
                    drawRoundRect(
                        color = Color(0xFFDCA038),
                        topLeft = Offset(sofaX - 60f, sofaY - 20f),
                        size = androidx.compose.ui.geometry.Size(120f, 40f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f)
                    )
                    // Sofa main cushion
                    drawRoundRect(
                        color = JoyMarigold,
                        topLeft = Offset(sofaX - 58f, sofaY + 10f),
                        size = androidx.compose.ui.geometry.Size(116f, 55f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f)
                    )
                    // Wooden sofa legs
                    drawRect(color = Color(0xFF4A3319), topLeft = Offset(sofaX - 52f, sofaY + 62f), size = androidx.compose.ui.geometry.Size(8f, 14f))
                    drawRect(color = Color(0xFF4A3319), topLeft = Offset(sofaX + 44f, sofaY + 62f), size = androidx.compose.ui.geometry.Size(8f, 14f))
                    // Woven throw blanket draped over sofa arm
                    drawRoundRect(
                        color = Cream,
                        topLeft = Offset(sofaX - 60f, sofaY + 8f),
                        size = androidx.compose.ui.geometry.Size(28f, 48f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f)
                    )

                    // Teal Beanbag on the right
                    val beanX = w * 0.74f
                    val beanY = floorTop + 55f
                    // Shadow under beanbag
                    drawOval(
                        color = Color(0x33000000),
                        topLeft = Offset(beanX - 52f, beanY + 45f),
                        size = androidx.compose.ui.geometry.Size(104f, 28f)
                    )
                    // Main beanbag body
                    drawOval(
                        color = KolaTeal,
                        topLeft = Offset(beanX - 50f, beanY - 10f),
                        size = androidx.compose.ui.geometry.Size(100f, 75f)
                    )
                    // Beanbag crease highlight
                    drawArc(
                        color = Color(0x33FFFFFF),
                        startAngle = 30f,
                        sweepAngle = 120f,
                        useCenter = false,
                        topLeft = Offset(beanX - 40f, beanY + 5f),
                        size = androidx.compose.ui.geometry.Size(80f, 45f),
                        style = Stroke(width = 3f)
                    )

                    // 6. Central Table & Warm Glowing Lamp
                    val tableX = w * 0.5f
                    val tableY = floorTop + 65f
                    // Round wood side table with leg shadow
                    drawOval(
                        color = Color(0xFF53351C),
                        topLeft = Offset(tableX - 30f, tableY),
                        size = androidx.compose.ui.geometry.Size(60f, 34f)
                    )
                    drawRect(
                        color = Color(0xFF382312),
                        topLeft = Offset(tableX - 5f, tableY + 20f),
                        size = androidx.compose.ui.geometry.Size(10f, 30f)
                    )
                    // Tabletop rim highlight
                    drawOval(
                        color = Color(0xFF6B4423),
                        topLeft = Offset(tableX - 28f, tableY + 2f),
                        size = androidx.compose.ui.geometry.Size(56f, 30f)
                    )

                    // Lamp base & warm glowing shade
                    val lampCenter = Offset(tableX, tableY - 15f)
                    val glowScale = lampGlow.value
                    // Expanding warm ambient glow ring
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Amber.copy(alpha = 0.65f), Amber.copy(alpha = 0.15f), Color.Transparent),
                            radius = 75.dp.toPx() * glowScale
                        ),
                        radius = 75.dp.toPx() * glowScale,
                        center = lampCenter
                    )
                    // Brass lamp stem
                    drawLine(
                        color = Color(0xFFD4AF37),
                        start = Offset(lampCenter.x, lampCenter.y + 12f),
                        end = Offset(lampCenter.x, lampCenter.y - 10f),
                        strokeWidth = 4f
                    )
                    // Lamp shade (Warm Amber trapezoid)
                    val shadePath = Path().apply {
                        moveTo(lampCenter.x - 14f, lampCenter.y - 28f)
                        lineTo(lampCenter.x + 14f, lampCenter.y - 28f)
                        lineTo(lampCenter.x + 20f, lampCenter.y - 6f)
                        lineTo(lampCenter.x - 20f, lampCenter.y - 6f)
                        close()
                    }
                    drawPath(shadePath, color = Amber)

                    // 7. Characters (Clearly visible, charming, and distinct)
                    // JOY on the Sofa (Left)
                    if (!partnerIsAsleep) {
                        // Joy's body & knit outfit
                        drawRoundRect(
                            color = Color(0xFF556B2F), // Olive knit sweater
                            topLeft = Offset(sofaX - 16f, sofaY - 5f),
                            size = androidx.compose.ui.geometry.Size(32f, 36f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f)
                        )
                        // Rust trousers
                        drawRoundRect(
                            color = Color(0xFFB85D36),
                            topLeft = Offset(sofaX - 12f, sofaY + 26f),
                            size = androidx.compose.ui.geometry.Size(24f, 26f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f)
                        )
                        // Head: Warm brown skin
                        drawCircle(color = Color(0xFF98603A), radius = 20f, center = Offset(sofaX, sofaY - 22f))
                        // Braided high bun
                        drawCircle(color = Color(0xFF151515), radius = 22f, center = Offset(sofaX, sofaY - 36f))
                        drawCircle(color = Color(0xFF151515), radius = 12f, center = Offset(sofaX, sofaY - 52f))
                        // Gold hoop earring
                        drawCircle(color = Color(0xFFD4AF37), radius = 4f, center = Offset(sofaX + 18f, sofaY - 20f), style = Stroke(width = 1.5f))
                        // Cute friendly eyes with sparkle
                        drawCircle(color = Color.White, radius = 4f, center = Offset(sofaX + 7f, sofaY - 22f))
                        drawCircle(color = Ink, radius = 2.5f, center = Offset(sofaX + 8f, sofaY - 22f))
                        // Steaming ceramic mug
                        drawRoundRect(color = Cream, topLeft = Offset(sofaX - 2f, sofaY + 8f), size = androidx.compose.ui.geometry.Size(14f, 16f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f))
                    } else {
                        // Joy asleep under cozy blanket with pillow
                        drawRoundRect(
                            color = Color(0xFFFFF8EE),
                            topLeft = Offset(sofaX - 48f, sofaY - 10f),
                            size = androidx.compose.ui.geometry.Size(28f, 24f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                        )
                        // Sleeping head on pillow
                        drawCircle(color = Color(0xFF98603A), radius = 18f, center = Offset(sofaX - 35f, sofaY - 6f))
                        drawCircle(color = Color(0xFF151515), radius = 18f, center = Offset(sofaX - 44f, sofaY - 10f))
                        // Fleece blanket covering body
                        drawRoundRect(
                            color = CreamDeep,
                            topLeft = Offset(sofaX - 30f, sofaY + 2f),
                            size = androidx.compose.ui.geometry.Size(85f, 48f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f)
                        )
                    }

                    // KOLA in the Beanbag (Right)
                    // Kola's relaxed body in mustard sweater & denim
                    drawRoundRect(
                        color = Color(0xFFE0A526), // Mustard knit
                        topLeft = Offset(beanX - 18f, beanY - 14f),
                        size = androidx.compose.ui.geometry.Size(36f, 38f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
                    )
                    // Indigo jeans
                    drawRoundRect(
                        color = Color(0xFF2B3A67),
                        topLeft = Offset(beanX - 16f, beanY + 20f),
                        size = androidx.compose.ui.geometry.Size(32f, 28f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    // Head: Warm deep brown skin
                    drawCircle(color = Color(0xFF7F4D2E), radius = 20f, center = Offset(beanX, beanY - 32f))
                    // Fade haircut with crisp hairline
                    drawCircle(color = Color(0xFF151515), radius = 20f, center = Offset(beanX, beanY - 40f))
                    // Tortoise round glasses
                    drawCircle(color = Color(0xFF4A2A14), radius = 5.5f, center = Offset(beanX - 6f, beanY - 32f), style = Stroke(width = 1.8f))
                    drawCircle(color = Color(0xFF4A2A14), radius = 5.5f, center = Offset(beanX + 6f, beanY - 32f), style = Stroke(width = 1.8f))
                    drawLine(color = Color(0xFF4A2A14), start = Offset(beanX - 1f, beanY - 32f), end = Offset(beanX + 1f, beanY - 32f), strokeWidth = 1.8f)
                    // Eyes with gleam
                    drawCircle(color = Color.White, radius = 3f, center = Offset(beanX - 6f, beanY - 32f))
                    drawCircle(color = Ink, radius = 2f, center = Offset(beanX - 6f, beanY - 32f))
                    drawCircle(color = Color.White, radius = 3f, center = Offset(beanX + 6f, beanY - 32f))
                    drawCircle(color = Ink, radius = 2f, center = Offset(beanX + 6f, beanY - 32f))
                    // Sleek silver laptop on lap
                    val lapPath = Path().apply {
                        moveTo(beanX - 22f, beanY + 16f)
                        lineTo(beanX + 22f, beanY + 16f)
                        lineTo(beanX + 18f, beanY - 2f)
                        lineTo(beanX - 18f, beanY - 2f)
                        close()
                    }
                    drawPath(lapPath, color = Color(0xFFC5CAD8))
                    // Glowing laptop screen edge
                    drawLine(color = Color(0xAAFFFFFF), start = Offset(beanX - 16f, beanY - 1f), end = Offset(beanX + 16f, beanY - 1f), strokeWidth = 2f)

                    // 8. Record Player on right corner table
                    val recX = w * 0.88f
                    val recY = floorTop + 35f
                    drawRoundRect(
                        color = Color(0xFF6B4423),
                        topLeft = Offset(recX - 28f, recY),
                        size = androidx.compose.ui.geometry.Size(56f, 38f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f)
                    )
                    drawCircle(color = Ink, radius = 14f, center = Offset(recX, recY + 18f))
                    drawCircle(color = Amber, radius = 4f, center = Offset(recX, recY + 18f))
                }

                // Weather & Time Caption Chips under Window (Screen 3)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .align(Alignment.TopCenter)
                        .padding(top = 180.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Manchester chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Cream,
                        tonalElevation = 4.dp
                    ) {
                        Text(
                            text = if (partnerIsAsleep) "Manchester · 6:50 AM · 9°C · drizzle"
                            else "Manchester · 9:20 PM · 11°C · rain",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Ink,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Houston chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Cream,
                        tonalElevation = 4.dp
                    ) {
                        Text(
                            text = if (partnerIsAsleep) "Houston · 12:50 AM · 22°C · clear"
                            else "Houston · 3:20 PM · 29°C · sunny",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Ink,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Vertical Emote Stack on the Right (Screen 3: Hug, High five, Wave, Hold hands)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val emotes = listOf(
                        Triple("Hug", Icons.Default.VolunteerActivism, "emote_hug"),
                        Triple("High Five", Icons.Default.PanTool, "emote_highfive"),
                        Triple("Wave", Icons.Default.WavingHand, "emote_wave"),
                        Triple("Hold Hands", Icons.Default.FrontHand, "emote_holdhands")
                    )

                    emotes.forEach { (name, icon, tag) ->
                        Surface(
                            shape = CircleShape,
                            color = Cream,
                            tonalElevation = 6.dp,
                            modifier = Modifier.size(44.dp)
                        ) {
                            IconButton(
                                onClick = { onSendEmote(name) },
                                modifier = Modifier.testTag(tag)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = name,
                                    tint = Ink,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Partner Asleep Doodle Bubble (Screen 3b)
                if (partnerIsAsleep && partnerLeftDoodle) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Cream,
                        tonalElevation = 6.dp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 90.dp, start = 40.dp)
                            .clickable { onTapDoodle() }
                            .testTag("left_doodle_bubble")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🤍", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "$partnerName left you a doodle.",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Ink
                                )
                                Text(
                                    text = "2h ago",
                                    fontSize = 10.sp,
                                    color = InkMuted
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Area: Distance Card & Amber Lamp FAB (Screen 3 & 3b)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Left: Cream Distance & Next Visit Card
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Cream,
                    tonalElevation = 4.dp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTapTogetherCard() }
                        .testTag("room_distance_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Pin trajectory line with heart
                        Row(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(KolaTeal))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(2.dp)
                                    .background(InkMuted.copy(alpha = 0.3f))
                            )
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Rose,
                                modifier = Modifier.size(14.dp).padding(horizontal = 2.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(2.dp)
                                    .background(InkMuted.copy(alpha = 0.3f))
                            )
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(JoyMarigold))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "$distanceMiles mi apart",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Ink
                        )
                        Text(
                            text = "Next visit in $nextVisitDays days",
                            fontSize = 12.sp,
                            color = InkMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Right: Round Amber FAB "Light the lamp" / "Light her lamp"
                Surface(
                    shape = CircleShape,
                    color = Amber,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .size(80.dp)
                        .clickable {
                            lampLitTrigger = true
                            onLightLamp()
                        }
                        .testTag("light_lamp_button")
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BrightnessLow,
                            contentDescription = "Light the lamp",
                            tint = Ink,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (partnerIsAsleep) "Light\nher lamp" else "Light\nthe lamp",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink,
                            textAlign = TextAlign.Center,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }
    }
}
