package com.tma.romanova.domain.feature.playlist.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

data class Playlist(
    val tracks: List<Track>
)

data class Track(
    val artworkUrl: String,
    val waveformUrl: String,
    val id: Long,
    val createdAt: Date,
    val duration: Long,
    val title: String
)