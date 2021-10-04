package com.tma.romanova.presentation.feature.main.view_model

import androidx.lifecycle.viewModelScope
import com.tma.romanova.domain.action.MainScreenClientAction
import com.tma.romanova.domain.action.toIntent
import com.tma.romanova.domain.event.*
import com.tma.romanova.domain.event.event_handler.MainScreenEventHandler
import com.tma.romanova.domain.feature.now_playing_track.use_case.NowPlayingTrackInteractor
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.feature.playlist.use_case.PlaylistInteractor
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState
import com.tma.romanova.domain.state.feature.main_screen.reducer.MainScreenReducer
import com.tma.romanova.presentation.extensions.launchWithCancelInMain
import com.tma.romanova.presentation.feature.main.state.MainScreenUiState
import com.tma.romanova.presentation.feature.main.state.ui
import com.tma.romanova.domain.feature.track_stream.ifInitialized
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractor
import com.tma.romanova.domain.navigation.NavigationDirections
import com.tma.romanova.domain.navigation.NavigationDirections.Player.Arguments.TRACK_ID
import com.tma.romanova.domain.navigation.NavigationManager
import com.tma.romanova.presentation.extensions.launchImmediately
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainScreenViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val nowPlayingTrackInteractor: NowPlayingTrackInteractor,
    private val trackStreamInteractor: TrackStreamInteractor
): BaseViewModel(
    initialState = MainScreenState.PlaylistIsLoading(
        nowPlayingState = MainScreenState.NowPlayingState.NoAudioAvailable
    ),
    mapToUiState = MainScreenState::ui
){

    init {
        consumeInternalEvent(MainScreenEvent.PageOpen)
        observeTrackStreamEvents()
    }

    private var loadPlaylistJob: Job? = null
    private var loadNowPlayingTrackJob: Job? = null

    override fun consumeClientAction(action: MainScreenClientAction) {
        consumeIntent(action.toIntent(state.value))
    }

    override fun consumeInternalEvent(internalEvent: MainScreenEvent) {
        consumeIntent(internalEvent.toIntent(state.value))
    }

    override fun consumeIntent(intent: MainScreenIntent) {
        when(intent){
            is MainScreenIntent.AddTrackLike -> Unit
            is MainScreenIntent.PauseNowPlayingTrack -> {
                pauseTrack()
            }
            is MainScreenIntent.ResumeNowPlayingTrack -> {
                resumeTrack(track = intent.track)
            }
            MainScreenIntent.GoToAboutAuthorScreen -> {
                NavigationManager.navigate(
                    directions = NavigationDirections.AboutAuthor.create()
                )
            }
            is MainScreenIntent.GoToNowPlayingTrackPlayerScreen -> {
                NavigationManager.navigate(
                    directions = NavigationDirections.Player.create(
                        mapOf(
                            TRACK_ID to intent.track.id
                        )
                    )
                )
            }
            MainScreenIntent.GetNowPlayingTrack -> {
                loadNowPlayingTrack()
            }
            is MainScreenIntent.ShowPlaylist -> {
                viewModelScope.launchImmediately {
                    playlistInteractor.savePlaylist(
                        playlist = intent.playlist
                    )
                }
            }
            is MainScreenIntent.GoToPlayerScreen -> {
                state.value.tracks?.find { it == intent.track }?.let { track ->
                    NavigationManager.navigate(
                        directions = NavigationDirections.Player.create(
                            mapOf(
                                TRACK_ID to track.id
                            )
                        )
                    )
                }
            }
            MainScreenIntent.LoadData -> {
                loadPlaylist()
                loadNowPlayingTrack()
            }
            else -> Unit
        }
        _events.tryEmit(MainScreenEventHandler().invoke(state.value, intent))
        state.value = MainScreenReducer().invoke(state.value, intent)
    }

    private fun pauseTrack(){
        trackStreamInteractor.pauseTrack().ifInitialized {
            viewModelScope.launch(Dispatchers.IO) {
                collect {
                    withContext(Dispatchers.Main) {
                        consumeInternalEvent(it.mainScreenEvent)
                    }
                }
            }
        }
    }

    private fun resumeTrack(track: Track){
        viewModelScope.launch(Dispatchers.IO) {
            val currentPlayingTrack = trackStreamInteractor.lastPlayingTrack
            if (currentPlayingTrack == null) {
                trackStreamInteractor.prepareTrack(
                    withPlaying = true,
                    track = track
                ).collect {
                    println("prepare track event ${it}")
                    withContext(Dispatchers.Main){
                        consumeInternalEvent(it.mainScreenEvent)
                    }
                }
            } else {
                trackStreamInteractor.playTrack().ifInitialized {
                    viewModelScope.launch(Dispatchers.IO) {
                        collect {
                            withContext(Dispatchers.Main) {
                                consumeInternalEvent(it.mainScreenEvent)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun loadPlaylist(){
        loadPlaylistJob = viewModelScope.launchWithCancelInMain(loadPlaylistJob) {
            playlistInteractor.getPlaylist().collect { consumeInternalEvent(it.mainScreenEvent) }
        }
    }

    private fun observeTrackStreamEvents(){
        viewModelScope.launch(Dispatchers.Main) {
            trackStreamInteractor.playingEvent.collect {
                consumeInternalEvent(it.mainScreenEvent)
            }
        }
    }

    private fun loadNowPlayingTrack(){
        loadNowPlayingTrackJob = viewModelScope.launchWithCancelInMain(loadNowPlayingTrackJob) {
            nowPlayingTrackInteractor.getNowPlayingTrack().collect { consumeInternalEvent(it.mainScreenEvent.also { println(it) }) }
        }
    }


}


typealias BaseViewModel =
        com.tma.romanova.presentation.base.BaseViewModel<
                MainScreenState,
                MainScreenUiState,
                MainScreenIntent,
                MainScreenClientEvent,
                MainScreenClientAction,
                MainScreenEvent
                >