package com.tma.romanova.domain.feature.playlist

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.Result

interface PlaylistRepository {
    suspend fun getPlaylist(): Result<Playlist>
    suspend fun getTrack(trackId: Int): Result<Track>
    suspend fun savePlaylist(playlist: Playlist)
    suspend fun getWaveFormValues(url: String, partsCount: Int): Result<List<Float>>
}