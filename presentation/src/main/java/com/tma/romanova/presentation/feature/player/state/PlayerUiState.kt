package com.tma.romanova.presentation.feature.player.state

import android.graphics.drawable.Drawable
import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_BACK_MS
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_UP_MS
import com.tma.romanova.domain.state.feature.player.DirectionPriority
import com.tma.romanova.domain.state.feature.player.PlayerState
import com.tma.romanova.domain.state.feature.player.WaveFormValuesStatus
import com.tma.romanova.presentation.R
import com.tma.romanova.presentation.extensions.drawable
import com.tma.romanova.presentation.extensions.str
import com.tma.romanova.presentation.feature.main.entity.MainScreenTrackItemUi
import com.tma.romanova.presentation.feature.main.entity.durationStr
import com.tma.romanova.presentation.feature.player.state.PlayerUiState.Companion.likeFilledIcon
import com.tma.romanova.presentation.feature.player.state.PlayerUiState.Companion.likeIcon
import com.tma.romanova.presentation.feature.player.state.PlayerUiState.Companion.pauseIcon
import com.tma.romanova.presentation.feature.player.state.PlayerUiState.Companion.playIcon

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
        val likeFilledIcon: Drawable by lazy{
            R.drawable.ic_like_filled.drawable
        }
        val likeIcon: Drawable by lazy {
            R.drawable.ic_like.drawable
        }
        val pauseIcon: Drawable by lazy{
            R.drawable.ic_pause.drawable
        }
        val playIcon: Drawable by lazy {
            R.drawable.ic_play.drawable
        }
    }

    object TrackLoading: PlayerUiState()
    data class TrackLoaded(
        val currentTrack: TrackPlayerUi,
        val allTracks: List<TrackPlayerUi>,
        val waveFormValuesStatus: WaveFormValuesStatus,
        val playedPercent: Float,
        val currentTimePosition: String,
        private val _desiredTrackPosition: Pair<Int, com.tma.romanova.presentation.custom_components.DirectionPriority>?
    ): PlayerUiState(){

        val desiredTrackPosition
        get() = _desiredTrackPosition ?: (selectedTrackPosition to com.tma.romanova.presentation.custom_components.DirectionPriority.Right)

        val selectedTrackPosition
        get() = allTracks.indexOfFirst { it.trackId == currentTrack.trackId }

    }
}


val DirectionPriority.pagerValue
    get() = when(this){
        DirectionPriority.Left -> com.tma.romanova.presentation.custom_components.DirectionPriority.Left
        DirectionPriority.Right -> com.tma.romanova.presentation.custom_components.DirectionPriority.Right
    }

val PlayerState.ui
get() = when(this){
    PlayerState.Loading -> PlayerUiState.TrackLoading
    is PlayerState.TrackIsPlaying -> PlayerUiState.TrackLoaded(
        currentTrack = currentTrack.playerUi,
        playedPercent = waveFormFilledPercent,
        waveFormValuesStatus = waveFormValuesStatus,
        allTracks = allTracks.map { it.playerUi },
        currentTimePosition = (waveFormFilledPercent*currentTrack.duration).toInt().durationStr,
        _desiredTrackPosition = desiredCurrentTrack?.let{ desired->
            allTracks.indexOfFirst { it.id == desired.first.id } to desired.second.pagerValue
        }
    )
}

data class TrackPlayerUi(
    val isOnPrimaryLikesTextColor: Boolean,
    val likeIcon: Drawable,
    val artworkUrl: String,
    val trackTitle: String,
    val centerButtonIcon: Drawable,
    val trackId: Int
)

val Track.mainScreenTrackUi
    get() = MainScreenTrackItemUi(
        artworkUrl = largeArtworkUrl,
        id = id,
        duration = duration.durationStr,
        title = title,
        likeButtonIcon = if(isLiked) likeFilledIcon
        else likeIcon,
        commentsText = commentsCount.toString(),
        likesText = likesCount.toString(),
        isOnPrimaryLikesTextColor = isLiked
    )


val Track.playerUi
get() = TrackPlayerUi(
    trackId = id,
    isOnPrimaryLikesTextColor = isLiked,
    likeIcon = if(isLiked) likeFilledIcon
    else likeIcon,
    artworkUrl = largeArtworkUrl,
    trackTitle = title,
    centerButtonIcon = if(playingState is PlayingState.IsOnPause) playIcon
    else pauseIcon
)