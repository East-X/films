package com.eastx7.films.theme

import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val thicknessDivider = 1.dp

private val LightThemeColors = lightColorScheme(
    primary = Orange247,
    primaryContainer = Orange255,
    onPrimary = Color.White,
    secondary = Red300,
    secondaryContainer = Red200,
    onSecondary = Color.White,
    error = Red900,
    onBackground = Color.Black,
)

private val DarkThemeColors = darkColorScheme(
    primary = Color.Black,
    primaryContainer = Color.Black,
    onPrimary = Color.Black,
    secondary = Color.Black,
    onSecondary = Color.Black,
    error = Red700,
    onBackground = Color.White
)

@Composable
fun FilmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = FilmTypography,
        shapes = FilmShapes,
        content = content
    )
}

@Composable
fun TopBarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = FilmTypography,
        shapes = FilmShapes,
        content = content
    )
}
