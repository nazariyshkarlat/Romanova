package com.tma.romanova.presentation.extensions
import com.tma.romanova.core.application
import com.tma.romanova.domain.result.ErrorCause
import com.tma.romanova.presentation.R
import java.text.SimpleDateFormat
import java.util.*

private val uiDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)


val Int.str: String
    get() = application.getString(this)

fun Int.str(vararg arguments: Any) = application.getString(this, *arguments)

val Date.formattedDate
get() = uiDateFormat.format(this)

val ErrorCause.string: String
get() = when(this){
    is ErrorCause.Exception -> {
        R.string.server_error.str
    }
    ErrorCause.NetworkError -> R.string.network_error.str
    is ErrorCause.ServerError -> {
        if(message.isNullOrBlank()) R.string.server_error.str
        else message!!
    }
}
