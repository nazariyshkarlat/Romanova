package com.tma.romanova.data.feature.track_navigation.memory

import com.tma.romanova.core.playlistsMemoryStorageQualifier
import com.tma.romanova.data.BuildConfig
import com.tma.romanova.data.data_source.memory.*
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetTrackMemoryRequest(private val trackId: Int) : BaseMemoryRequest<Track>,
    KoinComponent {

    override val memoryStorage: MemoryStorage<Playlist> by inject(qualifier = playlistsMemoryStorageQualifier)

    override fun makeRequest(): MemoryResult<Track> {
        return memoryStorage.getElement(id = BuildConfig.PLAYLIST_ID).transform {
            it.tracks.first { it.id == trackId }
        }
    }
}