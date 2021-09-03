package com.tma.romanova.domain.action

import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.PlayerIntent
import com.tma.romanova.domain.state.feature.player.PlayerState

sealed class PlayerClientAction {
    object BackButtonClick: PlayerClientAction()
    object LikeClick: PlayerClientAction()
    data class TimeLineClick(
        // @FloatRange(from = 0.0, to = 1.0)
        val position: Float,
    ): PlayerClientAction()
    object ToPreviousTrackClick: PlayerClientAction()
    object ToNextTrackClick: PlayerClientAction()
    object CenterButtonClick: PlayerClientAction()
    object TimeUpClick: PlayerClientAction()
    object TimeBackClick: PlayerClientAction()
}

fun PlayerClientAction.toIntent(playerState: PlayerState) = when(this) {
    PlayerClientAction.BackButtonClick -> PlayerIntent.NavigateBack
    PlayerClientAction.CenterButtonClick -> {
        when(playerState){
            PlayerState.Loading -> Intent.DoNothing
            is PlayerState.TrackIsPlaying -> {
                when(playerState.track.playingState){
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
                if(playerState.track.isLiked) PlayerIntent.DislikeTrack
                else PlayerIntent.LikeTrack
            }
        }
    }
    else -> Intent.DoNothing
}