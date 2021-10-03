package com.tma.romanova.data.feature.track_stream.data_source

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.domain.result.DataSourceType

class StreamDataSourceProvider():
    DataSourceProvider {
    override val sourceType: DataSourceType
        get() = DataSourceType.Network
}