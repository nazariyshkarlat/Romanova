package com.tma.romanova.presentation.extensions

import android.content.res.Resources

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Float.sp: Int
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()