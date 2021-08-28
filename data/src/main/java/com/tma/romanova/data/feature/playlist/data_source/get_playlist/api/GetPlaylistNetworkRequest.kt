package com.tma.romanova.data.feature.playlist.data_source.get_playlist.api

import com.tma.romanova.data.data_source.network.BaseNetworkRequest
import com.tma.romanova.data.extensions.handleNetworkRequest
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import io.ktor.client.request.*
import com.tma.romanova.data.BuildConfig
import io.ktor.client.*

class GetPlaylistNetworkRequest(httpClient: HttpClient) :
    BaseNetworkRequest<PlaylistEntity>(httpClient) {

    override suspend fun makeRequest() =
        suspend {
            httpClient.use {
                it.get<PlaylistEntity>("$baseUrl/users/${BuildConfig.USER_ID}/playlists/${BuildConfig.PLAYLIST_ID}/") {
                    parameter("client_id", BuildConfig.CLIENT_ID)
                }
            }
        }.handleNetworkRequest()

}