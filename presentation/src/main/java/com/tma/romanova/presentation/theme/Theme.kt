package com.tma.romanova.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.tma.romanova.presentation.extensions.AppColors
import com.tma.romanova.presentation.theme.*

private val LocalColors = staticCompositionLocalOf {
    LightColorPalette
}

private val LightColorPalette = AppColors(
    material = lightColors(
        primary = black,
        secondary = lightGray,
        background = white,
        surface = blueGray,
        onPrimary = white,
        onSecondary = white,
        onBackground = black,
        onSurface = gray
    ),
    accent = blue
)

private val DarkColorPalette = AppColors(
    material = darkColors(
        primary = black,
        secondary = lightGray,
        background = white,
        surface = blueGray,
        onPrimary = white,
        onSecondary = white,
        onBackground = black,
        onSurface = gray
    ),
    accent = blue
)

val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colors = colors.material,
            shapes = shapes,
            content = content
        )
    }
}
