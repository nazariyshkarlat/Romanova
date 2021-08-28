package com.tma.romanova.data.feature.playlist.data_source.get_playlist.cache

import com.tma.romanova.data.data_source.cache.BaseCacheRequest
import com.tma.romanova.data.data_source.cache.CacheResult
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity

class GetPlaylistCacheRequest : BaseCacheRequest<PlaylistEntity> {
    override suspend fun makeRequest(): CacheResult<PlaylistEntity> {
        TODO("Not yet implemented")
    }
}