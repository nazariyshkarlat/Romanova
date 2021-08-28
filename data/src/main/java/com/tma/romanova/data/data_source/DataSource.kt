package com.tma.romanova.data.data_source

import com.tma.romanova.data.data_source.cache.BaseCacheRequest
import com.tma.romanova.data.data_source.memory.BaseMemoryRequest
import com.tma.romanova.data.data_source.network.BaseNetworkRequest

abstract class DataSource<FromNetwork: Any, FromCache: Any, FromMemory: Any> {
    suspend fun getFromCache(request: BaseCacheRequest<FromCache>) = request.makeRequest()
    fun getFromMemory(request: BaseMemoryRequest<FromMemory>) = request.makeRequest()
    suspend fun getFromServer(request: BaseNetworkRequest<FromNetwork>) = request.makeRequest()

}