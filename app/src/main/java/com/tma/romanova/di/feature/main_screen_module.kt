package com.tma.romanova.di.feature

import com.tma.romanova.core.*
import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.data_source.memory.MemoryStorage
import com.tma.romanova.data.feature.playlist.PlaylistRepositoryImpl
import com.tma.romanova.data.feature.playlist.data_source.get_playlist.PlaylistDataSource
import com.tma.romanova.data.feature.playlist.data_source.get_playlist.PlaylistDataSourceProvider
import com.tma.romanova.data.feature.playlist.data_source.get_track.data_source.TrackDataSource
import com.tma.romanova.data.feature.playlist.data_source.get_track.data_source.TrackDataSourceProvider
import com.tma.romanova.data.feature.playlist.data_source.get_track.data_store.TrackDataStore
import com.tma.romanova.data.feature.playlist.data_source.get_track.data_store.TrackDataStoreProvider
import com.tma.romanova.data.feature.playlist.data_source.get_track.memory.TracksMemoryStorage
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import com.tma.romanova.domain.feature.playlist.use_case.GetPlaylist
import com.tma.romanova.domain.feature.playlist.use_case.GetPlaylistImpl
import com.tma.romanova.presentation.feature.main.view_model.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val mainScreenModule = module {
    viewModel {
        MainScreenViewModel(
            getPlaylist = get(),
            trackInteractor = get()
        )
    }
    single {
        GetPlaylistImpl(
            playlistRepository = get()
        )
    } bind GetPlaylist::class

    single {
        PlaylistRepositoryImpl(
            httpClient = get(),
            playlistDataSource = get(qualifier = playlistDataSourceQualifier),
            playlistDataSourceProvider = get(qualifier = playlistDataSourceProviderQualifier),
            trackDataSource = get(qualifier = trackDataSourceQualifier),
            trackDataStore = get(qualifier = trackDataStoreQualifier),
            trackDataSourceProvider = get(qualifier = trackDataSourceProviderQualifier),
            trackDataStoreProvider = get(qualifier = trackDataStoreProviderQualifier)
        )
    } bind PlaylistRepository::class

    single(qualifier = playlistDataSourceProviderQualifier) {
        PlaylistDataSourceProvider(get(playlistDataSourceQualifier))
    } bind DataSourceProvider::class

    single(qualifier = playlistDataSourceQualifier) {
        PlaylistDataSource()
    } bind DataSource::class
}