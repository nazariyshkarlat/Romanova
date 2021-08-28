package com.tma.romanova.domain.extensions.date

import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

val String.date: Date
    get() = dateFormat.parse(this)!!