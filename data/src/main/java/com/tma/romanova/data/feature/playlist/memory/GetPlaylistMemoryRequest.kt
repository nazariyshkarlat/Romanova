package com.tma.romanova.data.feature.playlist.memory

import com.tma.romanova.core.playlistsMemoryStorageQualifier
import com.tma.romanova.data.BuildConfig
import com.tma.romanova.data.data_source.memory.BaseMemoryRequest
import com.tma.romanova.data.data_source.memory.MemoryResult
import com.tma.romanova.data.data_source.memory.MemoryStorage
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPlaylistMemoryRequest : BaseMemoryRequest<Playlist>,
    KoinComponent {

    override val memoryStorage: MemoryStorage<Playlist> by inject(qualifier = playlistsMemoryStorageQualifier)

    override fun makeRequest(): MemoryResult<Playlist> {
        return memoryStorage.getElement(id = BuildConfig.PLAYLIST_ID)
    }
}