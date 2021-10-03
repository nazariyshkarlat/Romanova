package com.tma.romanova.data.data_source.cache

import com.tma.romanova.data.data_source.memory.MemoryStorage


interface BaseCacheRequest<T: Any> {

    val cacheStorage: CacheStorage<*>

    suspend fun makeRequest(): CacheResult<T>
}