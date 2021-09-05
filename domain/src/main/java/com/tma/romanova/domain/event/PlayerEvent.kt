package com.tma.romanova.domain.event

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.intent.PlayerIntent
import com.tma.romanova.domain.result.ErrorCause

sealed interface PlayerEvent {
    data class TrackLoadingSuccess(val track: Track): PlayerEvent
    object TrackLoadingStart : PlayerEvent
    data class TrackLoadingError(
        val errorCause: ErrorCause
    ): PlayerEvent
    object PageOpen : PlayerEvent
    data class NeedsChangeTimeLinePosition(
        val newPositionMs: Int
    ): PlayerEvent
    object WaveFormLoadingError: PlayerEvent
    data class WaveFormValuesReceived(
        val values: List<Float>
        ): PlayerEvent
}

val PlayerEvent.intent
    get() = when(this){
        is PlayerEvent.TrackLoadingError -> PlayerIntent.ShowPageLoadingError(errorCause = errorCause)
        PlayerEvent.TrackLoadingStart -> PlayerIntent.ShowPageIsLoading
        is PlayerEvent.TrackLoadingSuccess -> PlayerIntent.ShowTrack(track = track)
        Event.DoNothing -> Intent.DoNothing
        PlayerEvent.PageOpen -> PlayerIntent.LoadTrack
        is PlayerEvent.NeedsChangeTimeLinePosition -> PlayerIntent.ChangePosition(
            newPositionMs = newPositionMs
        )
        PlayerEvent.WaveFormLoadingError -> PlayerIntent.ShowWaveFormLoadingError
        is PlayerEvent.WaveFormValuesReceived -> PlayerIntent.ShowWaveForm(
            values = values
        )
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

val GetTrackEvent.playerEvent: PlayerEvent
    get() = when(this){
        is GetTrackEvent.TrackFound -> {
            PlayerEvent.TrackLoadingSuccess(track = track)
        }
        GetTrackEvent.TrackNotFound -> Event.DoNothing
        ResponseEvent.DoNothing -> Event.DoNothing
        is ResponseEvent.Exception -> PlayerEvent.TrackLoadingError(
            errorCause = this.errorCase
        )
        ResponseEvent.Loading -> PlayerEvent.TrackLoadingStart
        ResponseEvent.NetworkUnavailable -> PlayerEvent.TrackLoadingError(
            errorCause = ResponseEvent.NetworkUnavailable.errorCase
        )
        is ResponseEvent.ServerError -> PlayerEvent.TrackLoadingError(
            errorCause = this.errorCase
        )
    }