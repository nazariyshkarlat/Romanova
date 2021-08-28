package com.tma.romanova.domain.event

sealed class Event {
    object DoNothing : Event()
}