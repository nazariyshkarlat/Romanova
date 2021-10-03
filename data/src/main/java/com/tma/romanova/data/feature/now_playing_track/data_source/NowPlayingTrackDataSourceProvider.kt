package com.tma.romanova.data.feature.now_playing_track.data_source

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.memory.MemoryResult
import com.tma.romanova.data.feature.playlist.memory.GetPlaylistMemoryRequest
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.data.feature.playlist.entity.TrackEntity
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.DataSourceType

class NowPlayingTrackDataSourceProvider(
    private val dataSource: DataSource<TrackEntity, Track, Track>
    ):
    DataSourceProvider {
    override val sourceType: DataSourceType
        get() = DataSourceType.Cache
}