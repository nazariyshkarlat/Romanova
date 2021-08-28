package com.tma.romanova.data.data_source.cache


interface BaseCacheRequest<T: Any> {

    suspend fun makeRequest(): CacheResult<T>
}