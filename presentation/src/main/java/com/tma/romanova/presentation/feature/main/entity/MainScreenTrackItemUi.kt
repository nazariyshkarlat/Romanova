package com.tma.romanova.presentation.feature.main.entity

import android.graphics.drawable.Drawable
import com.tma.romanova.core.application
import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.presentation.R
import com.tma.romanova.presentation.extensions.dp
import com.tma.romanova.presentation.extensions.drawable
import com.tma.romanova.presentation.ui.drawable.CircularProgressDrawable

data class MainScreenTrackItemUi(
    val artworkUrl: String,
    val id: Int,
    val duration: String,
    val title: String,
    val likeButtonIcon: Drawable,
    val likesText: String,
    val commentsText: String,
    val isOnPrimaryLikesTextColor: Boolean
){
    val commentButtonIcon: Drawable by lazy {
        R.drawable.ic_comment.drawable
    }
    val progressBarDrawable by lazy{
        CircularProgressDrawable(application).apply {
            strokeWidth = 4F.dp.toFloat()
            centerRadius = 12F.dp.toFloat()
            start()
        }
    }
}

data class NowPlayingTrackUi(
    val buttonIcon: Drawable,
    val trackTitle: String,
    val artworkUrl: String,
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

val Track.nowPlayingTrackUi
get() = NowPlayingTrackUi(
        trackTitle = title,
        artworkUrl = smallArtworkUrl,
        trackId = id,
        buttonIcon = if(playingState is PlayingState.IsOnPause) R.drawable.ic_play.drawable
            else R.drawable.ic_pause.drawable
)

data class Duration(
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val milliseconds: Int = 0
)

val Int.duration
get() = Duration(
    seconds = (this/1000)%60,
    minutes = (this/(1000*60))%60,
    hours = (this/(1000*60*60)),
    milliseconds = (this)%1000
)

val Int.durationStr
get() = duration.string

val Duration.string
get() = buildString {
    if(hours != 0){
        append(hours)
        append(':')
    }
    when{
        hours == 0 && minutes == 0 -> {
            append(
                "0".repeat(2)
            )
            append(':')
        }
        hours == 0 && minutes != 0 -> {
            append(minutes)
            append(':')
        }
        hours != 0 -> {
            append(
                minutes.toString().padStart(2, '0')
            )
            append(':')
        }
    }
    append(
        seconds.toString().padStart(2, '0')
    )
}

