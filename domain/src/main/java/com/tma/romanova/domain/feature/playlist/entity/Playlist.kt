package com.tma.romanova.domain.feature.playlist.entity

import com.tma.romanova.domain.extensions.date.DateSerializer
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Playlist(
    val id: Int,
    val tracks: List<Track>,
    val title: String
)

@Serializable
data class Track(
    val largeArtworkUrl: String,
    val smallArtworkUrl: String,
    val waveformUrl: String,
    val id: Int,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    val duration: Int,
    val title: String,
    val isLiked: Boolean = false,
    val likesCount: Int,
    val commentsCount: Int,
    val streamUrl: String,
    val playingState: PlayingState = PlayingState.IsOnPause(
        currentPositionMs = 0
    )
)

@Serializable
sealed class PlayingState{

    val positionMs: Long?
    get() = when(this){
        IsNotPlaying -> null
        is IsOnPause -> currentPositionMs
        is IsPlaying -> currentPositionMs
    }

    @Serializable
    data class IsPlaying(
        val currentPositionMs: Long
    ) : PlayingState()

    @Serializable
    data class IsOnPause(
        val currentPositionMs: Long
    ) : PlayingState()

    @Serializable
    object IsNotPlaying : PlayingState()
}