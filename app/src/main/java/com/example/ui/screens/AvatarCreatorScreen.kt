package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
import com.example.ui.theme.Plum
import com.example.ui.theme.PlumDeep
import com.example.ui.theme.Terracotta
import kotlinx.coroutines.launch

// 12 Skin tone swatches from blueprint specification
val SkinSwatches = listOf(
    Color(0xFFFBE3CF), Color(0xFFF5D0B0), Color(0xFFEAB98F), Color(0xFFD9A073),
    Color(0xFFC68B5E), Color(0xFFB07448), Color(0xFF98603A), Color(0xFF7F4D2E),
    Color(0xFF6A3F26), Color(0xFF55321F), Color(0xFF432818), Color(0xFF33201A)
)

// 8 Hair colors
val HairColorSwatches = listOf(
    Color(0xFF1B1B1B), Color(0xFF3B2A20), Color(0xFF6B4423), Color(0xFFA0642F),
    Color(0xFFC68E3C), Color(0xFF9A9A9A), Color(0xFFE8E0D4), Color(0xFFF28B9B)
)

data class HairStyle(val id: String, val name: String, val iconShape: String)

val HairStyles = listOf(
    HairStyle("box_braids_down", "Box Braids", "braids"),
    HairStyle("locs", "Locs", "locs"),
    HairStyle("twists", "Twists", "twists"),
    HairStyle("coils", "Coils", "coils"),
    HairStyle("afro", "Afro", "afro"),
    HairStyle("fade", "Fade Cut", "fade"),
    HairStyle("headwrap", "Headwrap", "wrap"),
    HairStyle("buzz", "Buzz Cut", "buzz")
)

@Composable
fun AvatarCreatorScreen(
    partnerName: String = "Kola",
    initialSkinIndex: Int = 5,
    initialHairIndex: Int = 0,
    initialHairColorIndex: Int = 0,
    onBack: () -> Unit,
    onSaveAvatar: (skinIdx: Int, hairIdx: Int, hairColorIdx: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryTab by remember { mutableIntStateOf(1) } // 0: Skin, 1: Hair, 2: Face, 3: Outfit, 4: Glasses, 5: Extras
    var skinIndex by remember { mutableIntStateOf(initialSkinIndex) }
    var hairIndex by remember { mutableIntStateOf(initialHairIndex) }
    var hairColorIndex by remember { mutableIntStateOf(initialHairColorIndex) }

    val coroutineScope = rememberCoroutineScope()
    val avatarBounce = remember { Animatable(1f) }

    val categories = listOf("Skin", "Hair", "Face", "Outfit", "Glasses", "Extras")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PlumDeep)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header (Screen 2)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("avatar_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Cream
                    )
                }

                Text(
                    text = "Make yourself",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Cream
                )

                // Pill "2 of 3"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Cream.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "2 of 3",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Cream
                    )
                }
            }

            // Partner status chip: "{Partner} is making his/hers too... •••"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cream,
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Plum)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$partnerName is making theirs too... •••",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Ink
                        )
                    }
                }
            }

            // Center Stage: Spotlight, Woven Rug & Chibi Avatar Figure (Screen 2)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                val skinColor = SkinSwatches[skinIndex]
                val hairColor = HairColorSwatches[hairColorIndex]
                val currentHair = HairStyles[hairIndex]

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(avatarBounce.value)
                ) {
                    val w = size.width
                    val h = size.height

                    // Soft overhead spotlight beam
                    val spotPath = Path().apply {
                        moveTo(w * 0.35f, 0f)
                        lineTo(w * 0.65f, 0f)
                        lineTo(w * 0.85f, h * 0.85f)
                        lineTo(w * 0.15f, h * 0.85f)
                        close()
                    }
                    drawPath(
                        path = spotPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(Cream.copy(alpha = 0.12f), Cream.copy(alpha = 0.02f)),
                            startY = 0f,
                            endY = h * 0.85f
                        )
                    )

                    // Woven Circular Rug (Perspective Ellipse)
                    val rugCenter = Offset(w * 0.5f, h * 0.78f)
                    drawOval(
                        color = Color(0xFFD6C29E),
                        topLeft = Offset(rugCenter.x - 140f, rugCenter.y - 45f),
                        size = androidx.compose.ui.geometry.Size(280f, 90f)
                    )
                    // Rug texture ring
                    drawOval(
                        color = Color(0xFFBAA582),
                        topLeft = Offset(rugCenter.x - 120f, rugCenter.y - 38f),
                        size = androidx.compose.ui.geometry.Size(240f, 76f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
                    )

                    // Chibi Avatar Character Model
                    val charBottom = rugCenter.y - 10f

                    // Soft contact shadow under feet
                    drawOval(
                        color = Color(0x66000000),
                        topLeft = Offset(w * 0.5f - 40f, charBottom - 5f),
                        size = androidx.compose.ui.geometry.Size(80f, 20f)
                    )

                    // Shoes (White rounded sneakers)
                    drawRoundRect(
                        color = Cream,
                        topLeft = Offset(w * 0.5f - 30f, charBottom - 18f),
                        size = androidx.compose.ui.geometry.Size(24f, 18f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    drawRoundRect(
                        color = Cream,
                        topLeft = Offset(w * 0.5f + 6f, charBottom - 18f),
                        size = androidx.compose.ui.geometry.Size(24f, 18f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )

                    // Pants / Trousers (Lilac/Indigo soft tone)
                    val pantsTop = charBottom - 100f
                    drawRoundRect(
                        color = Color(0xFF756F8A),
                        topLeft = Offset(w * 0.5f - 28f, pantsTop),
                        size = androidx.compose.ui.geometry.Size(56f, 85f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f)
                    )
                    // Divider line between legs
                    drawLine(
                        color = PlumDeep,
                        start = Offset(w * 0.5f, pantsTop + 35f),
                        end = Offset(w * 0.5f, pantsTop + 85f),
                        strokeWidth = 3f
                    )

                    // Top / Shirt (Cream / Mustard cozy top)
                    val torsoTop = pantsTop - 70f
                    drawRoundRect(
                        color = Cream,
                        topLeft = Offset(w * 0.5f - 35f, torsoTop),
                        size = androidx.compose.ui.geometry.Size(70f, 75f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f)
                    )

                    // Arms & Hands
                    // Left arm relaxed
                    drawRoundRect(
                        color = skinColor,
                        topLeft = Offset(w * 0.5f - 48f, torsoTop + 10f),
                        size = androidx.compose.ui.geometry.Size(16f, 65f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
                    )
                    // Right arm bent touching hair (playful chibi pose from Screen 2!)
                    val rightArmPath = Path().apply {
                        moveTo(w * 0.5f + 35f, torsoTop + 15f)
                        lineTo(w * 0.5f + 60f, torsoTop + 35f)
                        lineTo(w * 0.5f + 48f, torsoTop - 30f)
                        lineTo(w * 0.5f + 38f, torsoTop - 25f)
                        close()
                    }
                    drawPath(rightArmPath, color = skinColor)

                    // Head & Face
                    val headCenter = Offset(w * 0.5f, torsoTop - 55f)
                    drawCircle(color = skinColor, radius = 45f, center = headCenter)

                    // Eyes (Warm dark expressive eyes with sparkle)
                    drawCircle(color = Ink, radius = 5f, center = Offset(headCenter.x - 14f, headCenter.y - 2f))
                    drawCircle(color = Color.White, radius = 1.8f, center = Offset(headCenter.x - 15f, headCenter.y - 4f))
                    drawCircle(color = Ink, radius = 5f, center = Offset(headCenter.x + 14f, headCenter.y - 2f))
                    drawCircle(color = Color.White, radius = 1.8f, center = Offset(headCenter.x + 13f, headCenter.y - 4f))

                    // Gentle smile
                    val smilePath = Path().apply {
                        moveTo(headCenter.x - 8f, headCenter.y + 14f)
                        quadraticTo(headCenter.x, headCenter.y + 20f, headCenter.x + 8f, headCenter.y + 14f)
                    }
                    drawPath(smilePath, color = Ink, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f, cap = androidx.compose.ui.graphics.StrokeCap.Round))

                    // Hair Rendering based on selection
                    when (currentHair.iconShape) {
                        "afro" -> {
                            drawCircle(color = hairColor, radius = 56f, center = Offset(headCenter.x, headCenter.y - 8f))
                            drawCircle(color = skinColor, radius = 42f, center = headCenter)
                            // Redraw face features above
                            drawCircle(color = Ink, radius = 5f, center = Offset(headCenter.x - 14f, headCenter.y - 2f))
                            drawCircle(color = Ink, radius = 5f, center = Offset(headCenter.x + 14f, headCenter.y - 2f))
                        }
                        "braids", "locs", "twists" -> {
                            // Crown hair
                            drawCircle(color = hairColor, radius = 48f, center = Offset(headCenter.x, headCenter.y - 12f))
                            // Hanging braids on sides
                            for (b in -3..3) {
                                val bx = headCenter.x + (b * 12f)
                                drawRoundRect(
                                    color = hairColor,
                                    topLeft = Offset(bx - 4f, headCenter.y - 30f),
                                    size = androidx.compose.ui.geometry.Size(9f, 85f),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                                )
                            }
                        }
                        "wrap" -> {
                            drawRoundRect(
                                color = Terracotta,
                                topLeft = Offset(headCenter.x - 48f, headCenter.y - 50f),
                                size = androidx.compose.ui.geometry.Size(96f, 50f),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f)
                            )
                        }
                        else -> {
                            // Fade / Buzz
                            drawCircle(color = hairColor, radius = 46f, center = Offset(headCenter.x, headCenter.y - 10f))
                        }
                    }
                }
            }

            // Category Tabs: Skin, Hair, Face, Outfit, Glasses, Extras
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryTab,
                edgePadding = 16.dp,
                containerColor = PlumDeep,
                contentColor = Amber
            ) {
                categories.forEachIndexed { idx, cat ->
                    Tab(
                        selected = selectedCategoryTab == idx,
                        onClick = { selectedCategoryTab = idx },
                        text = {
                            Text(
                                text = cat,
                                fontWeight = if (selectedCategoryTab == idx) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedCategoryTab == idx) Amber else CreamDeep.copy(alpha = 0.8f)
                            )
                        },
                        modifier = Modifier.testTag("avatar_tab_$cat")
                    )
                }
            }

            // Options Selection Panel (Screen 2: Cream Card)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Cream,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Option Tiles Grid (4 columns, 8 hair styles)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(HairStyles) { index, style ->
                            val isSelected = hairIndex == index
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(CreamDeep)
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) Amber else Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { hairIndex = index }
                                    .testTag("hair_tile_$index"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    // Mini hair avatar icon
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(HairColorSwatches[hairColorIndex])
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = style.name.take(6),
                                        fontSize = 10.sp,
                                        color = Ink,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Color Swatches Row (Skin or Hair palette)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (selectedCategoryTab == 0) {
                            itemsIndexed(SkinSwatches) { idx, color ->
                                val isSelected = skinIndex == idx
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) Amber else Color(0x33000000),
                                            shape = CircleShape
                                        )
                                        .clickable { skinIndex = idx }
                                )
                            }
                        } else {
                            itemsIndexed(HairColorSwatches) { idx, color ->
                                val isSelected = hairColorIndex == idx
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) Amber else Color(0x33000000),
                                            shape = CircleShape
                                        )
                                        .clickable { hairColorIndex = idx }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary Button: "Looks like me"
                    Button(
                        onClick = {
                            onSaveAvatar(skinIndex, hairIndex, hairColorIndex)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("looks_like_me_button"),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber)
                    ) {
                        Text(
                            text = "Looks like me",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Ink
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Ghost Button: "Shuffle"
                    TextButton(
                        onClick = {
                            skinIndex = SkinSwatches.indices.random()
                            hairIndex = HairStyles.indices.random()
                            hairColorIndex = HairColorSwatches.indices.random()
                            coroutineScope.launch {
                                avatarBounce.animateTo(1.15f, tween(120, easing = FastOutSlowInEasing))
                                avatarBounce.animateTo(1.0f, tween(180, easing = FastOutSlowInEasing))
                            }
                        },
                        modifier = Modifier.testTag("shuffle_avatar_button")
                    ) {
                        Text(
                            text = "Shuffle",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Ink,
                            textDecoration = TextDecoration.Underline
                        )
                    }

                    // 3 Page Dots (Step 2 of 3)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(InkMuted.copy(alpha = 0.3f)))
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Ink))
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(InkMuted.copy(alpha = 0.3f)))
                    }
                }
            }
        }
    }
}
