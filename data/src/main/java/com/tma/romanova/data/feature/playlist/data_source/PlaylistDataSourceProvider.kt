package com.tma.romanova.data.feature.playlist.data_source

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.memory.MemoryResult
import com.tma.romanova.data.feature.playlist.memory.GetPlaylistMemoryRequest
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.result.DataSourceType

class PlaylistDataSourceProvider(
    private val dataSource: DataSource<PlaylistEntity, Playlist, Playlist>
    ):
    DataSourceProvider {
    override val sourceType: DataSourceType
        get() = run{
            val memoryResult = dataSource.getFromMemory(
                GetPlaylistMemoryRequest()
            )
            when (memoryResult) {
                is MemoryResult.Success -> {
                    DataSourceType.Memory
                }
                else -> DataSourceType.Network
            }
        }
}