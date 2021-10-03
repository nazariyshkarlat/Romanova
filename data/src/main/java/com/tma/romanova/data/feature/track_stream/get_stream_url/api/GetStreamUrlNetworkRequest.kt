package com.tma.romanova.data.feature.track_stream.get_stream_url.api

import com.tma.romanova.data.BuildConfig
import com.tma.romanova.data.data_source.network.BaseNetworkRequest
import com.tma.romanova.data.extensions.handleNetworkRequest
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GetStreamUrlNetworkRequest(httpClient: HttpClient, private val requestUrl: String) :
    BaseNetworkRequest<UrlResult>(httpClient) {

    override suspend fun makeRequest() =
        suspend {
            httpClient.get<UrlResult>(requestUrl) {
                parameter("client_id", BuildConfig.CLIENT_ID)
            }
        }.handleNetworkRequest()

}

@Serializable
data class UrlResult(
    @SerialName("url")
    val url: String
)