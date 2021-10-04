package com.tma.romanova.presentation.feature.player.view_model

import androidx.lifecycle.viewModelScope
import com.tma.romanova.domain.action.PlayerClientAction
import com.tma.romanova.domain.action.toIntent
import com.tma.romanova.domain.event.*
import com.tma.romanova.domain.event.event_handler.PlayerEventHandler
import com.tma.romanova.domain.feature.now_playing_track.use_case.NowPlayingTrackInteractor
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.playlist.use_case.PlaylistInteractor
import com.tma.romanova.domain.feature.playlist.use_case.TrackInteractor
import com.tma.romanova.domain.feature.track_stream.ifInitialized
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractor
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.PlayerIntent
import com.tma.romanova.domain.navigation.NavigationManager
import com.tma.romanova.domain.state.feature.player.PlayerState
import com.tma.romanova.domain.state.feature.player.reducer.PlayerReducer
import com.tma.romanova.presentation.extensions.launchImmediately
import com.tma.romanova.presentation.feature.player.state.PlayerUiState
import com.tma.romanova.presentation.feature.player.state.ui
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import java.lang.Exception

class PlayerViewModel(
    private var trackId: Int,
    private val trackInteractor: TrackInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val trackStreamInteractor: TrackStreamInteractor,
    private val nowPlayingTrackInteractor: NowPlayingTrackInteractor
): BaseViewModel(
    initialState = PlayerState.Loading,
    mapToUiState = PlayerState::ui
), KoinComponent {


    private val completableJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)

    private val currentPlayingTrackId
    get() = trackStreamInteractor.currentPlayingTrackId.initializedOrNull

    init {
        consumeInternalEvent(
            internalEvent = PlayerEvent.PageOpen(trackId = trackId)
        )
        observeStreamEvents()
        observePlayingPosition()
    }
    override fun onCleared() {
        super.onCleared()
        completableJob.cancel()
    }

    override fun consumeClientAction(action: PlayerClientAction) {
        consumeIntent(action.toIntent(playerState = state.value))
    }

    override fun consumeInternalEvent(internalEvent: PlayerEvent) {
        consumeIntent(internalEvent.toIntent(state.value))
    }

    override fun consumeIntent(intent: PlayerIntent) {
        when(intent){
            Intent.DoNothing -> Unit
            is PlayerIntent.UpdateTrack -> {
                onLoadPage(trackId = intent.trackId)
            }
            PlayerIntent.NavigateBack -> {
                NavigationManager.navigateBack()
            }
            is PlayerIntent.DownloadWaveFormValues -> {
                loadWaveFormValues(
                    partsCount = intent.partsCount,
                    waveFormUrl = intent.waveFormUrl
                )
            }
            is PlayerIntent.MoveTrackToPosition -> {
                moveToPosition(
                    newPositionMs = intent.newPositionMs
                )
            }
            is PlayerIntent.ShowTrack -> {
                if(currentPlayingTrackId != intent.currentTrack.id)
                    prepareTrackAndPlay(track = intent.currentTrack)
            }
            PlayerIntent.PauseTrack -> {
                pauseTrack()
            }
            PlayerIntent.ResumeTrack -> {
                resumeTrack()
            }
            PlayerIntent.UpPlayingTime ->{
                upPlayingTime()
            }
            PlayerIntent.DownPlayingTime -> {
                downPlayingTime()
            }
            else -> Unit
        }
        _events.tryEmit(PlayerEventHandler().invoke(state.value, intent))
        state.value = PlayerReducer().invoke(state.value, intent)
    }

    private fun onLoadPage(trackId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            var nowPlayingTrack = nowPlayingTrackInteractor.getNowPlayingTrackVal()

            this@PlayerViewModel.trackId = trackId
            if ((currentPlayingTrackId != trackId &&
                    nowPlayingTrack?.id != trackId) ||
                    nowPlayingTrack == null) {
                withContext(Dispatchers.Main) {
                    reloadTrack(trackId = trackId)
                }
            } else {

                trackStreamInteractor.lastPlayingTrack?.let{
                    nowPlayingTrack = it
                }
                consumeInternalEvent(
                    GetNowPlayingTrackEvent.NowPlayingTrackFound(
                        track = nowPlayingTrack!!
                    ).playerEvent
                )

                coroutineScope.launch(Dispatchers.IO) {
                    if(state.value !is PlayerState.TrackIsPlaying) {
                        loadTrack(
                            trackId = trackId,
                            nowPlayingTrack = nowPlayingTrack
                        )
                    }
                }
            }
        }
    }

    private fun reloadTrack(trackId: Int){
        try {
            completableJob.cancelChildren()
        } catch (e: Exception) { }
        trackStreamInteractor.closeStream()
        loadTrack(
            trackId = trackId,
            nowPlayingTrack = null
        )
    }

    private fun prepareTrackAndPlay(track: Track){
        coroutineScope.launch(Dispatchers.IO) {
            trackStreamInteractor.prepareTrack(
                track = track,
                withPlaying = true
            ).collect {
                withContext(Dispatchers.Main) {
                    consumeInternalEvent(it.playerEvent)
                }
            }
        }
    }

    private fun moveToPosition(newPositionMs: Long){
        trackStreamInteractor.durationMs.ifInitialized {
            this?.let {
                trackStreamInteractor.moveToPosition(
                    playedPercent = newPositionMs / it.toFloat()
                ).ifInitialized {
                    coroutineScope.launch(Dispatchers.IO) {
                        collect {
                            withContext(Dispatchers.Main) {
                                consumeInternalEvent(it.playerEvent)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadTrack(trackId: Int, nowPlayingTrack: Track?){
        println(nowPlayingTrack)
        coroutineScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylist()
                .combine(
                    if(nowPlayingTrack != null) flow {
                        emit(
                            GetNowPlayingTrackEvent.NowPlayingTrackFound(
                                track = nowPlayingTrack
                            ).getTrackEvent
                        )
                    }
                    else playlistInteractor.getTrack(trackId = trackId)
                ) { playlistRes: GetPlaylistEvent, trackRes: GetTrackEvent->
                    consumeInternalEvent((trackRes to playlistRes).playerEvent)
                }.collect()
        }
    }

    private fun pauseTrack(){
        trackStreamInteractor.pauseTrack().ifInitialized {
            coroutineScope.launch(Dispatchers.IO) {
                collect {
                    withContext(Dispatchers.Main) {
                        consumeInternalEvent(it.playerEvent)
                    }
                }
            }
        }
    }

    private fun observeStreamEvents(){
        viewModelScope.launch(Dispatchers.IO) {
            trackStreamInteractor.playingEvent.collect {
                withContext(Dispatchers.Main){
                    consumeInternalEvent(it.playerEvent)
                }
            }
        }
    }

    private fun observePlayingPosition(){
        viewModelScope.launch(Dispatchers.IO) {
            trackStreamInteractor.currentTrackPlayTime.collect { ms ->
                withContext(Dispatchers.Main){
                    trackStreamInteractor.durationMs.ifInitialized {
                        this?.let { duration ->
                            consumeInternalEvent(
                                PlayerEvent.NeedsChangeTimeLinePosition(
                                    newPosition = ms / duration.toFloat()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadWaveFormValues(partsCount: Int, waveFormUrl: String){
        coroutineScope.launch(Dispatchers.Default) {
            trackInteractor.getWaveformValues(
                url = waveFormUrl,
                partsCount = partsCount
            ).also {
                consumeInternalEvent(it.playerEvent)
            }
        }
    }

    private fun resumeTrack(){
            trackStreamInteractor.playTrack().ifInitialized {
                coroutineScope.launch(Dispatchers.IO) {
                    collect {
                        withContext(Dispatchers.Main) {
                            consumeInternalEvent(it.playerEvent)
                        }
                    }
                }
        }
    }

    private fun upPlayingTime(){
        trackStreamInteractor.moveTimeUp().ifInitialized {
            coroutineScope.launch(Dispatchers.IO) {
                collect {
                    withContext(Dispatchers.Main) {
                        consumeInternalEvent(it.playerEvent)
                    }
                }
            }
        }
    }

    private fun downPlayingTime(){
        trackStreamInteractor.moveTimeBack().ifInitialized {
            coroutineScope.launch(Dispatchers.IO) {
                collect {
                    withContext(Dispatchers.Main) {
                        consumeInternalEvent(it.playerEvent)
                    }
                }
            }
        }
    }

    override fun onStop(){
/*        consumeClientAction(
            action = PlayerClientAction.AppClose
        )*/
    }

}

typealias BaseViewModel =
        com.tma.romanova.presentation.base.BaseViewModel<
                PlayerState,
                PlayerUiState,
                PlayerIntent,
                PlayerClientEvent,
                PlayerClientAction,
                PlayerEvent
                >