package com.tma.romanova.presentation.extensions

import android.app.Activity
import android.view.View
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

data class AppColors(
    val material: Colors,
    val accent: Color
) {
    val primary: Color get() = material.primary
    val primaryVariant: Color get() = material.primaryVariant
    val secondary: Color get() = material.secondary
    val secondaryVariant: Color get() = material.secondaryVariant
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
    val isLight: Boolean get() = material.isLight
}

@Composable
fun Activity.LightStatusBar(){
    with(rememberSystemUiController()) {
        setStatusBarColor(
            color = MaterialTheme.colors.background,
            darkIcons = true
        )
        var flags: Int = window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.setSystemUiVisibility(flags)
    }
}

@Composable
fun Activity.DarkStatusBar(){
    with(rememberSystemUiController()) {
        setStatusBarColor(
            color = MaterialTheme.colors.primary,
            darkIcons = false
        )
        var flags: Int = window.decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        window.decorView.systemUiVisibility = flags
    }
}
