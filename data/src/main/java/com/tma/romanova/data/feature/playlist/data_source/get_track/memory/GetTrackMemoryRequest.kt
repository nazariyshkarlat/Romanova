package com.tma.romanova.data.feature.playlist.data_source.get_track.memory

import com.tma.romanova.data.data_source.memory.BaseMemoryRequest
import com.tma.romanova.data.data_source.memory.MemoryResult
import com.tma.romanova.data.data_source.memory.MemoryStorage
import com.tma.romanova.domain.feature.playlist.entity.Track
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetTrackMemoryRequest(private val trackId: Int) : BaseMemoryRequest<Track>,
    KoinComponent {

    override val memoryStorage: MemoryStorage<Track> by inject()

    override fun makeRequest(): MemoryResult<Track> {
        return memoryStorage.getElement(trackId.toString())
    }
}