package com.tma.romanova.domain.event

import com.tma.romanova.domain.result.Result

sealed interface MoveTimeBackTrackEvent {
    object MoveTimeBackTrackError: MoveTimeBackTrackEvent
    object MoveTimeBackSuccess: MoveTimeBackTrackEvent
}

val Result<Unit>.moveTimeBackTrackEvent: MoveTimeBackTrackEvent
    get() = when(this){
        Result.CacheIsEmpty -> Event.DoNothing
        is Result.LocalException -> MoveTimeBackTrackEvent.MoveTimeBackTrackError
        Result.NetworkError -> MoveTimeBackTrackEvent.MoveTimeBackTrackError
        is Result.ServerError -> MoveTimeBackTrackEvent.MoveTimeBackTrackError
        is Result.Success -> MoveTimeBackTrackEvent.MoveTimeBackSuccess
    }