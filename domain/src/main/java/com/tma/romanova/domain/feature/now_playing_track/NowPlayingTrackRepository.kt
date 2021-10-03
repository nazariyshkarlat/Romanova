package com.tma.romanova.domain.feature.now_playing_track

import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.Result

interface NowPlayingTrackRepository {
    suspend fun saveNowPlayingTrack(track: Track)
    suspend fun getNowPlayingTrack(): Result<Track>
}