package com.tma.romanova.presentation.feature.main.view_model

import androidx.lifecycle.viewModelScope
import com.tma.romanova.domain.action.MainScreenClientAction
import com.tma.romanova.domain.action.toIntent
import com.tma.romanova.domain.event.MainScreenClientEvent
import com.tma.romanova.domain.event.MainScreenEvent
import com.tma.romanova.domain.event.event_handler.MainScreenEventHandler
import com.tma.romanova.domain.event.intent
import com.tma.romanova.domain.event.mainScreenEvent
import com.tma.romanova.domain.feature.playlist.use_case.GetPlaylist
import com.tma.romanova.domain.intent.Intent
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.state.MainScreenState
import com.tma.romanova.domain.state.reducer.MainScreenReducer
import com.tma.romanova.presentation.R
import com.tma.romanova.presentation.extensions.drawable
import com.tma.romanova.presentation.extensions.launchWithCancelInMain
import com.tma.romanova.presentation.feature.main.entity.NowPlayingTrackUi
import com.tma.romanova.presentation.feature.main.state.MainScreenUiState
import com.tma.romanova.presentation.feature.main.state.ui
import kotlinx.coroutines.Job
import com.tma.romanova.domain.feature.playlist.entity.Track
import com.tma.romanova.domain.navigation.NavigationDirections
import com.tma.romanova.domain.navigation.NavigationManager
import kotlinx.coroutines.flow.collect
import java.util.*

class MainScreenViewModel(
    private val getPlaylist: GetPlaylist
): BaseViewModel(
    initialState = MainScreenState.PlaylistIsLoading(
        nowPlayingState = MainScreenState.NowPlayingState.AudioIsPlaying(
            isOnPause = true,
            track = Track(
                largeArtworkUrl = "https://i1.sndcdn.com/artworks-ZKEyONOnOFmgRZmg-lbS7QQ-t500x500.jpg",
                smallArtworkUrl = "https://i1.sndcdn.com/artworks-ZKEyONOnOFmgRZmg-lbS7QQ-t200x200.jpg",
                waveformUrl = "",
                createdAt = Date(),
                duration = 12341,
                title = "Вечеринка",
                id = 950640094,
            )
        )
    ),
    mapToUiState = MainScreenState::ui
){

    init {
        consumeInternalEvent(MainScreenEvent.PageOpen)
    }

    private var loadPlaylistJob: Job? = null

    override fun consumeClientAction(action: MainScreenClientAction) {
        consumeIntent(action.toIntent(state.value))
    }

    override fun consumeInternalEvent(internalEvent: MainScreenEvent) {
        consumeIntent(internalEvent.intent)
    }

    override fun consumeIntent(intent: MainScreenIntent) {
        when(intent){
            is MainScreenIntent.AddTrackLike -> Unit
            MainScreenIntent.GoToAboutAuthorScreen -> {
                NavigationManager.navigate(
                    directions = NavigationDirections.AboutAuthor.create()
                )
            }
            is MainScreenIntent.GoToPlayerScreen -> Unit
            is MainScreenIntent.GoToTrackComments -> Unit
            Intent.DoNothing -> Unit
            MainScreenIntent.LoadPlaylist -> {
                loadPlaylist()
            }
            is MainScreenIntent.ShowPlaylist -> Unit
            is MainScreenIntent.RemoveTrackLike -> Unit
            MainScreenIntent.ShowPageIsLoading -> Unit
            is MainScreenIntent.ShowPageLoadingError -> Unit
            is MainScreenIntent.PauseNowPlayingTrack -> Unit
            is MainScreenIntent.ResumeNowPlayingTrack -> Unit
            is MainScreenIntent.GoToNowPlayingTrackPlayerScreen -> Unit
        }
        _events.tryEmit(MainScreenEventHandler().invoke(state.value, intent))
        state.value = MainScreenReducer().invoke(state.value, intent)
    }

    private fun loadPlaylist(){
        loadPlaylistJob = viewModelScope.launchWithCancelInMain(loadPlaylistJob) {
            getPlaylist.getPlaylist().collect { consumeInternalEvent(it.mainScreenEvent) }
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