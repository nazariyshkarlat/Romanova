package com.tma.romanova.data.feature.playlist

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.data_source.memory.toResult
import com.tma.romanova.data.data_source.network.toResult
import com.tma.romanova.data.feature.playlist.data_source.get_playlist.api.GetPlaylistNetworkRequest
import com.tma.romanova.data.feature.playlist.data_source.get_track.memory.GetTrackMemoryRequest
import com.tma.romanova.data.feature.playlist.data_source.get_track.memory.SaveTrackMemoryRequest
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.data.feature.playlist.entity.TrackEntity
import com.tma.romanova.data.feature.playlist.entity.domain
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result
import io.ktor.client.*
import java.lang.UnsupportedOperationException

class PlaylistRepositoryImpl(
    private val playlistDataSource: DataSource<PlaylistEntity, PlaylistEntity, PlaylistEntity>,
    private val playlistDataSourceProvider: DataSourceProvider,
    private val trackDataSourceProvider: DataSourceProvider,
    private val trackDataStoreProvider: DataSourceProvider,
    private val trackDataSource: DataSource<TrackEntity, Track, Track>,
    private val trackDataStore: DataStore<TrackEntity, Track, Track>,
    private val httpClient: HttpClient
) : PlaylistRepository {
    override suspend fun getPlaylist(): Result<Playlist> =
        when(playlistDataSourceProvider.dataSourceType){
            DataSourceType.Cache -> {
                throw UnsupportedOperationException()
            }
            DataSourceType.Network -> playlistDataSource.getFromServer(
                GetPlaylistNetworkRequest(httpClient = httpClient)
            ).toResult {
                it.domain
            }
            DataSourceType.Memory -> {
                throw UnsupportedOperationException()
            }
        }

    override suspend fun getTrack(trackId: Int): Result<Track> =
        when(trackDataSourceProvider.dataSourceType){
            DataSourceType.Cache -> {
                throw UnsupportedOperationException()
            }
            DataSourceType.Network -> {
                throw UnsupportedOperationException()
            }
            DataSourceType.Memory -> {
                trackDataSource.getFromMemory(
                    GetTrackMemoryRequest(trackId = trackId)
                ).toResult {
                    it
                }
            }
        }

    override suspend fun saveTrack(track: Track) {
        when (trackDataStoreProvider.dataSourceType) {
            DataSourceType.Memory -> {
                trackDataStore.saveToMemory(
                    SaveTrackMemoryRequest(
                        track = track
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
}