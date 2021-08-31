package com.tma.romanova.domain.event

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.result.ErrorCause

sealed interface MainScreenEvent {
    data class PlaylistLoadingSuccess(val playlist: Playlist): MainScreenEvent
    object PlaylistLoadingStart : MainScreenEvent
    data class PlaylistLoadingError(
        val errorCause: ErrorCause
    ) : MainScreenEvent
    object PageOpen : MainScreenEvent
}

val MainScreenEvent.intent
get() = when(this){
    is MainScreenEvent.PlaylistLoadingError -> MainScreenIntent.ShowPageLoadingError(errorCause = errorCause)
    MainScreenEvent.PlaylistLoadingStart -> MainScreenIntent.ShowPageIsLoading
    is MainScreenEvent.PlaylistLoadingSuccess -> MainScreenIntent.ShowPlaylist(playlist = playlist)
    Event.DoNothing -> Intent.DoNothing
    MainScreenEvent.PageOpen -> MainScreenIntent.LoadPlaylist
}

val GetPlaylistEvent.mainScreenEvent: MainScreenEvent
get() = when(this){
    is GetPlaylistEvent.PlaylistFound -> {
        MainScreenEvent.PlaylistLoadingSuccess(playlist = playlist)
    }
    GetPlaylistEvent.PlaylistNotFound -> Event.DoNothing
    ResponseEvent.DoNothing -> Event.DoNothing
    is ResponseEvent.Exception -> MainScreenEvent.PlaylistLoadingError(
        errorCause = this.errorCase
    )
    ResponseEvent.Loading -> MainScreenEvent.PlaylistLoadingStart
    ResponseEvent.NetworkUnavailable -> MainScreenEvent.PlaylistLoadingError(
        errorCause = ResponseEvent.NetworkUnavailable.errorCase
    )
    is ResponseEvent.ServerError -> MainScreenEvent.PlaylistLoadingError(
        errorCause = this.errorCase
    )
}