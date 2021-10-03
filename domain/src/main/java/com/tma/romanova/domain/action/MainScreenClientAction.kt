package com.tma.romanova.domain.action

import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState

sealed class MainScreenClientAction {
    object AppClose: MainScreenClientAction()
    object AboutAuthorItemClick : MainScreenClientAction()
    object NowPlayingTrackButtonClick : MainScreenClientAction()
    object NowPlayingTrackClick : MainScreenClientAction()
    data class TrackItemClick(val position: Int) : MainScreenClientAction()
    data class LikeClick(val position: Int) : MainScreenClientAction()
    data class CommentsClick(val position: Int) : MainScreenClientAction()
}

fun MainScreenClientAction.toIntent(mainScreenState: MainScreenState) = when(this){
    MainScreenClientAction.AppClose -> MainScreenIntent.SaveNowPlayingTrack
    MainScreenClientAction.AboutAuthorItemClick -> MainScreenIntent.GoToAboutAuthorScreen
    is MainScreenClientAction.CommentsClick -> {
        if(mainScreenState is MainScreenState.PlaylistLoadingSuccess) {
            MainScreenIntent.GoToTrackComments(
                trackId = mainScreenState.playlist.tracks[position].id
            )
        }
        else Intent.DoNothing
    }
    is MainScreenClientAction.LikeClick -> {
        if(mainScreenState is MainScreenState.PlaylistLoadingSuccess) {
            val track = mainScreenState.playlist.tracks[position]
            if(track.isLiked) {
                MainScreenIntent.RemoveTrackLike(
                    trackId = mainScreenState.playlist.tracks[position].id
                )
            }else {
                MainScreenIntent.AddTrackLike(
                    trackId = mainScreenState.playlist.tracks[position].id
                )
            }
        }
        else Intent.DoNothing
    }
    MainScreenClientAction.NowPlayingTrackButtonClick -> {
        when(val nowPlaying = mainScreenState.nowPlayingState){
            is MainScreenState.NowPlayingState.AudioIsPlaying -> {
                if(nowPlaying.track.playingState is PlayingState.IsOnPause){
                    MainScreenIntent.ResumeNowPlayingTrack(
                        track = nowPlaying.track
                    )
                }else {
                    MainScreenIntent.PauseNowPlayingTrack(
                        track = nowPlaying.track
                    )
                }
            }
            MainScreenState.NowPlayingState.NoAudioAvailable -> Intent.DoNothing
        }
    }
    is MainScreenClientAction.TrackItemClick -> {
        if(mainScreenState is MainScreenState.PlaylistLoadingSuccess) {
            MainScreenIntent.GoToPlayerScreen(
                track = mainScreenState.playlist.tracks[position]
            )
        }
        else Intent.DoNothing
    }
    MainScreenClientAction.NowPlayingTrackClick -> {
        when(val nowPlaying = mainScreenState.nowPlayingState){
            MainScreenState.NowPlayingState.NoAudioAvailable -> {
                Intent.DoNothing
            }
            is MainScreenState.NowPlayingState.AudioIsPlaying -> {
                MainScreenIntent.GoToNowPlayingTrackPlayerScreen(
                    track = nowPlaying.track
                )
            }
        }
    }
}