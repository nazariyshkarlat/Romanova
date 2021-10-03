package com.tma.romanova.domain.feature.playlist.use_case

import com.tma.romanova.domain.event.*
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override fun getPlaylist(): Flow<GetPlaylistEvent> = flow{
        emit(ResponseEvent.Loading)
        emit(playlistRepository.getPlaylist().getPlaylistEvent)
    }.flowOn(Dispatchers.IO)

    override fun getTrack(trackId: Int): Flow<GetTrackEvent> = flow{
        emit(ResponseEvent.Loading)
        emit(playlistRepository.getTrack(trackId).getTrackEvent)
    }.flowOn(Dispatchers.IO)


    override suspend fun savePlaylist(playlist: Playlist) {
        playlistRepository.savePlaylist(
            playlist = playlist
        )
    }

}

interface PlaylistInteractor{
    fun getTrack(
        trackId: Int
    ): Flow<GetTrackEvent>
    fun getPlaylist() : Flow<GetPlaylistEvent>
    suspend fun savePlaylist(playlist: Playlist)
}