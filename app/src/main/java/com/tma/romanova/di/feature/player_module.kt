package com.tma.romanova.di.feature

import com.tma.romanova.core.*
import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.data_source.memory.MemoryStorage
import com.tma.romanova.data.feature.playlist.data_source.get_track.data_source.TrackDataSource
import com.tma.romanova.data.feature.playlist.data_source.get_track.data_source.TrackDataSourceProvider
import com.tma.romanova.data.feature.playlist.data_source.get_track.data_store.TrackDataStore
import com.tma.romanova.data.feature.playlist.data_source.get_track.data_store.TrackDataStoreProvider
import com.tma.romanova.data.feature.playlist.data_source.get_track.memory.TracksMemoryStorage
import com.tma.romanova.data.feature.playlist.data_source.track_stream.StreamDataSource
import com.tma.romanova.data.feature.playlist.data_source.track_stream.StreamDataSourceImpl
import com.tma.romanova.data.feature.playlist.data_source.track_stream.StreamDataSourceProvider
import com.tma.romanova.data.feature.track_stream.TrackStreamRepositoryImpl
import com.tma.romanova.domain.feature.playlist.use_case.TrackInteractor
import com.tma.romanova.domain.feature.playlist.use_case.TrackInteractorImpl
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractor
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractorImpl
import com.tma.romanova.presentation.feature.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val playerModule = module{

    viewModel { (trackId: Int) ->
        PlayerViewModel(trackId, get())
    }

    factory(qualifier = playerDataSourceQualifier) { (trackId: Int) ->
        StreamDataSourceImpl(
            trackId
        )
    } bind StreamDataSource::class

    factory { (trackId: Int) ->
        TrackStreamRepositoryImpl(
            dataSource = get(qualifier = playerDataSourceQualifier){
                parametersOf(trackId)
            },
            dataSourceProvider = get(
                qualifier = playerDataSourceProviderQualifier
            )
        )
    } bind TrackStreamRepository::class

    factory {(trackId: Int) ->
        TrackStreamInteractorImpl(
            get{
                parametersOf(trackId)
            }
        )
    } bind TrackStreamInteractor::class

    factory(qualifier = playerDataSourceProviderQualifier) {
        StreamDataSourceProvider()
    } bind DataSourceProvider::class

    single(qualifier = trackDataSourceQualifier) {
        TrackDataSource()
    } bind DataSource::class

    single(qualifier = trackDataStoreQualifier) {
        TrackDataStore()
    } bind DataStore::class

    single(qualifier = trackDataStoreProviderQualifier) {
        TrackDataStoreProvider(get(qualifier = trackDataStoreQualifier))
    } bind DataSourceProvider::class

    single(qualifier = trackDataSourceProviderQualifier) {
        TrackDataSourceProvider(get(qualifier = trackDataSourceQualifier))
    } bind DataSourceProvider::class

    single(qualifier = tracksMemoryStorageQualifier){
        TracksMemoryStorage()
    } bind MemoryStorage::class

    single {
        TrackInteractorImpl(get())
    } bind TrackInteractor::class
}