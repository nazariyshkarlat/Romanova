package com.tma.romanova.domain.feature.playlist.use_case

import com.tma.romanova.domain.event.GetPlaylistEvent
import com.tma.romanova.domain.event.ResponseEvent
import com.tma.romanova.domain.event.getPlaylistEvent
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetPlaylistImpl(private val playlistRepository: PlaylistRepository): GetPlaylist {
    override fun getPlaylist(): Flow<GetPlaylistEvent> = flow{
        emit(ResponseEvent.Loading)
        emit(playlistRepository.getPlaylist().getPlaylistEvent)
    }.flowOn(Dispatchers.IO)

}

interface GetPlaylist{
    fun getPlaylist() : Flow<GetPlaylistEvent>
}