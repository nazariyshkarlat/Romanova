package com.tma.romanova.presentation.extensions

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchInMain(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit) = launch(Dispatchers.Main, start, block)

fun CoroutineScope.launchImmediately(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit) = launch(Dispatchers.Main.immediate, start, block)

fun CoroutineScope.launchWithCancelInMain(
    job: Job?,
    block: suspend CoroutineScope.() -> Unit
): Job{
    job?.cancel()
    return launchInMain {
        block()
    }
}

fun <T>Flow<T>.whenStarted(
    lifecycleCoroutineScope: LifecycleCoroutineScope){
    lifecycleCoroutineScope.launchWhenStarted {
        this@whenStarted.collect()
    }
}

fun CoroutineScope.launchWithCancel(
    job: Job?,
    coroutineContext: CoroutineContext = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
): Job {
    job?.cancel()
    return launch(coroutineContext) {
        block()
    }
}