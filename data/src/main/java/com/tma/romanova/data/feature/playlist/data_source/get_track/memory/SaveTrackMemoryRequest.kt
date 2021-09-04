package com.tma.romanova.data.feature.playlist.data_source.get_track.memory

import com.tma.romanova.core.tracksMemoryStorageQualifier
import com.tma.romanova.data.data_source.memory.BaseMemoryRequest
import com.tma.romanova.data.data_source.memory.MemoryResult
import com.tma.romanova.data.data_source.memory.MemoryStorage
import com.tma.romanova.domain.feature.playlist.entity.Track
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SaveTrackMemoryRequest(private val track: Track) : BaseMemoryRequest<List<Track>>,
    KoinComponent {

    override val memoryStorage: MemoryStorage<Track> by inject(qualifier = tracksMemoryStorageQualifier)

    override fun makeRequest(): MemoryResult<List<Track>> {
        return memoryStorage.saveData(id = track.id.toString(), data = track)
    }
}