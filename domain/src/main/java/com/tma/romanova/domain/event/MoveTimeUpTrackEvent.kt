package com.tma.romanova.domain.event

import com.tma.romanova.domain.result.Result

sealed interface MoveTimeUpTrackEvent {
    object MoveTimeUpTrackError: MoveTimeUpTrackEvent
    object MoveTimeUpSuccess: MoveTimeUpTrackEvent
}

val Result<Unit>.moveTimeUpTrackEvent: MoveTimeUpTrackEvent
    get() = when(this){
        Result.CacheIsEmpty -> Event.DoNothing
        is Result.LocalException -> MoveTimeUpTrackEvent.MoveTimeUpTrackError
        Result.NetworkError -> MoveTimeUpTrackEvent.MoveTimeUpTrackError
        is Result.ServerError -> MoveTimeUpTrackEvent.MoveTimeUpTrackError
        is Result.Success -> MoveTimeUpTrackEvent.MoveTimeUpSuccess
    }