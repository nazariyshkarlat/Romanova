package com.tma.romanova.di.feature

import com.tma.romanova.core.*
import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.feature.playlist.PlaylistRepositoryImpl
import com.tma.romanova.data.feature.playlist.data_source.PlaylistDataSource
import com.tma.romanova.data.feature.playlist.data_source.PlaylistDataSourceProvider
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import com.tma.romanova.domain.feature.playlist.use_case.PlaylistInteractor
import com.tma.romanova.domain.feature.playlist.use_case.PlaylistInteractorImpl
import com.tma.romanova.presentation.feature.main.view_model.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val mainScreenModule = module {
    viewModel {
        MainScreenViewModel(
            playlistInteractor = get(),
            nowPlayingTrackInteractor = get(),
            trackStreamInteractor = get()
        )
    }
    single {
        PlaylistInteractorImpl(
            playlistRepository = get()
        )
    } bind PlaylistInteractor::class

    single {
        PlaylistRepositoryImpl(
            httpClient = get(),
            playlistDataSource = get(qualifier = playlistDataSourceQualifier),
            playlistDataSourceProvider = get(qualifier = playlistDataSourceProviderQualifier),
            trackDataSource = get(qualifier = trackDataSourceQualifier),
            trackDataSourceProvider = get(qualifier = trackDataSourceProviderQualifier),
            playlistDataStore = get(qualifier = playlistDataStoreQualifier),
            playlistDataStoreProvider = get(qualifier = playlistDataStoreProviderQualifier)
        )
    } bind PlaylistRepository::class

    single(qualifier = playlistDataSourceProviderQualifier) {
        PlaylistDataSourceProvider(get(playlistDataSourceQualifier))
    } bind DataSourceProvider::class

    single(qualifier = playlistDataSourceQualifier) {
        PlaylistDataSource()
    } bind DataSource::class

}