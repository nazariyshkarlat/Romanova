package com.tma.romanova.di.feature

import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.feature.playlist.data_source.track_stream.StreamDataSource
import com.tma.romanova.data.feature.playlist.data_source.track_stream.StreamDataSourceImpl
import com.tma.romanova.data.feature.playlist.data_source.track_stream.StreamDataSourceProvider
import com.tma.romanova.data.feature.track_stream.TrackStreamRepositoryImpl
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractor
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractorImpl
import com.tma.romanova.presentation.feature.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val playerDataSourceProviderQualifier = named("player_data_source_provider")

val playerModule = module{

    viewModel { (trackId: Int) ->
        PlayerViewModel(trackId)
    }

    factory { (trackId: Int) ->
        StreamDataSourceImpl(
            trackId
        )
    } bind StreamDataSource::class

    factory { (trackId: Int) ->
        TrackStreamRepositoryImpl(
            dataSource = get{
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
}