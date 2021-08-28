package com.tma.romanova.domain.feature.playlist

import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.result.Result

interface PlaylistRepository {
    suspend fun getPlaylist(): Result<Playlist>
}