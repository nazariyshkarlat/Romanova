package com.tma.romanova.domain.intent

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.ErrorCause

sealed interface MainScreenIntent {
    object SaveNowPlayingTrack: MainScreenIntent
    data class ShowPageLoadingError(
        val errorCause: ErrorCause
    ) : MainScreenIntent
    object ShowPageIsLoading : MainScreenIntent
    data class ShowPlaylist(
        val playlist: Playlist
    ) : MainScreenIntent
    object LoadData : MainScreenIntent
    data class AddTrackLike(val trackId: Int) : MainScreenIntent
    data class RemoveTrackLike(val trackId: Int) : MainScreenIntent
    data class GoToTrackComments(val trackId: Int) : MainScreenIntent
    object GoToAboutAuthorScreen : MainScreenIntent
    data class UpdateNowPlayingTrack(val track: Track) : MainScreenIntent
    object GetNowPlayingTrack : MainScreenIntent
    data class GoToPlayerScreen(val track: Track) : MainScreenIntent
    data class GoToNowPlayingTrackPlayerScreen(val track: Track) : MainScreenIntent
    data class PauseNowPlayingTrack(val track: Track) : MainScreenIntent
    data class ResumeNowPlayingTrack(val track: Track) : MainScreenIntent
}