package com.example.semestralka_vamz.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkerBlueColorScheme = lightColorScheme(
    primary = Color(0xFF1E3A8A),
    onPrimary = Color.White,
    secondary = Color(0xFF274690),
    onSecondary = Color.White,
    background = Color(0xFFEEF2FF),
    error = Color(0xFFB00020),
    surfaceTint = Color(0xFF1E3A8A),
    surface = Color.White,
    onSurface = Color.Black,
    onError = Color.White
)

@Composable
fun SemestralkaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkerBlueColorScheme,
        content = content
    )
}