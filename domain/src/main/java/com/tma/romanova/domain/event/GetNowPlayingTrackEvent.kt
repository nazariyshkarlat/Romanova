package com.tma.romanova.domain.event

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.Result

sealed interface GetNowPlayingTrackEvent {
    data class NowPlayingTrackFound(val track: Track): GetNowPlayingTrackEvent
    object NowPlayingTrackNotFound : GetNowPlayingTrackEvent
}

val Result<Track>.getNowPlayingTrackEvent
    get(): GetNowPlayingTrackEvent = when(this){
        is Result.LocalException -> ResponseEvent.Exception(
            cause = cause
        )
        is Result.NetworkError -> ResponseEvent.NetworkUnavailable
        is Result.CacheIsEmpty -> GetNowPlayingTrackEvent.NowPlayingTrackNotFound
        is Result.ServerError -> ResponseEvent.ServerError(
            code = code,
            message = message
        )
        is Result.Success -> GetNowPlayingTrackEvent.NowPlayingTrackFound(track = this.data)
    }