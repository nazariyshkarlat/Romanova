package com.tma.romanova.domain.state.feature.main_screen.reducer

import com.tma.romanova.domain.feature.playlist.entity.PlayingState
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
            is MainScreenIntent.UpdateNowPlayingTrack -> {
                when(currentState){
                    is MainScreenState.PlaylistIsLoading -> currentState.copy(
                        nowPlayingState = MainScreenState.NowPlayingState.AudioIsPlaying(
                            track = intent.track
                        )
                    )
                    is MainScreenState.PlaylistLoadingError -> currentState.copy(
                        nowPlayingState = MainScreenState.NowPlayingState.AudioIsPlaying(
                            track = intent.track
                        )
                    )
                    is MainScreenState.PlaylistLoadingSuccess -> currentState.copy(
                        nowPlayingState = MainScreenState.NowPlayingState.AudioIsPlaying(
                            track = intent.track
                        )
                    )
                }
            }
            is MainScreenIntent.PauseNowPlayingTrack -> {
                when(val nowPlaying = currentState.nowPlayingState){
                    is MainScreenState.NowPlayingState.AudioIsPlaying -> {
                        when(currentState){
                            is MainScreenState.PlaylistIsLoading -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        track = nowPlaying.track.copy(
                                            playingState = PlayingState.IsOnPause(
                                                currentPositionMs = nowPlaying.track.playingState.positionMs ?: 0L
                                            )
                                        )
                                    )
                                )
                            }
                            is MainScreenState.PlaylistLoadingError -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        track = nowPlaying.track.copy(
                                            playingState = PlayingState.IsOnPause(
                                                currentPositionMs = nowPlaying.track.playingState.positionMs ?: 0L
                                            )
                                        )
                                    )
                                )
                            }
                            is MainScreenState.PlaylistLoadingSuccess -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        track = nowPlaying.track.copy(
                                            playingState = PlayingState.IsOnPause(
                                                currentPositionMs = nowPlaying.track.playingState.positionMs ?: 0L
                                            )
                                        )
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
                                        track = nowPlaying.track.copy(
                                            playingState = PlayingState.IsPlaying(
                                                currentPositionMs = nowPlaying.track.playingState.positionMs ?: 0L
                                            )
                                        )
                                    )
                                )
                            }
                            is MainScreenState.PlaylistLoadingError -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        track = nowPlaying.track.copy(
                                            playingState = PlayingState.IsPlaying(
                                                currentPositionMs = nowPlaying.track.playingState.positionMs ?: 0L
                                            )
                                        )
                                    )
                                )
                            }
                            is MainScreenState.PlaylistLoadingSuccess -> {
                                currentState.copy(
                                    nowPlayingState = nowPlaying.copy(
                                        track = nowPlaying.track.copy(
                                            playingState = PlayingState.IsPlaying(
                                                currentPositionMs = nowPlaying.track.playingState.positionMs ?: 0L
                                            )
                                        )
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