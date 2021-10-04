package com.tma.romanova.domain.state.feature.player.reducer

import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.intent.PlayerIntent
import com.tma.romanova.domain.intent.TouchStatus
import com.tma.romanova.domain.mvi.Reducer
import com.tma.romanova.domain.state.feature.player.DirectionPriority
import com.tma.romanova.domain.state.feature.player.PlayerState
import com.tma.romanova.domain.state.feature.player.WaveFormValuesStatus

fun PlayerReducer() =
    Reducer<PlayerState, PlayerIntent> { currentState, intent ->
        when (currentState) {
            PlayerState.Loading -> {
                when(intent){
                    is PlayerIntent.ShowTrack -> {
                        PlayerState.TrackIsPlaying(
                            currentTrack = intent.currentTrack,
                            waveFormValuesStatus = WaveFormValuesStatus.Loading,
                            allTracks = intent.allTracks,
                            waveFormLastTouchStartPosition = null,
                            desiredCurrentTrack = null,
                            waveFormFilledPercent =
                            (intent.currentTrack.playingState.positionMs ?: 0L)/intent.currentTrack.duration.toFloat()
                        )
                    }
                    is PlayerIntent.ShowPageLoadingError -> {
                        //TODO
                        currentState
                    }
                    else -> currentState
                }
            }
            is PlayerState.TrackIsPlaying -> {
                when (intent) {
                    is PlayerIntent.MoveTrackToPosition -> {
                        currentState.copy(
                            waveFormLastTouchStartPosition =
                            when {
                                intent.isClickAction -> null
                                else -> currentState.waveFormLastTouchStartPosition
                            },
                            currentTrack = currentState.currentTrack.copy(
                                playingState = when (currentState.currentTrack.playingState) {
                                    PlayingState.IsNotPlaying -> currentState.currentTrack.playingState
                                    is PlayingState.IsOnPause -> currentState.currentTrack.playingState.copy(
                                        currentPositionMs = intent.newPositionMs
                                    )
                                    is PlayingState.IsPlaying -> currentState.currentTrack.playingState.copy(
                                        currentPositionMs = intent.newPositionMs
                                    )
                                }
                            )
                        )
                    }
                    is PlayerIntent.MoveWaveFormToPosition -> {
                        if(!(currentState.timeLineIsPressed && intent.touchStatus == TouchStatus.IsNotTouch)) {
                            currentState.copy(
                                waveFormFilledPercent = intent.playedPercent,
                                waveFormLastTouchStartPosition =
                                when {
                                    intent.touchStatus.isDownActionVal  -> currentState.waveFormFilledPercent
                                    else -> currentState.waveFormLastTouchStartPosition
                                }
                            )
                        }else currentState
                    }
                    PlayerIntent.MoveWaveFormToStartTouchPosition -> {
                        currentState.copy(
                            waveFormFilledPercent =
                            currentState.waveFormLastTouchStartPosition
                                ?: currentState.waveFormFilledPercent
                        )
                    }
                    PlayerIntent.DislikeTrack -> currentState.copy(
                        currentTrack = currentState.currentTrack.copy(
                            isLiked = false
                        )
                    )
                    PlayerIntent.LikeTrack -> currentState.copy(
                        currentTrack = currentState.currentTrack.copy(
                            isLiked = true
                        )
                    )
                    PlayerIntent.PauseTrack -> currentState.copy(
                        currentTrack = currentState.currentTrack.copy(
                            playingState = when (currentState.currentTrack.playingState) {
                                PlayingState.IsNotPlaying -> currentState.currentTrack.playingState
                                is PlayingState.IsOnPause -> currentState.currentTrack.playingState
                                is PlayingState.IsPlaying -> PlayingState.IsOnPause(
                                    currentPositionMs = currentState.currentTrack.playingState.currentPositionMs
                                )
                            }
                        )
                    )
                    PlayerIntent.ResumeTrack -> currentState.copy(
                        currentTrack = currentState.currentTrack.copy(
                            playingState = when (currentState.currentTrack.playingState) {
                                PlayingState.IsNotPlaying -> currentState.currentTrack.playingState
                                is PlayingState.IsOnPause -> PlayingState.IsPlaying(
                                    currentPositionMs = currentState.currentTrack.playingState.currentPositionMs
                                )
                                is PlayingState.IsPlaying -> currentState.currentTrack.playingState
                            }
                        )
                    )
                    PlayerIntent.ShowPageIsLoading -> {
                        PlayerState.Loading
                    }
                    is PlayerIntent.ShowTrack -> {
                        println(intent.currentTrack.playingState.positionMs)
                        PlayerState.TrackIsPlaying(
                            currentTrack = intent.currentTrack,
                            waveFormValuesStatus = WaveFormValuesStatus.Loading,
                            allTracks = intent.allTracks,
                            waveFormLastTouchStartPosition = null,
                            waveFormFilledPercent =
                            (intent.currentTrack.playingState.positionMs ?: 0L)/intent.currentTrack.duration.toFloat(),
                            desiredCurrentTrack = null
                        )
                    }
                    is PlayerIntent.NavigateToNextTrack -> {
                        currentState.copy(
                            desiredCurrentTrack = currentState.allTracks.first { it.id == intent.trackId } to DirectionPriority.Right
                        )
                    }
                    is PlayerIntent.NavigateToPreviousTrack -> {
                        currentState.copy(
                            desiredCurrentTrack = currentState.allTracks.first { it.id == intent.trackId } to DirectionPriority.Left
                        )
                    }
                    is PlayerIntent.DownloadWaveFormValues -> {
                        currentState.copy(
                            waveFormValuesStatus = WaveFormValuesStatus.Loading
                        )
                    }
                    is PlayerIntent.ShowWaveFormLoadingError -> {
                        currentState.copy(
                            waveFormValuesStatus = WaveFormValuesStatus.Error
                        )
                    }
                    is PlayerIntent.ShowWaveForm -> {
                        currentState.copy(
                            waveFormValuesStatus = WaveFormValuesStatus.ValuesReceived(
                                values = intent.values
                            )
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