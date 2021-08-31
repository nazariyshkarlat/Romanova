package com.tma.romanova.data.feature.playlist.entity

import android.icu.text.SimpleDateFormat
import com.tma.romanova.data.feature.playlist.entity.TrackEntity.Companion.date
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class PlaylistEntity(
    @SerialName("tracks")
    val tracks: List<TrackEntity>,
    @SerialName("title")
    val title: String
)

@Serializable
data class TrackEntity(
    @SerialName("artwork_url")
    val artworkUrl: String,
    @SerialName("waveform_url")
    val waveformUrl: String,
    @SerialName("id")
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("duration")
    val duration: Int,
    @SerialName("title")
    val title: String
){
    companion object{
        private val dateFormat = java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.US)
        val String.date: Date
        get() = dateFormat.parse(this)!!
    }
}

val PlaylistEntity.domain
get() = Playlist(
    tracks = tracks.map{ it.domain },
    title = title
)

val TrackEntity.domain
get() = Track(
    smallArtworkUrl = artworkUrl.replace("large", "t200x200"),
    largeArtworkUrl = artworkUrl.replace("large", "t500x500"),
    waveformUrl = waveformUrl,
    id = id,
    createdAt = createdAt.date,
    duration = duration,
    title = title
)