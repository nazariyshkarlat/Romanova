package com.tma.romanova.domain.event

import com.tma.romanova.domain.result.Result

sealed interface MoveToPositionTrackEvent {
    object MoveToPositionTrackError: MoveToPositionTrackEvent
    data class MoveToPositionTrackSuccess(
        val newPositionMs: Long
    ): MoveToPositionTrackEvent
}

val Result<Long>.moveToPositionTrackEvent: MoveToPositionTrackEvent
    get() = when(this){
        Result.CacheIsEmpty -> Event.NothingHappened
        is Result.LocalException -> MoveToPositionTrackEvent.MoveToPositionTrackError
        Result.NetworkError -> MoveToPositionTrackEvent.MoveToPositionTrackError
        is Result.ServerError -> MoveToPositionTrackEvent.MoveToPositionTrackError
        is Result.Success -> MoveToPositionTrackEvent.MoveToPositionTrackSuccess(
            newPositionMs = data
        )
    }