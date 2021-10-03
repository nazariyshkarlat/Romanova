package com.tma.romanova.data.feature.track_stream
import com.tma.romanova.domain.feature.track_stream.StreamActionResult
import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface Stream {
    val currentPlayMsTimeFlow: Flow<Long>
    val currentPlayMsTime: Result<Long>
    val durationMs: Result<Long>

    fun prepareAudio(playWhenPrepared: Boolean): Flow<Result<Unit>>
    fun playAudio(): Flow<Result<Unit>>
    fun pauseAudio(): Flow<Result<Unit>>
    fun seekTo(ms: Long): Flow<Result<Unit>>
    fun closeStream()
}


fun <T>(Stream?).ifInitialized(action: Stream.() -> T): StreamActionResult<T> {
    return if (this == null) StreamActionResult.StreamNotInitialized
    else StreamActionResult.IsInitialized(
        result = action.invoke(this)
    )
}