package com.tma.romanova.domain.event

sealed class Event :
    MainScreenClientEvent,
    MainScreenEvent,
    PlayerEvent,
    PlayerClientEvent,
    PrepareTrackEvent,
    ResumeTrackEvent,
    PauseTrackEvent,
    MoveTimeBackTrackEvent,
    MoveTimeUpTrackEvent,
    MoveToPositionTrackEvent{

    object DoNothing : Event()

}