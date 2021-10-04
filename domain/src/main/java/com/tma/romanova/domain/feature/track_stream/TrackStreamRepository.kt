package com.tma.romanova.domain.feature.track_stream

import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface TrackStreamRepository {

    companion object{
        const val TIME_UP_MS = 15000
        const val TIME_BACK_MS = 15000
    }

    val playingEvent: Flow<PlayingEvent>
    val currentTrackPlayTime: Flow<Long>
    val duration: StreamActionResult<Result<Long>>
    val currentPlayingTrackId: StreamActionResult<Int>
    val lastPlayingTrack: Track?

    fun preparePlaylist(track: Track, withPlaying: Boolean): Flow<Result<Unit>>
    fun playTrack(): StreamActionResult<Flow<Result<Unit>>>
    fun pauseTrack(): StreamActionResult<Flow<Result<Unit>>>
    fun moveToPosition(playedPercent: Float): StreamActionResult<Flow<Result<Long>>>
    fun moveTimeBack(): StreamActionResult<Flow<Result<Unit>>>
    fun moveTimeUp(): StreamActionResult<Flow<Result<Unit>>>
    fun closeStream()
}

sealed class StreamActionResult<out T>{

    val initializedOrNull: T?
    get() = (this as? IsInitialized)?.result

    val notNull
    get() = initializedOrNull != null

    object StreamNotInitialized: StreamActionResult<Nothing>()
    data class IsInitialized<T>(val result: T) : StreamActionResult<T>()
}

fun <T, R>StreamActionResult<T>.ifInitialized(action: T.() -> R): StreamActionResult<R> = when(this){
    is StreamActionResult.IsInitialized -> StreamActionResult.IsInitialized(
        result = action.invoke(this.result)
    )
    StreamActionResult.StreamNotInitialized -> StreamActionResult.StreamNotInitialized
}