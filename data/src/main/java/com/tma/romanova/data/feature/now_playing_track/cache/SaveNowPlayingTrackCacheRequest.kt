package com.tma.romanova.data.feature.now_playing_track.cache

import com.tma.romanova.core.nowPlayingTrackCacheStorageQualifier
import com.tma.romanova.data.data_source.cache.BaseCacheRequest
import com.tma.romanova.data.data_source.cache.CacheResult
import com.tma.romanova.data.data_source.cache.CacheStorage
import com.tma.romanova.data.data_source.memory.BaseMemoryRequest
import com.tma.romanova.data.data_source.memory.MemoryResult
import com.tma.romanova.data.data_source.memory.MemoryStorage
import com.tma.romanova.domain.feature.playlist.entity.Track
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SaveNowPlayingTrackCacheRequest(private val track: Track) : BaseCacheRequest<Track>,
    KoinComponent {

    override val cacheStorage: CacheStorage<Track> by inject(qualifier = nowPlayingTrackCacheStorageQualifier)

    override suspend fun makeRequest(): CacheResult<Track> {
        println("save")
        return cacheStorage.setSingleElement(
            data = track,
            id = track.id.toString()
        )
    }
}