package com.tma.romanova.domain.event

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.result.Result

sealed interface GetPlaylistEvent {
    data class PlaylistFound(val playlist: Playlist): GetPlaylistEvent
    object PlaylistNotFound : GetPlaylistEvent
}

val Result<Playlist>.getPlaylistEvent
get(): GetPlaylistEvent = when(this){
    is Result.LocalException -> ResponseEvent.Exception(
        cause = cause
    )
    is Result.NetworkError -> ResponseEvent.NetworkUnavailable
    is Result.CacheIsEmpty -> GetPlaylistEvent.PlaylistNotFound
    is Result.ServerError -> ResponseEvent.ServerError(
        code = code,
        message = message
    )
    is Result.Success -> GetPlaylistEvent.PlaylistFound(playlist = this.data)
}