package com.tma.romanova.domain.event

sealed class Event : MainScreenClientEvent, MainScreenEvent {
    object DoNothing : Event()
}