package com.tma.romanova.presentation.extensions

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.tma.romanova.core.application

val @receiver:androidx.annotation.DrawableRes Int.drawable: Drawable
    get() = AppCompatResources.getDrawable(application, this)!!