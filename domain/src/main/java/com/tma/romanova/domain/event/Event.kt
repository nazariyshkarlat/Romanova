package com.tma.romanova.domain.event

sealed class Event : MainScreenClientEvent, MainScreenEvent, PlayerEvent, PlayerClientEvent {
    object DoNothing : Event()
}