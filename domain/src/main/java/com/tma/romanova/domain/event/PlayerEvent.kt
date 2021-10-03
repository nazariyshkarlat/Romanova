package com.tma.romanova.domain.event

import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.PlayerIntent
import com.tma.romanova.domain.intent.TouchStatus
import com.tma.romanova.domain.result.ErrorCause
import com.tma.romanova.domain.state.feature.player.PlayerState

sealed interface PlayerEvent {
    data class TracksLoadingSuccess(
        val currentTrack: Track,
        val allTracks: List<Track>
        ): PlayerEvent
    object TrackLoadingStart : PlayerEvent
    data class TrackLoadingError(
        val errorCause: ErrorCause
    ): PlayerEvent
    data class PageOpen(val trackId: Int) : PlayerEvent
    data class NeedsChangeTimeLinePosition(
        val newPosition: Float
    ): PlayerEvent
    data class NeedsChangeTrackPosition(
        val newPosition: Float
    ): PlayerEvent
    object NeedsChangeTrackPositionToTouchStart: PlayerEvent
    object WaveFormLoadingError: PlayerEvent
    data class WaveFormValuesReceived(
        val values: List<Float>
        ): PlayerEvent
    object PrepareTrackStart: PlayerEvent
    object PrepareTrackStartWithPlaying: PlayerEvent
    object PrepareTrackError: PlayerEvent
    object PrepareTrackSuccess: PlayerEvent
    object TrackPaused: PlayerEvent
    object TrackResumed: PlayerEvent
    object TrackPauseError: PlayerEvent
    object TrackResumeError: PlayerEvent
    object TrackTimeUpSuccess: PlayerEvent
    object TrackTimeBackSuccess: PlayerEvent
    object TrackTimeUpError: PlayerEvent
    object TrackTimeBackError: PlayerEvent
    object TrackMoveToPositionError: PlayerEvent
    data class TrackMoveToPositionSuccess(
        val newPositionMs: Long
    ): PlayerEvent
}

fun PlayerEvent.toIntent(state: PlayerState) = when(this){
    is PlayerEvent.TrackLoadingError -> PlayerIntent.ShowPageLoadingError(errorCause = errorCause)
    PlayerEvent.TrackLoadingStart -> PlayerIntent.ShowPageIsLoading
    is PlayerEvent.TracksLoadingSuccess -> PlayerIntent.ShowTrack(
        currentTrack = currentTrack,
        allTracks = allTracks
    )
    Event.DoNothing -> Intent.DoNothing
    is PlayerEvent.PageOpen -> PlayerIntent.LoadTrack(trackId = trackId)
    is PlayerEvent.NeedsChangeTimeLinePosition -> PlayerIntent.MoveWaveFormToPosition(
        playedPercent = newPosition,
        touchStatus = TouchStatus.IsNotTouch
    )
    is PlayerEvent.NeedsChangeTrackPosition -> {
        if(state is PlayerState.TrackIsPlaying) {
            PlayerIntent.MoveTrackToPosition(
                newPositionMs = (newPosition*state.currentTrack.duration).toLong(),
                isClickAction = false
            )
        }else Intent.DoNothing
    }
    PlayerEvent.NeedsChangeTrackPositionToTouchStart -> PlayerIntent.MoveWaveFormToStartTouchPosition
    PlayerEvent.WaveFormLoadingError -> PlayerIntent.ShowWaveFormLoadingError
    is PlayerEvent.WaveFormValuesReceived -> PlayerIntent.ShowWaveForm(
        values = values
    )
    PlayerEvent.PrepareTrackError -> Intent.DoNothing
    PlayerEvent.PrepareTrackStart -> Intent.DoNothing
    PlayerEvent.PrepareTrackStartWithPlaying -> PlayerIntent.ResumeTrack
    PlayerEvent.PrepareTrackSuccess -> {
        PlayerIntent.PlayerPrepared
    }
    PlayerEvent.TrackPauseError -> Intent.DoNothing
    PlayerEvent.TrackPaused -> Intent.DoNothing
    PlayerEvent.TrackResumeError -> Intent.DoNothing
    PlayerEvent.TrackResumed -> Intent.DoNothing
    PlayerEvent.TrackTimeBackError -> Intent.DoNothing
    PlayerEvent.TrackTimeBackSuccess -> Intent.DoNothing
    PlayerEvent.TrackTimeUpError -> Intent.DoNothing
    PlayerEvent.TrackTimeUpSuccess -> Intent.DoNothing
    PlayerEvent.TrackMoveToPositionError -> Intent.DoNothing
    is PlayerEvent.TrackMoveToPositionSuccess -> Intent.DoNothing
}

val GetWaveFormEvent.playerEvent: PlayerEvent
get() = when(this){
    GetWaveFormEvent.Error -> PlayerEvent.WaveFormLoadingError
    ResponseEvent.DoNothing ->  Event.DoNothing
    is ResponseEvent.Exception -> PlayerEvent.WaveFormLoadingError
    ResponseEvent.Loading -> Event.DoNothing
    ResponseEvent.NetworkUnavailable -> PlayerEvent.WaveFormLoadingError
    is ResponseEvent.ServerError -> PlayerEvent.WaveFormLoadingError
    is GetWaveFormEvent.ValuesReceived -> PlayerEvent.WaveFormValuesReceived(
        values = values
    )
}

val Pair<GetTrackEvent, GetPlaylistEvent>.playerEvent: PlayerEvent
get() = when {
        first is GetTrackEvent.TrackFound &&
                second is GetPlaylistEvent.PlaylistFound -> {
            PlayerEvent.TracksLoadingSuccess(
                currentTrack = (first as GetTrackEvent.TrackFound).track,
                allTracks = (second as GetPlaylistEvent.PlaylistFound).playlist.tracks
            )
        }
        first is GetTrackEvent.TrackNotFound ||
                second is GetPlaylistEvent.PlaylistNotFound -> Event.DoNothing
        listOf(first, second).any {
            it is ResponseEvent.NetworkUnavailable
        } -> PlayerEvent.TrackLoadingError(
            errorCause = ResponseEvent.NetworkUnavailable.errorCase
        )
        listOf(first, second).any {
            it is ResponseEvent.ServerError
        } -> PlayerEvent.TrackLoadingError(
            errorCause = (first as ResponseEvent.ServerError).errorCase
        )
        listOf(first, second).any {
            it is ResponseEvent.Exception
        } -> PlayerEvent.TrackLoadingError(
            errorCause = (first as ResponseEvent.Exception).errorCase
        )
        first is ResponseEvent.Loading -> PlayerEvent.TrackLoadingStart
        else -> Event.DoNothing
}

val PrepareTrackEvent.playerEvent: PlayerEvent
get() = when(this){
    Event.DoNothing -> Event.DoNothing
    PrepareTrackEvent.PrepareCompleted -> PlayerEvent.PrepareTrackSuccess
    PrepareTrackEvent.PrepareStart -> PlayerEvent.PrepareTrackStart
    PrepareTrackEvent.PrepareStartWithPlaying -> PlayerEvent.PrepareTrackStartWithPlaying
    PrepareTrackEvent.PrepareTrackError -> PlayerEvent.PrepareTrackError
}

val PauseTrackEvent.playerEvent: PlayerEvent
    get() = when(this){
        Event.DoNothing -> Event.DoNothing
        PauseTrackEvent.TrackPaused -> PlayerEvent.TrackPaused
        PauseTrackEvent.PauseTrackError -> PlayerEvent.TrackPauseError
    }

val ResumeTrackEvent.playerEvent: PlayerEvent
    get() = when(this){
        Event.DoNothing -> Event.DoNothing
        ResumeTrackEvent.TrackResumed -> PlayerEvent.TrackResumed
        ResumeTrackEvent.ResumeTrackError -> PlayerEvent.TrackResumeError
    }

val MoveTimeUpTrackEvent.playerEvent: PlayerEvent
    get() = when(this){
        Event.DoNothing -> Event.DoNothing
        MoveTimeUpTrackEvent.MoveTimeUpTrackError -> PlayerEvent.TrackTimeUpError
        MoveTimeUpTrackEvent.MoveTimeUpSuccess -> PlayerEvent.TrackTimeUpSuccess
    }

val MoveTimeBackTrackEvent.playerEvent: PlayerEvent
    get() = when(this){
        Event.DoNothing -> Event.DoNothing
        MoveTimeBackTrackEvent.MoveTimeBackTrackError -> PlayerEvent.TrackTimeBackError
        MoveTimeBackTrackEvent.MoveTimeBackSuccess -> PlayerEvent.TrackTimeBackSuccess
    }

val MoveToPositionTrackEvent.playerEvent: PlayerEvent
    get() = when(this){
        Event.DoNothing -> Event.DoNothing
        MoveToPositionTrackEvent.MoveToPositionTrackError -> PlayerEvent.TrackMoveToPositionError
        is MoveToPositionTrackEvent.MoveToPositionTrackSuccess -> PlayerEvent.TrackMoveToPositionSuccess(
            newPositionMs = newPositionMs
        )
    }