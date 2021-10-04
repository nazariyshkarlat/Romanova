package com.tma.romanova.domain.feature.track_stream.use_case

import com.tma.romanova.domain.event.*
import com.tma.romanova.domain.extensions.atIndexOrFirst
import com.tma.romanova.domain.extensions.onEachAfter
import com.tma.romanova.domain.feature.now_playing_track.NowPlayingTrackRepository
import com.tma.romanova.domain.feature.playlist.PlaylistRepository
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.playlist.use_case.PlaylistInteractor
import com.tma.romanova.domain.feature.track_stream.PlayingEvent
import com.tma.romanova.domain.feature.track_stream.StreamActionResult
import com.tma.romanova.domain.feature.track_stream.TrackStreamRepository
import com.tma.romanova.domain.feature.track_stream.ifInitialized
import com.tma.romanova.domain.result.Result
import kotlin.collections.*
import kotlinx.coroutines.flow.*

class TrackStreamInteractorImpl(
    private val trackStreamRepository: TrackStreamRepository,
    private val playlistRepository: PlaylistRepository,
    private val nowPlayingTrackRepository: NowPlayingTrackRepository
    ) : TrackStreamInteractor{
    override val currentTrackPlayTime: Flow<Long>
    get() = trackStreamRepository.currentTrackPlayTime

    private var currentPlayingTrack: Track? = null

    override val currentPlayingTrackId: StreamActionResult<Int>
        get() = trackStreamRepository.currentPlayingTrackId

    override val lastPlayingTrack: Track?
        get() = trackStreamRepository.lastPlayingTrack

    private suspend fun playlistTracks(): List<Track>{
       return (playlistRepository.getPlaylist() as? Result.Success)?.data?.tracks
            ?: emptyList()
    }

    override val playingEvent: Flow<PlayingEvent>
    get() = trackStreamRepository.playingEvent
        .onEachAfter {
            when(it){
                PlayingEvent.TrackEnd -> {
                    val tracks = playlistTracks()
                    tracks.indexOfFirst { it.id == currentPlayingTrack?.id }
                        .takeIf { it != -1 }
                        ?.let {
                            prepareTrack(
                                tracks.atIndexOrFirst(it+1),
                                withPlaying = true
                            ).collect()
                        }
                }
                else -> Unit
            }
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
            nowPlayingTrackRepository.saveNowPlayingTrack(track = track)
            trackStreamRepository.preparePlaylist(
                track = track,
                withPlaying = withPlaying
            ).collect{
                emit(it.prepareTrackEvent)
            }
    }.also {
            currentPlayingTrack = track
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
    val currentTrackPlayTime: Flow<Long>
    val durationMs: StreamActionResult<Long?>
    val playingEvent: Flow<PlayingEvent>
    val lastPlayingTrack: Track?
    val currentPlayingTrackId: StreamActionResult<Int>

    fun prepareTrack(track: Track, withPlaying: Boolean): Flow<PrepareTrackEvent>
    fun playTrack(): StreamActionResult<Flow<ResumeTrackEvent>>
    fun pauseTrack(): StreamActionResult<Flow<PauseTrackEvent>>
    fun moveToPosition(playedPercent: Float): StreamActionResult<Flow<MoveToPositionTrackEvent>>
    fun moveTimeBack(): StreamActionResult<Flow<MoveTimeBackTrackEvent>>
    fun moveTimeUp(): StreamActionResult<Flow<MoveTimeUpTrackEvent>>
    fun closeStream()
}