package com.tma.romanova.presentation.extensions

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.component1
import com.tma.romanova.core.application

val @receiver:androidx.annotation.DrawableRes Int.drawable: Drawable
    get() = AppCompatResources.getDrawable(application, this)!!

fun Drawable.withTint(color: androidx.compose.ui.graphics.Color) = (this as VectorDrawable).apply {
    setTint(color.toArgb())
}