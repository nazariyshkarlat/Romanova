package com.tma.romanova.domain.state.feature.main_screen

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.ErrorCause
import com.tma.romanova.domain.state.feature.player.PlayerState

sealed class MainScreenState {

    abstract val nowPlayingState: NowPlayingState


    val currentTrackVal
        get() = when(val state = this.nowPlayingState){
            is NowPlayingState.AudioIsPlaying -> state.track
            NowPlayingState.NoAudioAvailable -> null
        }

    val tracks: List<Track>?
    get() = (this as? PlaylistLoadingSuccess)?.playlist?.tracks

    val playlistVal: Playlist?
        get() = (this as? PlaylistLoadingSuccess)?.playlist

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
            val track: Track
            ): NowPlayingState()
    }

}