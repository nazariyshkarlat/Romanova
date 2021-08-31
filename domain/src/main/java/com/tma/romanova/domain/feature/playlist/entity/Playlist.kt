package com.tma.romanova.domain.feature.playlist.entity

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
    val createdAt: Date,
    val duration: Int,
    val title: String,
    val isLiked: Boolean = false,
    val likesCount: Int = 12,
    val commentsCount: Int = 4
)