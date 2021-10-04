package com.tma.romanova.di.feature

import com.tma.romanova.core.*
import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.data_source.memory.MemoryStorage
import com.tma.romanova.data.feature.playlist.memory.PlaylistsMemoryStorage
import com.tma.romanova.data.feature.track_navigation.data_source.TrackDataSource
import com.tma.romanova.data.feature.track_navigation.data_source.TrackDataSourceProvider
import com.tma.romanova.data.feature.playlist.data_store.PlaylistDataStore
import com.tma.romanova.data.feature.playlist.data_store.PlaylistDataStoreProvider
import com.tma.romanova.data.feature.track_stream.data_source.StreamDataSourceProvider
import com.tma.romanova.data.feature.track_stream.TrackStreamRepositoryImpl
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.playlist.use_case.TrackInteractor
import com.tma.romanova.domain.feature.playlist.use_case.TrackInteractorImpl
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractor
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractorImpl
import com.tma.romanova.presentation.feature.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val playerModule = module{

    viewModel { (trackId: Int) ->
        PlayerViewModel(trackId, get(), get(), get(), get())
    }

    single {
        TrackStreamRepositoryImpl(
            dataSourceProvider = get(
                qualifier = playerDataSourceProviderQualifier
            ),
            httpClient = get()
        )
    } bind TrackStreamRepository::class

    single {
        TrackStreamInteractorImpl(
            trackStreamRepository = get(),
            nowPlayingTrackRepository = get(),
            playlistRepository = get()
        )
    } bind TrackStreamInteractor::class

    factory(qualifier = playerDataSourceProviderQualifier) {
        StreamDataSourceProvider()
    } bind DataSourceProvider::class

    single(qualifier = trackDataSourceQualifier) {
        TrackDataSource()
    } bind DataSource::class

    single(qualifier = trackDataSourceProviderQualifier) {
        TrackDataSourceProvider(get(qualifier = trackDataSourceQualifier))
    } bind DataSourceProvider::class

    single(qualifier = playlistsMemoryStorageQualifier){
        PlaylistsMemoryStorage()
    } bind MemoryStorage::class

    single(qualifier = playlistDataStoreQualifier) {
        PlaylistDataStore()
    } bind DataStore::class

    single(qualifier = playlistDataStoreProviderQualifier) {
        PlaylistDataStoreProvider(get(qualifier = playlistDataStoreQualifier))
    } bind DataSourceProvider::class

    single {
        TrackInteractorImpl(get())
    } bind TrackInteractor::class
}