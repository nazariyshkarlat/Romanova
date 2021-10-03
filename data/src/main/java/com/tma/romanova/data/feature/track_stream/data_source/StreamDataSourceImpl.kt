package com.tma.romanova.data.feature.track_stream.data_source

import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.network.toResult
import com.tma.romanova.data.feature.track_stream.Stream
import com.tma.romanova.data.feature.track_stream.get_stream_url.api.GetStreamUrlNetworkRequest
import com.tma.romanova.data.feature.track_stream.network.NetworkStream
import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result
import io.ktor.client.*

class StreamDataSourceImpl(
    private val requestUrl: String,
    private val httpClient: HttpClient,
    private val dataSourceProvider: DataSourceProvider
    ): StreamDataSource {

    override suspend fun getStream(): Result<Stream> {
        when(dataSourceProvider.sourceType){
            DataSourceType.Cache -> TODO()
            DataSourceType.Network -> {
                return GetStreamUrlNetworkRequest(
                    httpClient = httpClient,
                    requestUrl = requestUrl
                ).makeRequest().toResult {
                    NetworkStream(
                        url = it.url
                    )
                }
            }
            DataSourceType.Memory -> TODO()
        }
    }
}