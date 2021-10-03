package com.tma.romanova.domain.event

import com.tma.romanova.domain.result.Result

sealed interface PrepareTrackEvent {
    object PrepareTrackError: PrepareTrackEvent
    object PrepareStartWithPlaying: PrepareTrackEvent
    object PrepareStart: PrepareTrackEvent
    object PrepareCompleted: PrepareTrackEvent
}

val Result<Unit>.prepareTrackEvent: PrepareTrackEvent
get() = when(this){
    Result.CacheIsEmpty -> Event.DoNothing
    is Result.LocalException -> PrepareTrackEvent.PrepareTrackError
    Result.NetworkError -> PrepareTrackEvent.PrepareTrackError
    is Result.ServerError -> PrepareTrackEvent.PrepareTrackError
    is Result.Success -> {
        PrepareTrackEvent.PrepareCompleted
    }
}