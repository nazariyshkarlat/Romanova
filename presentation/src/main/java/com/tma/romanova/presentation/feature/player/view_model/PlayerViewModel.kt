package com.tma.romanova.presentation.feature.player.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tma.romanova.domain.action.MainScreenClientAction
import com.tma.romanova.domain.action.PlayerClientAction
import com.tma.romanova.domain.action.toIntent
import com.tma.romanova.domain.event.*
import com.tma.romanova.domain.event.event_handler.MainScreenEventHandler
import com.tma.romanova.domain.event.event_handler.PlayerEventHandler
import com.tma.romanova.domain.feature.playlist.use_case.TrackInteractor
import com.tma.romanova.domain.feature.track_stream.use_case.TrackStreamInteractor
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.intent.PlayerIntent
import com.tma.romanova.domain.navigation.NavigationManager
import com.tma.romanova.domain.result.Result
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState
import com.tma.romanova.domain.state.feature.main_screen.reducer.MainScreenReducer
import com.tma.romanova.domain.state.feature.player.PlayerState
import com.tma.romanova.domain.state.feature.player.reducer.PlayerReducer
import com.tma.romanova.presentation.feature.main.state.MainScreenUiState
import com.tma.romanova.presentation.feature.player.state.PlayerUiState
import com.tma.romanova.presentation.feature.player.state.ui
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class PlayerViewModel(
    private val trackId: Int,
    private val trackInteractor: TrackInteractor
): BaseViewModel(
    initialState = PlayerState.Loading,
    mapToUiState = PlayerState::ui
), KoinComponent {

    private val trackStreamInteractor: TrackStreamInteractor by inject {
        parametersOf(trackId)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            trackStreamInteractor.prepareTrack(
                trackId = trackId,
                withPlaying = true
            ).collect {
                if (it is Result.Success)
                    trackStreamInteractor.currentTrackPlayTime.collect()
            }
        }
    }

    override fun consumeClientAction(action: PlayerClientAction) {
        consumeIntent(action.toIntent(playerState = state.value))
    }

    override fun consumeInternalEvent(internalEvent: PlayerEvent) {
        consumeIntent(internalEvent.intent)
    }

    override fun consumeIntent(intent: PlayerIntent) {
        when(intent){
            is PlayerIntent.ChangePosition -> {

            }
            Intent.DoNothing -> Unit
            PlayerIntent.LoadTrack -> {
                loadTrack(trackId = trackId)
            }
            PlayerIntent.NavigateBack -> {
                NavigationManager.navigateBack()
            }
            is PlayerIntent.ShowTrack -> {
                prepareTrackAndPlay(trackId = trackId)
            }
            is PlayerIntent.NavigateToNextTrack -> TODO()
            is PlayerIntent.NavigateToPreviousTrack -> TODO()
            PlayerIntent.PauseTrack -> {
                pauseTrack()
            }
            PlayerIntent.ResumeTrack -> {
                resumeTrack()
            }
            else -> Unit
        }
        _events.tryEmit(PlayerEventHandler().invoke(state.value, intent))
        state.value = PlayerReducer().invoke(state.value, intent)
    }

    private fun prepareTrackAndPlay(trackId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            trackStreamInteractor.prepareTrack(
                trackId = trackId,
                withPlaying = true
            ).collect {
                if (it is Result.Success)
                    trackStreamInteractor.currentTrackPlayTime.collect{ ms->
                        consumeInternalEvent(PlayerEvent.NeedsChangeTimeLinePosition(
                            newPositionMs = ms
                        ))
                    }
            }
        }
    }

    private fun loadTrack(trackId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            trackInteractor.getTrack(trackId = trackId).collect {
                consumeInternalEvent(it.playerEvent)
            }
        }
    }

    private fun pauseTrack(){
        viewModelScope.launch(Dispatchers.IO) {
            trackStreamInteractor.pauseTrack().collect {

            }
        }
    }

    private fun resumeTrack(){
        viewModelScope.launch(Dispatchers.IO) {
            trackStreamInteractor.playTrack().collect {

            }
        }
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