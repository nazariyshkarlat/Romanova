package com.tma.romanova.di.feature

import com.tma.romanova.core.*
import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.data_source.cache.CacheStorage
import com.tma.romanova.data.data_source.memory.MemoryStorage
import com.tma.romanova.data.feature.now_playing_track.NowPlayingTrackRepositoryImpl
import com.tma.romanova.data.feature.now_playing_track.cache.NowPlayingTrackCacheStorage
import com.tma.romanova.data.feature.now_playing_track.data_source.NowPlayingTrackDataSource
import com.tma.romanova.data.feature.now_playing_track.data_source.NowPlayingTrackDataSourceProvider
import com.tma.romanova.data.feature.now_playing_track.data_store.NowPlayingTrackDataStore
import com.tma.romanova.data.feature.now_playing_track.data_store.NowPlayingTrackDataStoreProvider
import com.tma.romanova.data.feature.playlist.memory.PlaylistsMemoryStorage
import com.tma.romanova.data.feature.track_navigation.data_source.TrackDataSource
import com.tma.romanova.data.feature.track_navigation.data_source.TrackDataSourceProvider
import com.tma.romanova.data.feature.track_stream.data_source.StreamDataSourceProvider
import com.tma.romanova.domain.feature.now_playing_track.NowPlayingTrackRepository
import com.tma.romanova.domain.feature.now_playing_track.use_case.NowPlayingTrackInteractor
import com.tma.romanova.domain.feature.now_playing_track.use_case.NowPlayingTrackInteractorImpl
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractor
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractorImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val nowPlayingModule = module{
    single {
        NowPlayingTrackRepositoryImpl(
            nowPlayingTrackDataSource = get(qualifier = nowPlayingTrackDataSourceQualifier),
            nowPlayingTrackDataStore = get(qualifier = nowPlayingTrackDataStoreQualifier),
            nowPlayingTrackDataSourceProvider = get(qualifier = nowPlayingTrackDataSourceProviderQualifier),
            nowPlayingTrackDataStoreProvider = get(qualifier = nowPlayingTrackDataStoreProviderQualifier)
        )
    } bind NowPlayingTrackRepository::class

    single(qualifier = nowPlayingTrackDataSourceQualifier) {
        NowPlayingTrackDataSource()
    } bind DataSource::class

    single(qualifier = nowPlayingTrackDataSourceProviderQualifier) {
        NowPlayingTrackDataSourceProvider(get(qualifier = nowPlayingTrackDataSourceQualifier))
    } bind DataSourceProvider::class

    single(qualifier = nowPlayingTrackCacheStorageQualifier){
        NowPlayingTrackCacheStorage()
    } bind CacheStorage::class

    single(qualifier = nowPlayingTrackDataStoreQualifier) {
        NowPlayingTrackDataStore()
    } bind DataStore::class

    single(qualifier = nowPlayingTrackDataStoreProviderQualifier) {
        NowPlayingTrackDataStoreProvider(get(qualifier = nowPlayingTrackDataStoreQualifier))
    } bind DataSourceProvider::class

    single {
        NowPlayingTrackInteractorImpl(get())
    } bind NowPlayingTrackInteractor::class
}