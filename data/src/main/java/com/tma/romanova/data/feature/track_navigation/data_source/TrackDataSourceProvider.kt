package com.tma.romanova.data.feature.track_navigation.data_source

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.feature.playlist.entity.TrackEntity
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.DataSourceType

class TrackDataSourceProvider(val dataSource: DataSource<TrackEntity, Track, Track>):
    DataSourceProvider {
    override val sourceType: DataSourceType
        get() = DataSourceType.Memory
}