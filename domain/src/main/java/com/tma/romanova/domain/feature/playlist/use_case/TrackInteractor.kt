package com.tma.romanova.domain.feature.playlist.use_case

import com.tma.romanova.domain.event.*
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import com.tma.romanova.domain.feature.playlist.entity.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TrackInteractorImpl(
    private val playlistRepository: PlaylistRepository
): TrackInteractor {

    override suspend fun getWaveformValues(url: String, partsCount: Int): GetWaveFormEvent {
        return playlistRepository.getWaveFormValues(
            url = url,
            partsCount = partsCount
        ).waveFormEvent
    }
}

interface TrackInteractor{

    suspend fun getWaveformValues(
        url: String,
        partsCount: Int
    ): GetWaveFormEvent
}