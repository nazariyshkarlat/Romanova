package com.tma.romanova.data.data_source

import com.tma.romanova.data.data_source.cache.BaseCacheRequest
import com.tma.romanova.data.data_source.memory.BaseMemoryRequest
import com.tma.romanova.data.data_source.network.BaseNetworkRequest

abstract class DataStore<ToNetwork: Any, ToCache: Any, ToMemory: Any> {
    suspend fun saveToCache(request: BaseCacheRequest<ToMemory>) = request.makeRequest()
    fun saveToMemory(request: BaseMemoryRequest<List<ToCache>>) = request.makeRequest()
    suspend fun saveToServer(request: BaseNetworkRequest<ToNetwork>) = request.makeRequest()

}