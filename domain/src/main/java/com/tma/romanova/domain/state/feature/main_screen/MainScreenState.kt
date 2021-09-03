package com.tma.romanova.domain.state.feature.main_screen

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.ErrorCause

sealed class MainScreenState {

    abstract val nowPlayingState: NowPlayingState

    val tracks: List<Track>?
    get() = (this as? PlaylistLoadingSuccess)?.playlist?.tracks

    data class PlaylistIsLoading(
        override val nowPlayingState: NowPlayingState
        ) : MainScreenState()

    data class PlaylistLoadingError(
        override val nowPlayingState: NowPlayingState,
        val errorCause: ErrorCause
    ) : MainScreenState()

    data class PlaylistLoadingSuccess(
        override val nowPlayingState: NowPlayingState,
        val playlist: Playlist
    ) : MainScreenState()

    sealed class NowPlayingState{
        object NoAudioAvailable: NowPlayingState()
        data class AudioIsPlaying(
            val isOnPause: Boolean,
            val track: Track
            ): NowPlayingState()
    }

}