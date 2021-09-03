package com.tma.romanova.domain.feature.track_stream.use_case

import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository
import kotlinx.coroutines.flow.Flow
import com.tma.romanova.domain.result.Result

class TrackStreamInteractorImpl(
    private val trackStreamRepository: TrackStreamRepository
    ) : TrackStreamInteractor{
    override val currentTrackPlayTime: Flow<Int> = trackStreamRepository.currentTrackPlayTime

    override fun prepareTrack(trackId: Int, withPlaying: Boolean)=
        trackStreamRepository.prepareTrack(
            trackId = trackId,
            withPlaying = withPlaying
        )

    override fun playTrack()=
        trackStreamRepository.playTrack()

    override fun pauseTrack() =
        trackStreamRepository.pauseTrack()

    override fun moveTimeBack() =
        trackStreamRepository.moveTimeBack()

    override fun moveTimeUp() =
        trackStreamRepository.moveTimeUp()

}

interface TrackStreamInteractor {
    val currentTrackPlayTime: Flow<Int>

    fun prepareTrack(trackId: Int, withPlaying: Boolean): Flow<Result<Unit>>
    fun playTrack(): Flow<Result<Unit>>
    fun pauseTrack(): Flow<Result<Unit>>
    fun moveTimeBack(): Flow<Result<Unit>>
    fun moveTimeUp(): Flow<Result<Unit>>
}