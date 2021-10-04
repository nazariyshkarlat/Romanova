package com.tma.romanova.domain.intent

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.ErrorCause

sealed interface PlayerIntent {
    object SaveNowPlayingTrack: PlayerIntent
    object LikeTrack : PlayerIntent
    object DislikeTrack : PlayerIntent
    object NavigateBack : PlayerIntent
    data class NavigateToNextTrack(
        val trackId: Int
    ) : PlayerIntent
    data class NavigateToPreviousTrack(
        val trackId: Int
    ): PlayerIntent
    data class DownloadWaveFormValues(
        val partsCount: Int,
        val waveFormUrl: String
    ): PlayerIntent
    object PauseTrack : PlayerIntent
    object ResumeTrack : PlayerIntent
    data class ShowPageLoadingError(
        val errorCause: ErrorCause
    ) : PlayerIntent
    object ShowPageIsLoading : PlayerIntent
    data class ShowTrack(
        val currentTrack: Track,
        val allTracks: List<Track>
    ) : PlayerIntent
    data class UpdateTrack(val trackId: Int): PlayerIntent
    object UpPlayingTime: PlayerIntent
    object DownPlayingTime: PlayerIntent
    object ShowWaveFormLoadingError: PlayerIntent
    data class ShowWaveForm(
        val values: List<Float>
    ): PlayerIntent
    data class MoveWaveFormToPosition(
        val playedPercent: Float,
        val touchStatus: TouchStatus
    ): PlayerIntent
    data class MoveTrackToPosition(
        val newPositionMs: Long,
        val isClickAction: Boolean
    ): PlayerIntent
    object MoveWaveFormToStartTouchPosition: PlayerIntent
    object PlayerPrepared: PlayerIntent
}

sealed class TouchStatus{

    val isDownActionVal
    get() = this is IsTouch && this.isDownAction

    object IsNotTouch: TouchStatus()
    data class IsTouch(
        val isDownAction: Boolean
    ): TouchStatus()
}