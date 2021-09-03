package com.tma.romanova.domain.feature.playlist.entity

import com.tma.romanova.domain.extensions.date.DateSerializer
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

data class Playlist(
    val tracks: List<Track>,
    val title: String
)

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
    val likesCount: Int = 12,
    val commentsCount: Int = 4,
    val streamUrl: String,
    val playingState: PlayingState = PlayingState.IsOnPause(
        currentPositionMs = 0
    )
)

sealed class PlayingState{

    val positionMs: Int?
    get() = when(this){
        IsNotPlaying -> null
        is IsOnPause -> currentPositionMs
        is IsPlaying -> currentPositionMs
    }

    data class IsPlaying(
        val currentPositionMs: Int
    ) : PlayingState()
    data class IsOnPause(
        val currentPositionMs: Int
    ) : PlayingState()
    object IsNotPlaying : PlayingState()
}