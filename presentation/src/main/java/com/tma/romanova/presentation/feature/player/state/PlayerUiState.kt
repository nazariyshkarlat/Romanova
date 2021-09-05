package com.tma.romanova.presentation.feature.player.state

import android.graphics.drawable.Drawable
import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_BACK_MS
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_UP_MS
import com.tma.romanova.domain.state.feature.player.PlayerState
import com.tma.romanova.domain.state.feature.player.WaveFormValuesStatus
import com.tma.romanova.presentation.R
import com.tma.romanova.presentation.extensions.drawable
import com.tma.romanova.presentation.extensions.str
import com.tma.romanova.presentation.feature.main.entity.MainScreenTrackItemUi
import com.tma.romanova.presentation.feature.main.entity.durationStr

sealed class PlayerUiState {

    companion object {
        val backButtonIcon: Drawable by lazy {
            R.drawable.ic_arrow_back.drawable
        }
        val toolbarTitle: String by lazy {
            R.string.now_playing_title.str
        }
        val authorName: String by lazy {
            R.string.player_author_name.str
        }
        val prevBackIcon: Drawable by lazy {
            R.drawable.ic_player_previous.drawable
        }
        val nextTrackIcon: Drawable by lazy {
            R.drawable.ic_player_next.drawable
        }
        val timeBackIcon: Drawable by lazy {
            R.drawable.ic_player_back.drawable
        }
        val timeUpIcon: Drawable by lazy {
            R.drawable.ic_player_up.drawable
        }
        val timeBackText: String by lazy {
            "-${TIME_BACK_MS/1000}"
        }
        val timeUpText: String by lazy {
            "+${TIME_UP_MS/1000}"
        }
    }

    object TrackLoading: PlayerUiState()
    data class TrackLoaded(
        val track: TrackPlayerUi,
        val waveFormValuesStatus: WaveFormValuesStatus,
        val playedPercent: Float
    ): PlayerUiState()
}

val PlayerState.ui
get() = when(this){
    PlayerState.Loading -> PlayerUiState.TrackLoading
    is PlayerState.TrackIsPlaying -> PlayerUiState.TrackLoaded(
        track = track.playerUi,
        playedPercent = playedPercent,
        waveFormValuesStatus = waveFormValuesStatus
    )
}

data class TrackPlayerUi(
    val isOnPrimaryLikesTextColor: Boolean,
    val likeIcon: Drawable,
    val artworkUrl: String,
    val trackTitle: String,
    val currentTimePosition: String,
    val centerButtonIcon: Drawable,
    val trackId: Int
)

val Track.mainScreenTrackUi
    get() = MainScreenTrackItemUi(
        artworkUrl = largeArtworkUrl,
        id = id,
        duration = duration.durationStr,
        title = title,
        likeButtonIcon = if(isLiked) R.drawable.ic_like_filled.drawable
        else R.drawable.ic_like.drawable,
        commentsText = commentsCount.toString(),
        likesText = likesCount.toString(),
        isOnPrimaryLikesTextColor = isLiked
    )


val Track.playerUi
get() = TrackPlayerUi(
    trackId = id,
    isOnPrimaryLikesTextColor = isLiked,
    likeIcon = if(isLiked) R.drawable.ic_like_filled.drawable
    else R.drawable.ic_like.drawable,
    artworkUrl = largeArtworkUrl,
    trackTitle = title,
    currentTimePosition = playingState.positionMs?.durationStr ?: 0.durationStr,
    centerButtonIcon = if(playingState is PlayingState.IsOnPause) R.drawable.ic_play.drawable
    else R.drawable.ic_pause.drawable
)