package com.tma.romanova.domain.feature.track_stream

import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface TrackStreamRepository {

    companion object{
        const val TIME_UP_MS = 15000
        const val TIME_BACK_MS = 15000
    }

    val currentTrackPlayTime: Flow<Int>

    fun prepareTrack(trackId: Int, withPlaying: Boolean): Flow<Result<Unit>>
    fun playTrack(): Flow<Result<Unit>>
    fun pauseTrack(): Flow<Result<Unit>>
    fun moveTimeBack(): Flow<Result<Unit>>
    fun moveTimeUp(): Flow<Result<Unit>>
}