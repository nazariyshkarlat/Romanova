package com.tma.romanova.data.feature.track_stream

import com.tma.romanova.data.data_source.DataSourceProvider
import com.tma.romanova.data.feature.track_stream.data_source.StreamDataSourceImpl
import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.track_stream.*
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_BACK_MS
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository.Companion.TIME_UP_MS
import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result
import com.tma.romanova.domain.result.map
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlin.math.absoluteValue

class TrackStreamRepositoryImpl(
    private val dataSourceProvider: DataSourceProvider,
    private val httpClient: HttpClient
): TrackStreamRepository {

    override val playingEvent: Flow<PlayingEvent>
    get() = streamPlayingEvent.flowOn(Dispatchers.Main)

    override val currentPlayingTrackId: StreamActionResult<Int>
        get() = stream.ifInitialized { trackId }

    override val lastPlayingTrack: Track?
    get() = _lastPlayingTrack

    private var _lastPlayingTrack: Track? = null

    private var stream: Stream? = null

    override val currentTrackPlayTime: Flow<Long>
    get() = currentPlayMsTimeFlow.onEach {
        if(lastPlayingTrack?.playingState?.positionMs
                ?.minus(it)
                ?.div(1000L)
                ?.absoluteValue
                ?.compareTo(1L) != -1
        ) {
            _lastPlayingTrack = _lastPlayingTrack?.copy(
                playingState = PlayingState.IsPlaying(
                    currentPositionMs = it
                )
            )
        }
    }.flowOn(Dispatchers.Main)

    override val duration: StreamActionResult<Result<Long>>
    get() = stream.ifInitialized{ durationMs }

    override fun preparePlaylist(track: Track, withPlaying: Boolean): Flow<Result<Unit>> {
        _lastPlayingTrack = track
        return flow {
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
            if (result is Result.Success) {
                stream = result.data
                println("prepare audio")
                result.data.prepareAudio(
                    playWhenPrepared = withPlaying,
                    startPositionInMillis = track.playingState.positionMs?.coerceIn(
                        0L,
                        track.duration.toLong()
                    ) ?: 0L
                ).collect {
                    emit(it)
                }
            } else {
                emit(result.map {
                    Unit
                })
            }
        }.flowOn(Dispatchers.Main)
    }

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