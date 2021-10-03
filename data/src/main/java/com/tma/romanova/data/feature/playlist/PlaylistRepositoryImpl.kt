package com.tma.romanova.data.feature.playlist

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.data_source.memory.result
import com.tma.romanova.data.data_source.memory.toResult
import com.tma.romanova.data.data_source.network.toResult
import com.tma.romanova.data.feature.playlist.api.GetPlaylistNetworkRequest
import com.tma.romanova.data.feature.playlist.memory.GetPlaylistMemoryRequest
import com.tma.romanova.data.feature.playlist.memory.SavePlaylistMemoryRequest
import com.tma.romanova.data.feature.track_navigation.memory.GetTrackMemoryRequest
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.data.feature.playlist.entity.TrackEntity
import com.tma.romanova.data.feature.playlist.entity.domain
import com.tma.romanova.data.feature.waveform.*
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result
import io.ktor.client.*
import java.lang.UnsupportedOperationException

class PlaylistRepositoryImpl(
    private val playlistDataSource: DataSource<PlaylistEntity, Playlist, Playlist>,
    private val playlistDataSourceProvider: DataSourceProvider,
    private val playlistDataStore: DataStore<PlaylistEntity, Playlist, Playlist>,
    private val playlistDataStoreProvider: DataSourceProvider,
    private val trackDataSource: DataSource<TrackEntity, Track, Track>,
    private val trackDataSourceProvider: DataSourceProvider,
    private val httpClient: HttpClient
) : PlaylistRepository {
    override suspend fun getPlaylist(): Result<Playlist> =
        when(playlistDataSourceProvider.sourceType){
            DataSourceType.Cache -> {
                throw UnsupportedOperationException()
            }
            DataSourceType.Network -> playlistDataSource.getFromServer(
                GetPlaylistNetworkRequest(httpClient = httpClient)
            ).toResult(PlaylistEntity::domain)
            DataSourceType.Memory -> {
                playlistDataSource.getFromMemory(
                    GetPlaylistMemoryRequest()
                ).result
            }
        }
    override suspend fun getTrack(trackId: Int): Result<Track> =
        when(trackDataSourceProvider.sourceType){
            DataSourceType.Cache -> {
                throw UnsupportedOperationException()
            }
            DataSourceType.Network -> {
                throw UnsupportedOperationException()
            }
            DataSourceType.Memory -> {
                trackDataSource.getFromMemory(
                    GetTrackMemoryRequest(trackId = trackId)
                ).result
            }
        }

    override suspend fun savePlaylist(playlist: Playlist) {
        when (playlistDataStoreProvider.sourceType) {
            DataSourceType.Memory -> {
                playlistDataStore.saveToMemory(
                    SavePlaylistMemoryRequest(
                        playlist = playlist
                    )
                )
            }
            DataSourceType.Cache -> {
                throw UnsupportedOperationException()
            }
            DataSourceType.Network -> {
                throw UnsupportedOperationException()
            }
        }
    }

    override suspend fun getWaveFormValues(url: String, partsCount: Int): Result<List<Float>> {
        (BitmapDownloaderImpl(url = url).download(compressionFactor = 0.2F) as? ImageDownloadResult.Success)?.let { result ->
            return Result.Success(
                    data = WaveformBitmapProcessorImpl().process(
                        bitmap = result.bitmap,
                        partsCount = partsCount
                    ),
                dataSourceType = DataSourceType.Network
            )
        } ?: run{
            return Result.NetworkError
        }
    }
}