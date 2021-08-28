package com.tma.romanova.data.feature.playlist.data_source.get_playlist

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.domain.result.DataSourceType

class PlaylistDataSourceProvider(val dataSource: DataSource<PlaylistEntity, PlaylistEntity, PlaylistEntity>):
    DataSourceProvider {
    override val dataSourceType: DataSourceType
        get() = DataSourceType.Network
}