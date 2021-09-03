package com.tma.romanova.domain.state.feature.player.reducer

import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.intent.PlayerIntent
import com.tma.romanova.domain.mvi.Reducer
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState
import com.tma.romanova.domain.state.feature.player.PlayerState

fun PlayerReducer() =
    Reducer<PlayerState, PlayerIntent> { currentState, intent ->
        when (currentState) {
            PlayerState.Loading -> currentState
            is PlayerState.TrackIsPlaying -> {
                when (intent) {
                    is PlayerIntent.ChangePosition -> {
                        currentState.copy(
                            track = currentState.track.copy(
                                playingState = when (currentState.track.playingState) {
                                    PlayingState.IsNotPlaying -> currentState.track.playingState
                                    is PlayingState.IsOnPause -> currentState.track.playingState.copy(
                                        currentPositionMs = intent.newPositionMs
                                    )
                                    is PlayingState.IsPlaying -> currentState.track.playingState.copy(
                                        currentPositionMs = intent.newPositionMs
                                    )
                                }
                            )
                        )
                    }
                    PlayerIntent.DislikeTrack -> currentState.copy(
                        track = currentState.track.copy(
                            isLiked = false
                        )
                    )
                    PlayerIntent.LikeTrack -> currentState.copy(
                        track = currentState.track.copy(
                            isLiked = true
                        )
                    )
                    PlayerIntent.PauseTrack -> currentState.copy(
                        track = currentState.track.copy(
                            playingState = when (currentState.track.playingState) {
                                PlayingState.IsNotPlaying -> currentState.track.playingState
                                is PlayingState.IsOnPause -> PlayingState.IsOnPause(
                                    currentPositionMs = currentState.track.playingState.currentPositionMs
                                )
                                is PlayingState.IsPlaying -> currentState.track.playingState
                            }
                        )
                    )
                    PlayerIntent.ResumeTrack -> currentState.copy(
                        track = currentState.track.copy(
                            playingState = when (currentState.track.playingState) {
                                PlayingState.IsNotPlaying -> currentState.track.playingState
                                is PlayingState.IsOnPause -> PlayingState.IsPlaying(
                                    currentPositionMs = currentState.track.playingState.currentPositionMs
                                )
                                is PlayingState.IsPlaying -> currentState.track.playingState
                            }
                        )
                    )
                    PlayerIntent.ShowPageIsLoading -> {
                        PlayerState.Loading
                    }
                    is PlayerIntent.ShowTrack -> {
                        PlayerState.TrackIsPlaying(
                            track = intent.track
                        )
                    }
                    is PlayerIntent.ShowPageLoadingError -> {
                        //TODO
                        currentState
                    }
                    else -> currentState
                }

            }
        }
    }