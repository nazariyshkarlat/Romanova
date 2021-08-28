package com.tma.romanova.data.feature.playlist.entity

import android.icu.text.SimpleDateFormat
import com.tma.romanova.data.feature.playlist.entity.TrackEntity.Companion.toDate
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class PlaylistEntity(
    @SerialName("tracks")
    val tracks: List<TrackEntity>
)

@Serializable
data class TrackEntity(
    @SerialName("artwork_url")
    val artworkUrl: String,
    @SerialName("waveform_url")
    val waveformUrl: String,
    @SerialName("id")
    val id: Long,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("duration")
    val duration: Long,
    @SerialName("title")
    val title: String
){
    companion object{
        private val dateFormat = java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.US)
        fun String.toDate(): Date = dateFormat.parse(this)!!
    }
}

fun PlaylistEntity.toDomain() = Playlist(
    tracks = tracks.map{ it.toDomain()}
)

fun TrackEntity.toDomain() = Track(
    artworkUrl = artworkUrl,
    waveformUrl = waveformUrl,
    id = id,
    createdAt = createdAt.toDate(),
    duration = duration,
    title = title
)



/*
* {
tracks: [
{
"artwork_url": "https://i1.sndcdn.com/artworks-ZKEyONOnOFmgRZmg-lbS7QQ-large.jpg",
"stream_url": "https://api.soundcloud.com/tracks/950640094/stream",
"download_url": "https://api.soundcloud.com/tracks/950640094/download",
"waveform_url": "https://wave.sndcdn.com/hfVKdgMaH9wi_m.png",
"id": 950640094,
"created_at": "2020/12/19 15:02:11 +0000",
"duration": 73091,
"title": "104”
},
….
]
}*/
