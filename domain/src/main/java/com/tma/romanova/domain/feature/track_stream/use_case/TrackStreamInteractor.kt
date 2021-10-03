package com.tma.romanova.domain.feature.track_stream.use_case

import com.tma.romanova.domain.event.*
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.track_stream.StreamActionResult
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository
import com.tma.romanova.domain.feature.track_stream.ifInitialized
import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.flow.*

class TrackStreamInteractorImpl(
    private val trackStreamRepository: TrackStreamRepository
    ) : TrackStreamInteractor{
    override val currentTrackPlayTime: StreamActionResult<Flow<Long>> by lazy {
        trackStreamRepository.currentTrackPlayTime
    }

    override val durationMs: StreamActionResult<Long?>
        get() = trackStreamRepository.duration.ifInitialized {
                when (val result = this) {
                    Result.CacheIsEmpty -> null
                    is Result.LocalException -> null
                    Result.NetworkError -> null
                    is Result.ServerError -> null
                    is Result.Success -> result.data
                }
            }

    override fun prepareTrack(track: Track, withPlaying: Boolean): Flow<PrepareTrackEvent> =
        flow{
            emit(
                if(withPlaying) PrepareTrackEvent.PrepareStartWithPlaying
                else PrepareTrackEvent.PrepareStart
            )
            trackStreamRepository.prepareTrack(
                track = track,
                withPlaying = withPlaying
            ).collect {
                emit(it.prepareTrackEvent)
            }
    }

    override fun playTrack()=
        trackStreamRepository.playTrack().ifInitialized {
            map { it.resumeTrackEvent }
        }

    override fun pauseTrack() =
        trackStreamRepository.pauseTrack().ifInitialized {
            map { it.pauseTrackEvent }
        }

    override fun moveTimeBack() =
        trackStreamRepository.moveTimeBack().ifInitialized {
            map { it.moveTimeBackTrackEvent }
        }

    override fun moveTimeUp() =
        trackStreamRepository.moveTimeUp().ifInitialized {
            map { it.moveTimeUpTrackEvent }
        }

    override fun moveToPosition(playedPercent: Float) =
        trackStreamRepository.moveToPosition(playedPercent = playedPercent).ifInitialized {
            map { it.moveToPositionTrackEvent }
        }

    override fun closeStream() {
        trackStreamRepository.closeStream()
    }

}

interface TrackStreamInteractor {
    val currentTrackPlayTime: StreamActionResult<Flow<Long>>
    val durationMs: StreamActionResult<Long?>

    fun prepareTrack(track: Track, withPlaying: Boolean): Flow<PrepareTrackEvent>
    fun playTrack(): StreamActionResult<Flow<ResumeTrackEvent>>
    fun pauseTrack(): StreamActionResult<Flow<PauseTrackEvent>>
    fun moveToPosition(playedPercent: Float): StreamActionResult<Flow<MoveToPositionTrackEvent>>
    fun moveTimeBack(): StreamActionResult<Flow<MoveTimeBackTrackEvent>>
    fun moveTimeUp(): StreamActionResult<Flow<MoveTimeUpTrackEvent>>
    fun closeStream()
}