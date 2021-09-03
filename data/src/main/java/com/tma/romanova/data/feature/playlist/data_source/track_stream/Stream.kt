package com.tma.romanova.data.feature.playlist.data_source.track_stream
import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface Stream {
    val currentPlayMsTimeFlow: Flow<Int>
    val currentPlayMsTime: Result<Int>
    val durationMs: Result<Int>

    fun prepareAudio(): Flow<Result<Unit>>
    fun playAudio(): Flow<Result<Unit>>
    fun pauseAudio(): Flow<Result<Unit>>
    fun seekTo(ms: Int): Flow<Result<Unit>>
    fun endStream(): Flow<Result<Unit>>
}