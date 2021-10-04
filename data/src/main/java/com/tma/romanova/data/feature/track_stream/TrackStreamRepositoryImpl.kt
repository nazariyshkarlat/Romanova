package com.tma.romanova.data.feature.track_stream

import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.feature.track_stream.data_source.StreamDataSourceImpl
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.track_stream.*
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_BACK_MS
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_UP_MS
import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result
import com.tma.romanova.domain.result.map
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class TrackStreamRepositoryImpl(
    private val dataSourceProvider: DataSourceProvider,
    private val httpClient: HttpClient
): TrackStreamRepository {

    override val playingEvent: Flow<PlayingEvent>
    get() = streamPlayingEvent.flowOn(Dispatchers.Main)

    override val currentPlayingTrackId: StreamActionResult<Int>
        get() = stream.ifInitialized { trackId }

    private var stream: Stream? = null

    override val currentTrackPlayTime: StreamActionResult<Flow<Long>>
    get() = stream.ifInitialized { currentPlayMsTimeFlow.flowOn(Dispatchers.Main) }

    override val duration: StreamActionResult<Result<Long>>
    get() = stream.ifInitialized{ durationMs }

    override fun preparePlaylist(track: Track, withPlaying: Boolean) =
        flow {
            val result: Result<Stream> = when (dataSourceProvider.sourceType) {
                DataSourceType.Cache -> TODO()
                DataSourceType.Network -> {
                    StreamDataSourceImpl(
                        requestUrl = track.streamUrl,
                        trackId = track.id,
                        httpClient = httpClient,
                        dataSourceProvider = dataSourceProvider
                    ).getStream()
                }
                DataSourceType.Memory -> TODO()
            }
            if(result is Result.Success){
                stream = result.data
                result.data.prepareAudio(playWhenPrepared = withPlaying).collect {
                    emit(it)
                }
            }else{
                emit(result.map{
                    Unit
                })
            }
    }.flowOn(Dispatchers.Main)

    override fun playTrack() = stream.ifInitialized {
        playAudio().flowOn(Dispatchers.Main)
    }

    override fun pauseTrack() = stream.ifInitialized {  pauseAudio().flowOn(Dispatchers.Main) }

    override fun moveTimeBack() = stream.ifInitialized {
        seekTo(
            ((currentPlayMsTime as Result.Success).data - TIME_UP_MS).coerceIn(
                minimumValue = 0,
                maximumValue = ((durationMs as Result.Success).data)
            )
        ).flowOn(Dispatchers.Main)
    }

    override fun moveTimeUp() = stream.ifInitialized {
        seekTo(
            ((currentPlayMsTime as Result.Success).data + TIME_BACK_MS).coerceIn(
                minimumValue = 0,
                maximumValue = ((durationMs as Result.Success).data)
            )
        ).flowOn(Dispatchers.Main)
    }

    override fun moveToPosition(playedPercent: Float) = stream.ifInitialized{
        val newPosition = ((durationMs as Result.Success).data * playedPercent).toLong()
        return@ifInitialized seekTo(
            (newPosition).coerceIn(
                minimumValue = 0L,
                maximumValue = ((durationMs as Result.Success).data)
            )
        ).map {
            it.map {
                newPosition
            }
        }.flowOn(Dispatchers.Main)
    }

    override fun closeStream() {
        stream.ifInitialized {
            closeStream()
        }
    }
}