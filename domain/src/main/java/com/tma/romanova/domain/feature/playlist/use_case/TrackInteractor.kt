package com.tma.romanova.domain.feature.playlist.use_case

import com.tma.romanova.domain.event.GetTrackEvent
import com.tma.romanova.domain.event.ResponseEvent
import com.tma.romanova.domain.event.getTrackEvent
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import com.tma.romanova.domain.feature.playlist.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TrackInteractorImpl(
    private val playlistRepository: PlaylistRepository
): TrackInteractor {
    override fun getTrack(trackId: Int): Flow<GetTrackEvent> = flow{
        emit(ResponseEvent.Loading)
        emit(playlistRepository.getTrack(trackId).getTrackEvent)
    }.flowOn(Dispatchers.IO)

    override suspend fun saveTrack(track: Track) {
        playlistRepository.saveTrack(track)
    }
}

interface TrackInteractor{
    fun getTrack(
        trackId: Int
    ): Flow<GetTrackEvent>

    suspend fun saveTrack(
        track: Track
    )
}