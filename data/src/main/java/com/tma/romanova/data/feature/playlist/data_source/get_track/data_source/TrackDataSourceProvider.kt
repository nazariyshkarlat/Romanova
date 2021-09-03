package com.tma.romanova.data.feature.playlist.data_source.get_track.data_source

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.feature.playlist.entity.TrackEntity
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.DataSourceType

class TrackDataSourceProvider(val dataSource: DataSource<TrackEntity, Track, Track>):
    DataSourceProvider {
    override val dataSourceType: DataSourceType
        get() = DataSourceType.Memory
}