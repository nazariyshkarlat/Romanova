package com.tma.romanova.domain.feature.track_stream
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*

val _playingEvent = MutableSharedFlow<PlayingEvent>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
val streamPlayingEvent: Flow<PlayingEvent> by lazy {
    _playingEvent.asSharedFlow()
}


val _currentPlayMsTimeFlow = MutableSharedFlow<Long>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
val currentPlayMsTimeFlow: Flow<Long> by lazy {
    _currentPlayMsTimeFlow.asSharedFlow()
}

abstract class Stream(open val trackId: Int) {
    abstract val currentPlayMsTime: Result<Long>
    abstract val durationMs: Result<Long>

    abstract fun prepareAudio(
        playWhenPrepared: Boolean,
        startPositionInMillis: Long
    ): Flow<Result<Unit>>

    abstract fun playAudio(): Flow<Result<Unit>>
    abstract fun pauseAudio(): Flow<Result<Unit>>
    abstract fun seekTo(ms: Long): Flow<Result<Unit>>
    abstract fun closeStream()
}


fun <T>(Stream?).ifInitialized(action: Stream.() -> T): StreamActionResult<T> {
    return if (this == null) StreamActionResult.StreamNotInitialized
    else StreamActionResult.IsInitialized(
        result = action.invoke(this)
    )
}

sealed class PlayingEvent{
    object TrackEnd: PlayingEvent()
    data class TrackPlayingStart(val trackId: Int): PlayingEvent()
}