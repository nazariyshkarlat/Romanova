package com.tma.romanova.domain.event

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.result.Result

sealed interface GetPlaylistEvent {
    data class PlaylistFound(val playlist: Playlist): GetPlaylistEvent
    object PlaylistNotFound : GetPlaylistEvent
}

fun Result<Playlist>.toGetPlaylistEvent(): GetPlaylistEvent = when(this){
    is Result.LocalException -> ResponseEvent.Exception
    is Result.NetworkError -> ResponseEvent.NetworkUnavailable
    is Result.CacheIsEmpty -> GetPlaylistEvent.PlaylistNotFound
    is Result.ServerError -> ResponseEvent.ServerError
    is Result.Success -> GetPlaylistEvent.PlaylistFound(playlist = this.data)
}