package com.tma.romanova.data.feature.playlist.data_store

import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.result.DataSourceType

class PlaylistDataStoreProvider(
    val dataStore: DataStore<PlaylistEntity, Playlist, Playlist>
    ):
    DataSourceProvider {
    override val sourceType: DataSourceType
        get() = DataSourceType.Memory
}