package com.tma.romanova.domain.mvi

typealias EventHandler<Intent, State, ClientEvent> = State.(intent: Intent) -> ClientEvent

inline fun <reified State: Any, reified Intent: Any, reified ClientEvent: Any>
        eventHandler(crossinline block: (State, Intent) -> ClientEvent): EventHandler<Intent, State, ClientEvent> =
    { intent -> block(this, intent) }