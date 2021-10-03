package com.tma.romanova.domain.action

import com.tma.romanova.domain.extensions.atIndexOrFirst
import com.tma.romanova.domain.extensions.atIndexOrLast
import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.PlayerIntent
import com.tma.romanova.domain.intent.TouchStatus
import com.tma.romanova.domain.state.feature.player.PlayerState

sealed class PlayerClientAction {
    object AppClose: PlayerClientAction()
    object BackButtonClick: PlayerClientAction()
    object LikeClick: PlayerClientAction()
    data class TimeLineDownAction(
        // @FloatRange(from = 0.0, to = 1.0)
        val position: Float,
    ): PlayerClientAction()
    data class TimeLineMoveAction(
        // @FloatRange(from = 0.0, to = 1.0)
        val position: Float,
    ): PlayerClientAction()
    data class TimeLineUpAction(
        // @FloatRange(from = 0.0, to = 1.0)
        val position: Float,
    ): PlayerClientAction()
    object TimeLineCancelAction: PlayerClientAction()
    object ToPreviousTrackClick: PlayerClientAction()
    object ToNextTrackClick: PlayerClientAction()
    object CenterButtonClick: PlayerClientAction()
    object TimeUpClick: PlayerClientAction()
    object TimeBackClick: PlayerClientAction()
    data class WaveFormPartsCountMeasured(
        val partsCount: Int
        ): PlayerClientAction()
    data class OnTrackImageScrolled(val newTrackPosition: Int): PlayerClientAction()
}

fun PlayerClientAction.toIntent(playerState: PlayerState) = when(this) {
    PlayerClientAction.AppClose -> PlayerIntent.SaveNowPlayingTrack
    PlayerClientAction.BackButtonClick -> PlayerIntent.NavigateBack
    PlayerClientAction.ToNextTrackClick -> {
        if(playerState is PlayerState.TrackIsPlaying && (
                    playerState.desiredCurrentTrack == null ||
            playerState.desiredCurrentTrack.first.id == playerState.currentTrack.id)) {
            PlayerIntent.NavigateToNextTrack(
                trackId = playerState.allTracks.atIndexOrFirst(
                    playerState.allTracks.indexOfFirst {
                        it.id == playerState.currentTrack.id
                    }+1
                ).id
            )
        }else Intent.DoNothing
    }
    PlayerClientAction.ToPreviousTrackClick -> {
        if(playerState is PlayerState.TrackIsPlaying  && (
                    playerState.desiredCurrentTrack == null ||
                            playerState.desiredCurrentTrack.first.id == playerState.currentTrack.id)) {
            PlayerIntent.NavigateToPreviousTrack(
                playerState.allTracks.atIndexOrLast(
                    playerState.allTracks.indexOfFirst {
                        it.id == playerState.currentTrack.id
                    }-1
                ).id
            )
        }else Intent.DoNothing
    }
    PlayerClientAction.CenterButtonClick -> {
        when(playerState){
            PlayerState.Loading -> Intent.DoNothing
            is PlayerState.TrackIsPlaying -> {
                when(playerState.currentTrack.playingState){
                    PlayingState.IsNotPlaying -> Intent.DoNothing
                    is PlayingState.IsOnPause -> PlayerIntent.ResumeTrack
                    is PlayingState.IsPlaying -> PlayerIntent.PauseTrack
                }
            }
        }
    }
    PlayerClientAction.LikeClick -> {
        when(playerState){
            PlayerState.Loading -> Intent.DoNothing
            is PlayerState.TrackIsPlaying -> {
                if(playerState.currentTrack.isLiked) PlayerIntent.DislikeTrack
                else PlayerIntent.LikeTrack
            }
        }
    }
    PlayerClientAction.TimeUpClick -> PlayerIntent.UpPlayingTime
    PlayerClientAction.TimeBackClick -> PlayerIntent.DownPlayingTime
    is PlayerClientAction.WaveFormPartsCountMeasured -> {
        when(playerState){
            PlayerState.Loading -> Intent.DoNothing
            is PlayerState.TrackIsPlaying -> {
                PlayerIntent.DownloadWaveFormValues(
                    partsCount = this.partsCount,
                    waveFormUrl = playerState.currentTrack.waveformUrl
                )
            }
        }
    }
    PlayerClientAction.TimeLineCancelAction -> PlayerIntent.MoveWaveFormToStartTouchPosition
    is PlayerClientAction.TimeLineMoveAction -> PlayerIntent.MoveWaveFormToPosition(
        playedPercent = position,
        touchStatus = TouchStatus.IsTouch(
            isDownAction = false
        )
    )
    is PlayerClientAction.TimeLineDownAction -> PlayerIntent.MoveWaveFormToPosition(
        playedPercent = position,
        touchStatus = TouchStatus.IsTouch(
            isDownAction = true
        )
    )
    is PlayerClientAction.OnTrackImageScrolled -> {
        if(playerState is PlayerState.TrackIsPlaying) {
            PlayerIntent.LoadTrack(
                trackId = playerState.allTracks[newTrackPosition].id
            )
        }else Intent.DoNothing
    }
    is PlayerClientAction.TimeLineUpAction -> {
        if(playerState is PlayerState.TrackIsPlaying) {
            PlayerIntent.MoveTrackToPosition(
                newPositionMs = (position*playerState.currentTrack.duration).toLong(),
                isClickAction = true
            )
        }else Intent.DoNothing
    }
    else -> Intent.DoNothing
}

sealed class TouchAction{
    object CancelAction: TouchAction()
    data class DownAction(
        // @FloatRange(from = 0.0, to = 1.0)
        val position: Float
    ): TouchAction()
    data class MoveAction(
        // @FloatRange(from = 0.0, to = 1.0)
        val position: Float
    ): TouchAction()
    data class UpAction(
        // @FloatRange(from = 0.0, to = 1.0)
        val position: Float
    ): TouchAction()
}

val TouchAction.playerClientAction
get() = when(this){
    TouchAction.CancelAction -> PlayerClientAction.TimeLineCancelAction
    is TouchAction.DownAction -> PlayerClientAction.TimeLineDownAction(
        position = position
    )
    is TouchAction.MoveAction -> PlayerClientAction.TimeLineMoveAction(
        position = position
    )
    is TouchAction.UpAction -> PlayerClientAction.TimeLineUpAction(
        position = position
    )
}