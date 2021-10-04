package com.tma.romanova.domain.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

fun <T> Flow<T>.onEachAfter(action: suspend (T) -> Unit): Flow<T> = transform { value ->
    val emit = emit(value)
    action(value)
    return@transform emit
}