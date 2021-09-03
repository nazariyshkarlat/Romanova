package com.tma.romanova.domain.state.feature.main_screen.reducer

import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.mvi.Reducer
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState

fun MainScreenReducer() =
    Reducer<MainScreenState, MainScreenIntent>{ currentState, intent->
        when(intent){
            is MainScreenIntent.AddTrackLike -> {
                when(currentState){
                    is MainScreenState.PlaylistIsLoading -> currentState
                    is MainScreenState.PlaylistLoadingError -> currentState
                    is MainScreenState.PlaylistLoadingSuccess -> {
                        currentState.copy(
                            playlist = currentState.playlist.copy(
                                tracks = currentState.playlist.tracks.map {
                                    if(intent.trackId == it.id) it.copy(isLiked = true)
                                    else it
                                }
                            )
                        )
                    }
                }
            }
            Intent.DoNothing -> currentState
            is MainScreenIntent.PauseNowPlayingTrack -> {
                when(val nowPlaying = currentState.nowPlayingState){
                    is MainScreenState.NowPlayingState.AudioIsPlaying -> {
                        when(currentState){
                            is MainScreenState.PlaylistIsLoading -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        isOnPause = true
                                    )
                                )
                            }
                            is MainScreenState.PlaylistLoadingError -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        isOnPause = true
                                    )
                                )
                            }
                            is MainScreenState.PlaylistLoadingSuccess -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        isOnPause = true
                                    )
                                )
                            }
                        }
                    }
                    MainScreenState.NowPlayingState.NoAudioAvailable -> currentState
                }
            }
            is MainScreenIntent.RemoveTrackLike -> {
                when(currentState){
                    is MainScreenState.PlaylistIsLoading -> currentState
                    is MainScreenState.PlaylistLoadingError -> currentState
                    is MainScreenState.PlaylistLoadingSuccess -> {
                        currentState.copy(
                            playlist = currentState.playlist.copy(
                                tracks = currentState.playlist.tracks.map {
                                    if(intent.trackId == it.id) it.copy(isLiked = false)
                                    else it
                                }
                            )
                        )
                    }
                }
            }
            is MainScreenIntent.ResumeNowPlayingTrack -> {
                when(val nowPlaying = currentState.nowPlayingState){
                    is MainScreenState.NowPlayingState.AudioIsPlaying -> {
                        when(currentState){
                            is MainScreenState.PlaylistIsLoading -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        isOnPause = false
                                    )
                                )
                            }
                            is MainScreenState.PlaylistLoadingError -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        isOnPause = false
                                    )
                                )
                            }
                            is MainScreenState.PlaylistLoadingSuccess -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        isOnPause = false
                                    )
                                )
                            }
                        }
                    }
                    MainScreenState.NowPlayingState.NoAudioAvailable -> currentState
                }
            }
            MainScreenIntent.ShowPageIsLoading -> {
                MainScreenState.PlaylistIsLoading(
                    nowPlayingState = currentState.nowPlayingState
                )
            }
            is MainScreenIntent.ShowPageLoadingError -> {
                MainScreenState.PlaylistLoadingError(
                    nowPlayingState = currentState.nowPlayingState,
                    errorCause = intent.errorCause
                )
            }
            is MainScreenIntent.ShowPlaylist -> {
                MainScreenState.PlaylistLoadingSuccess(
                    nowPlayingState = currentState.nowPlayingState,
                    playlist = intent.playlist
                )
            }
            else -> currentState
        }
    }