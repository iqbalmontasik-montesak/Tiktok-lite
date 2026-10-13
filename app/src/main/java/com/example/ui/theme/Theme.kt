package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TikTokLiteColorScheme = darkColorScheme(
    primary = TikTokRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4A0012),
    onPrimaryContainer = Color(0xFFFFD9DF),
    secondary = TikTokCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF00363A),
    onSecondaryContainer = Color(0xFF80DEEA),
    tertiary = LiteGreen,
    onTertiary = Color.Black,
    background = TikTokBlack,
    onBackground = TikTokTextPrimary,
    surface = TikTokDarkGray,
    onSurface = TikTokTextPrimary,
    surfaceVariant = TikTokSurface,
    onSurfaceVariant = TikTokTextSecondary,
    outline = TikTokBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use intentional TikTok aesthetic
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = TikTokLiteColorScheme,
        typography = Typography,
        content = content
    )
}
