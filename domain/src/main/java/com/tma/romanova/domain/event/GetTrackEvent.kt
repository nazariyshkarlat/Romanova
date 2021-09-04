package com.tma.romanova.domain.event

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.Result

sealed interface GetTrackEvent {
    data class TrackFound(val track: Track): GetTrackEvent
    object TrackNotFound : GetTrackEvent
}

val Result<Track>.getTrackEvent
    get(): GetTrackEvent {
        return when(this){
            is Result.LocalException -> ResponseEvent.Exception(
                cause = cause
            )
            is Result.NetworkError -> ResponseEvent.NetworkUnavailable
            is Result.CacheIsEmpty -> GetTrackEvent.TrackNotFound
            is Result.ServerError -> ResponseEvent.ServerError(
                code = code,
                message = message
            )
            is Result.Success -> GetTrackEvent.TrackFound(track = this.data)
        }
    }