package com.tma.romanova.domain.feature.track_stream

import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface TrackStreamRepository {

    companion object{
        const val TIME_UP_MS = 15000
        const val TIME_BACK_MS = 15000
    }

    val currentTrackPlayTime: StreamActionResult<Flow<Long>>
    val duration: StreamActionResult<Result<Long>>

    fun prepareTrack(track: Track, withPlaying: Boolean): Flow<Result<Unit>>
    fun playTrack(): StreamActionResult<Flow<Result<Unit>>>
    fun pauseTrack(): StreamActionResult<Flow<Result<Unit>>>
    fun moveToPosition(playedPercent: Float): StreamActionResult<Flow<Result<Long>>>
    fun moveTimeBack(): StreamActionResult<Flow<Result<Unit>>>
    fun moveTimeUp(): StreamActionResult<Flow<Result<Unit>>>
    fun closeStream()
}

sealed class StreamActionResult<out T>{
    object StreamNotInitialized: StreamActionResult<Nothing>()
    data class IsInitialized<T>(val result: T) : StreamActionResult<T>()
}

fun <T, R>StreamActionResult<T>.ifInitialized(action: T.() -> R) = when(this){
    is StreamActionResult.IsInitialized -> StreamActionResult.IsInitialized(
        result = action.invoke(this.result)
    )
    StreamActionResult.StreamNotInitialized -> StreamActionResult.StreamNotInitialized
}