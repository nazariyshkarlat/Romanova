package com.tma.romanova.domain.event

import com.tma.romanova.domain.result.Result

sealed interface PauseTrackEvent {
    object PauseTrackError: PauseTrackEvent
    object TrackPaused: PauseTrackEvent
}

val Result<Unit>.pauseTrackEvent: PauseTrackEvent
    get() = when(this){
        Result.CacheIsEmpty -> Event.DoNothing
        is Result.LocalException -> PauseTrackEvent.PauseTrackError
        Result.NetworkError -> PauseTrackEvent.PauseTrackError
        is Result.ServerError -> PauseTrackEvent.PauseTrackError
        is Result.Success -> PauseTrackEvent.TrackPaused
    }