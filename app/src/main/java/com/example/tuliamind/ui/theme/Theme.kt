package com.example.tuliamind.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CarbonDark = darkColorScheme(
    primary = Carbon.Blue60,
    onPrimary = Color.White,
    secondary = Carbon.Purple60,
    tertiary = Carbon.Teal50,
    background = Carbon.Gray100,
    onBackground = Carbon.Gray10,
    surface = Carbon.Gray90,
    onSurface = Carbon.Gray10,
    error = Carbon.Red60
)

@Composable
fun TuliaMindTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = CarbonDark, typography = TuliaTypography, content = content)
}
