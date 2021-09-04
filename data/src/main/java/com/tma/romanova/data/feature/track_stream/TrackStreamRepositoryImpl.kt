package com.tma.romanova.data.feature.track_stream

import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.feature.playlist.data_source.track_stream.StreamDataSource
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_BACK_MS
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_UP_MS
import com.tma.romanova.domain.result.DataSourceType
import kotlinx.coroutines.flow.Flow
import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.lang.UnsupportedOperationException

class TrackStreamRepositoryImpl(
    private val dataSource: StreamDataSource,
    private val dataSourceProvider: DataSourceProvider
): TrackStreamRepository {
    override val currentTrackPlayTime: Flow<Int> = dataSource.stream.currentPlayMsTimeFlow

    override fun prepareTrack(trackId: Int, withPlaying: Boolean) = when (dataSourceProvider.dataSourceType){
        DataSourceType.Cache -> throw UnsupportedOperationException()
        DataSourceType.Network -> {
            dataSource.stream.prepareAudio().onEach {
                if(withPlaying) {
                    if(it is Result.Success) {
                        playTrack().collect {

                        }
                    }
                }
            }
        }
        DataSourceType.Memory -> throw UnsupportedOperationException()
    }

    override fun playTrack(): Flow<Result<Unit>> = when (dataSourceProvider.dataSourceType){
        DataSourceType.Cache -> throw UnsupportedOperationException()
        DataSourceType.Network -> {
            dataSource.stream.playAudio()
        }
        DataSourceType.Memory -> throw UnsupportedOperationException()
    }

    override fun pauseTrack(): Flow<Result<Unit>> = dataSource.stream.pauseAudio()

    override fun moveTimeBack(): Flow<Result<Unit>> = when (dataSourceProvider.dataSourceType){
        DataSourceType.Cache -> throw UnsupportedOperationException()
        DataSourceType.Network -> {
            dataSource.stream.seekTo(
                ((dataSource.stream.currentPlayMsTime as Result.Success).data - TIME_UP_MS).coerceIn(
                    minimumValue = 0,
                    maximumValue = ((dataSource.stream.durationMs as Result.Success).data)
                )
            )
        }
        DataSourceType.Memory -> throw UnsupportedOperationException()
    }

    override fun moveTimeUp(): Flow<Result<Unit>> = when (dataSourceProvider.dataSourceType){
        DataSourceType.Cache -> throw UnsupportedOperationException()
        DataSourceType.Network -> {
            dataSource.stream.seekTo(
                ((dataSource.stream.currentPlayMsTime as Result.Success).data + TIME_BACK_MS).coerceIn(
                    minimumValue = 0,
                    maximumValue = ((dataSource.stream.durationMs as Result.Success).data)
                )
            )
        }
        DataSourceType.Memory -> throw UnsupportedOperationException()
    }
}