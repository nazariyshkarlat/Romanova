package com.tma.romanova.domain.feature.now_playing_track.use_case

import com.tma.romanova.domain.event.*
import com.tma.romanova.domain.feature.now_playing_track.NowPlayingTrackRepository
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NowPlayingTrackInteractorImpl(
    private val nowPlayingTrackRepository: NowPlayingTrackRepository
): NowPlayingTrackInteractor {
    override fun getNowPlayingTrack(): Flow<GetNowPlayingTrackEvent>  = flow{
        emit(ResponseEvent.Loading)
        emit(nowPlayingTrackRepository.getNowPlayingTrack().getNowPlayingTrackEvent)
    }.flowOn(Dispatchers.IO)

    override suspend fun saveNowPlayingTrack(track: Track) {
        nowPlayingTrackRepository.saveNowPlayingTrack(track = track)
    }
}

interface NowPlayingTrackInteractor{

    fun getNowPlayingTrack() : Flow<GetNowPlayingTrackEvent>
    suspend fun saveNowPlayingTrack(track: Track)

}