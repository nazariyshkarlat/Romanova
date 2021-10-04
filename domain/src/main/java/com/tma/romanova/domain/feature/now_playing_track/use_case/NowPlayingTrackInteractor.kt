package com.tma.romanova.domain.feature.now_playing_track.use_case

import com.tma.romanova.domain.event.*
import com.tma.romanova.domain.feature.now_playing_track.NowPlayingTrackRepository
import com.tma.romanova.domain.feature.playlist.entity.PlayingState
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository
import com.tma.romanova.domain.result.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NowPlayingTrackInteractorImpl(
    private val nowPlayingTrackRepository: NowPlayingTrackRepository,
    private val trackStreamRepository: TrackStreamRepository
): NowPlayingTrackInteractor {
    override fun getNowPlayingTrack(): Flow<GetNowPlayingTrackEvent>  = flow{
        emit(ResponseEvent.Loading)
        emit(nowPlayingTrackRepository.getNowPlayingTrack().map {
            println(it)
            it.copy(
                playingState =
                    if(trackStreamRepository.currentPlayingTrackId.notNull)
                        PlayingState.IsPlaying(
                            currentPositionMs = it.playingState.positionMs ?: 0L
                        )
                    else
                        PlayingState.IsOnPause(
                            currentPositionMs = it.playingState.positionMs ?: 0L
                        )
            )
        }.getNowPlayingTrackEvent)
    }.flowOn(Dispatchers.IO)

    override suspend fun getNowPlayingTrackVal(): Track? {
        return nowPlayingTrackRepository.getNowPlayingTrack().dataVal
    }

    override suspend fun saveNowPlayingTrack() {
        trackStreamRepository.lastPlayingTrack?.let { nowPlayingTrackRepository.saveNowPlayingTrack(track = it) }
    }
}

interface NowPlayingTrackInteractor{

    fun getNowPlayingTrack() : Flow<GetNowPlayingTrackEvent>
    suspend fun getNowPlayingTrackVal(): Track?
    suspend fun saveNowPlayingTrack()

}