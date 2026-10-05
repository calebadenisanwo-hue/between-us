package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BlueprintColorScheme = darkColorScheme(
    primary = Amber,
    onPrimary = Ink,
    primaryContainer = Amber.copy(alpha = 0.2f),
    onPrimaryContainer = Amber,
    secondary = Terracotta,
    onSecondary = Color.White,
    secondaryContainer = Terracotta.copy(alpha = 0.2f),
    onSecondaryContainer = Terracotta,
    tertiary = Sage,
    onTertiary = Color.White,
    background = PlumDeep,
    onBackground = Cream,
    surface = Plum,
    onSurface = Cream,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = CreamDeep,
    outline = BorderSubtle
)

@Composable
fun BetweenUsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BlueprintColorScheme,
        typography = Typography,
        content = content
    )
}
