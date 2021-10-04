package com.tma.romanova.domain.event

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.track_stream.PlayingEvent
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.result.ErrorCause
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState

sealed interface MainScreenEvent {
    data class PlaylistLoadingSuccess(val playlist: Playlist): MainScreenEvent
    object PlaylistLoadingStart : MainScreenEvent
    data class PlaylistLoadingError(
        val errorCause: ErrorCause
    ) : MainScreenEvent
    object TrackPaused: MainScreenEvent
    object TrackResumed: MainScreenEvent
    object TrackPauseError: MainScreenEvent
    object TrackResumeError: MainScreenEvent
    object NowPlayingTrackNotFound: MainScreenEvent
    data class NowPlayingTrackReceived(val track: Track): MainScreenEvent
    object PageOpen : MainScreenEvent
    object NowPlayingTrackChanged: MainScreenEvent
    object PrepareTrackStart: MainScreenEvent
    object PrepareTrackStartWithPlaying: MainScreenEvent
    object PrepareTrackError: MainScreenEvent
    object PrepareTrackSuccess: MainScreenEvent
}

fun MainScreenEvent.toIntent(state: MainScreenState) = when(this){
    is MainScreenEvent.NowPlayingTrackChanged -> MainScreenIntent.GetNowPlayingTrack
    is MainScreenEvent.PlaylistLoadingError -> MainScreenIntent.ShowPageLoadingError(errorCause = errorCause)
    MainScreenEvent.PlaylistLoadingStart -> MainScreenIntent.ShowPageIsLoading
    is MainScreenEvent.PlaylistLoadingSuccess -> MainScreenIntent.ShowPlaylist(playlist = playlist)
    Event.NothingHappened -> Intent.DoNothing
    MainScreenEvent.PageOpen -> MainScreenIntent.LoadData
    MainScreenEvent.NowPlayingTrackNotFound -> Intent.DoNothing
    is MainScreenEvent.NowPlayingTrackReceived -> MainScreenIntent.UpdateNowPlayingTrack(track = track)
    else -> Intent.DoNothing
}

val GetPlaylistEvent.mainScreenEvent: MainScreenEvent
get() = when(this){
    is GetPlaylistEvent.PlaylistFound -> {
        MainScreenEvent.PlaylistLoadingSuccess(playlist = playlist)
    }
    GetPlaylistEvent.PlaylistNotFound -> Event.NothingHappened
    ResponseEvent.DoNothing -> Event.NothingHappened
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

val PauseTrackEvent.mainScreenEvent: MainScreenEvent
    get() = when(this){
        Event.NothingHappened -> Event.NothingHappened
        PauseTrackEvent.TrackPaused -> MainScreenEvent.TrackPaused
        PauseTrackEvent.PauseTrackError -> MainScreenEvent.TrackPauseError
    }

val PrepareTrackEvent.mainScreenEvent: MainScreenEvent
    get() = when(this){
        Event.NothingHappened -> Event.NothingHappened
        PrepareTrackEvent.PrepareCompleted -> MainScreenEvent.PrepareTrackSuccess
        PrepareTrackEvent.PrepareStart -> MainScreenEvent.PrepareTrackStart
        PrepareTrackEvent.PrepareStartWithPlaying -> MainScreenEvent.PrepareTrackStartWithPlaying
        PrepareTrackEvent.PrepareTrackError -> MainScreenEvent.PrepareTrackError
    }


val ResumeTrackEvent.mainScreenEvent: MainScreenEvent
    get() = when(this){
        Event.NothingHappened -> Event.NothingHappened
        ResumeTrackEvent.TrackResumed -> MainScreenEvent.TrackResumed
        ResumeTrackEvent.ResumeTrackError -> MainScreenEvent.TrackResumeError
    }


val GetNowPlayingTrackEvent.mainScreenEvent: MainScreenEvent
    get() = when(this){
        is GetNowPlayingTrackEvent.NowPlayingTrackNotFound -> {
            MainScreenEvent.NowPlayingTrackNotFound
        }
        is GetNowPlayingTrackEvent.NowPlayingTrackFound -> {
            MainScreenEvent.NowPlayingTrackReceived(track = track)
        }
        ResponseEvent.DoNothing -> Event.NothingHappened
        is ResponseEvent.Exception -> Event.NothingHappened
        ResponseEvent.Loading -> Event.NothingHappened
        ResponseEvent.NetworkUnavailable -> Event.NothingHappened
        is ResponseEvent.ServerError -> Event.NothingHappened
    }

val PlayingEvent.mainScreenEvent
get() = when(this){
    PlayingEvent.TrackEnd -> Event.NothingHappened
    is PlayingEvent.TrackPlayingStart -> MainScreenEvent.NowPlayingTrackChanged

}