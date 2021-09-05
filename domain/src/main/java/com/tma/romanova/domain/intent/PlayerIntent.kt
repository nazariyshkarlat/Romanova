package com.tma.romanova.domain.intent

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.ErrorCause

sealed interface PlayerIntent {
    object LikeTrack : PlayerIntent
    object DislikeTrack : PlayerIntent
    data class ChangePosition(
        val newPositionMs: Int
    ) : PlayerIntent
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
        val track: Track
    ) : PlayerIntent
    object LoadTrack: PlayerIntent
    object UpPlayingTime: PlayerIntent
    object DownPlayingTime: PlayerIntent
    object ShowWaveFormLoadingError: PlayerIntent
    data class ShowWaveForm(
        val values: List<Float>
    ): PlayerIntent
}