package com.tma.romanova.domain.mvi

typealias Reducer<Intent, State> = State.(intent: Intent) -> State

inline fun <reified State: Any, reified Intent>
        Reducer(crossinline block: (State, Intent) -> State): Reducer<Intent, State> =
    { intent -> block(this, intent) }
