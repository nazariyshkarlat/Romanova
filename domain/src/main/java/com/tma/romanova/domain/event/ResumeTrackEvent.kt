package com.tma.romanova.domain.event

import com.tma.romanova.domain.result.Result

sealed interface ResumeTrackEvent {
    object ResumeTrackError: ResumeTrackEvent
    object TrackResumed: ResumeTrackEvent
}

val Result<Unit>.resumeTrackEvent: ResumeTrackEvent
    get() = when(this){
        Result.CacheIsEmpty -> Event.DoNothing
        is Result.LocalException -> ResumeTrackEvent.ResumeTrackError
        Result.NetworkError -> ResumeTrackEvent.ResumeTrackError
        is Result.ServerError -> ResumeTrackEvent.ResumeTrackError
        is Result.Success -> ResumeTrackEvent.TrackResumed
    }