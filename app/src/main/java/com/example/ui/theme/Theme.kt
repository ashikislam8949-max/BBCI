package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SaudiGreenLight,
    onPrimary = Color.Black,
    primaryContainer = SaudiGreenDark,
    onPrimaryContainer = Color.White,
    secondary = SaudiGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF423308),
    onSecondaryContainer = SaudiGoldContainer,
    background = SurfaceDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceCardDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = SaudiGreen,
    onPrimary = Color.White,
    primaryContainer = SaudiGreenContainer,
    onPrimaryContainer = OnSaudiGreenContainer,
    secondary = SaudiGoldDark,
    onSecondary = Color.White,
    secondaryContainer = SaudiGoldContainer,
    onSecondaryContainer = OnSaudiGoldContainer,
    background = SurfaceLight,
    surface = SurfaceLight,
    surfaceVariant = Color(0xFFF1F5F2),
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

