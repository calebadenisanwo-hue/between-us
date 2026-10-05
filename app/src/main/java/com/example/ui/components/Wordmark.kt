package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber
import com.example.ui.theme.Cream

enum class WordmarkSize {
    Small, Medium, Large
}

@Composable
fun Wordmark(
    modifier: Modifier = Modifier,
    wordmarkSize: WordmarkSize = WordmarkSize.Medium,
    color: Color = Cream
) {
    val fontSize = when (wordmarkSize) {
        WordmarkSize.Small -> 18.sp
        WordmarkSize.Medium -> 24.sp
        WordmarkSize.Large -> 32.sp
    }

    val lampHeight = when (wordmarkSize) {
        WordmarkSize.Small -> 10.dp
        WordmarkSize.Medium -> 14.dp
        WordmarkSize.Large -> 18.dp
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "be",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            color = color,
            letterSpacing = (-0.5).sp
        )

        // The "t" with lamp shade above it
        Box(
            modifier = Modifier.height((fontSize.value * 1.35f).dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Canvas(modifier = Modifier.size(lampHeight * 1.2f, lampHeight).align(Alignment.TopCenter)) {
                val canvasW = size.width
                val canvasH = size.height
                val path = Path().apply {
                    moveTo(canvasW * 0.25f, 0f)
                    lineTo(canvasW * 0.75f, 0f)
                    lineTo(canvasW * 0.95f, canvasH * 0.7f)
                    lineTo(canvasW * 0.05f, canvasH * 0.7f)
                    close()
                }
                drawPath(path, color = Amber)
                drawLine(
                    color = color.copy(alpha = 0.8f),
                    start = Offset(canvasW * 0.5f, canvasH * 0.7f),
                    end = Offset(canvasW * 0.5f, canvasH),
                    strokeWidth = 2f
                )
            }

            Text(
                text = "t",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
                color = color,
                letterSpacing = (-0.5).sp
            )
        }

        Text(
            text = "ween us",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            color = color,
            letterSpacing = (-0.5).sp
        )
    }
}
