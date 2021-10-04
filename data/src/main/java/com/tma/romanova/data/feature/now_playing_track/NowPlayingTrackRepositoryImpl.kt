package com.tma.romanova.data.feature.now_playing_track

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.data_source.cache.result
import com.tma.romanova.data.data_source.cache.toResult
import com.tma.romanova.data.feature.now_playing_track.cache.GetNowPlayingTrackCacheRequest
import com.tma.romanova.data.feature.now_playing_track.cache.SaveNowPlayingTrackCacheRequest
import com.tma.romanova.data.feature.playlist.entity.TrackEntity
import com.tma.romanova.domain.feature.now_playing_track.NowPlayingTrackRepository
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result

class NowPlayingTrackRepositoryImpl(
    private val nowPlayingTrackDataSource: DataSource<TrackEntity, Track, Track>,
    private val nowPlayingTrackDataSourceProvider: DataSourceProvider,
    private val nowPlayingTrackDataStore: DataStore<TrackEntity, Track, Track>,
    private val nowPlayingTrackDataStoreProvider: DataSourceProvider,
    ) : NowPlayingTrackRepository {
    override suspend fun getNowPlayingTrack(): Result<Track> {
        return when(nowPlayingTrackDataSourceProvider.sourceType){
            DataSourceType.Cache -> {
                nowPlayingTrackDataSource.getFromCache(
                    GetNowPlayingTrackCacheRequest()
                ).result
            }
            DataSourceType.Network -> TODO()
            DataSourceType.Memory -> TODO()
        }
    }

    override suspend fun saveNowPlayingTrack(track: Track) {
        when(nowPlayingTrackDataStoreProvider.sourceType){
            DataSourceType.Cache -> {
                nowPlayingTrackDataStore.saveToCache(
                    SaveNowPlayingTrackCacheRequest(
                        track = track
                    )
                )
            }
            DataSourceType.Network -> TODO()
            DataSourceType.Memory -> TODO()
        }
    }
}