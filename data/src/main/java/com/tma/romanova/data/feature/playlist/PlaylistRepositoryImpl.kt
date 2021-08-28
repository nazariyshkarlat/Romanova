package com.tma.romanova.data.feature.playlist

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.network.toResult
import com.tma.romanova.data.feature.playlist.data_source.get_playlist.api.GetPlaylistNetworkRequest
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.data.feature.playlist.entity.toDomain
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result
import io.ktor.client.*
import org.koin.androidx.compose.get
import java.lang.UnsupportedOperationException

class PlaylistRepositoryImpl(
    private val playlistDataSource: DataSource<PlaylistEntity, PlaylistEntity, PlaylistEntity>,
    private val playlistDataSourceProvider: DataSourceProvider,
    private val httpClient: HttpClient
) : PlaylistRepository{
    override suspend fun getPlaylist(): Result<Playlist> =
        when(playlistDataSourceProvider.dataSourceType){
            DataSourceType.Cache -> {
                throw UnsupportedOperationException()
            }
            DataSourceType.Network -> playlistDataSource.getFromServer(
                GetPlaylistNetworkRequest(httpClient = httpClient)
            ).toResult {
                it.toDomain()
            }
            DataSourceType.Memory -> {
                throw UnsupportedOperationException()
            }
        }

}