package com.tma.romanova.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

abstract class BaseViewModel<State, UiState, Intent, ClientEvent, ClientAction, InternalEvent>(
    private val initialState: State,
    private val mapToUiState: State.() -> UiState
) : ViewModel() {

    protected val state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = state.map {
        mapToUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = mapToUiState(state.value)
    )

    protected val _events: MutableSharedFlow<ClientEvent> = MutableSharedFlow()
    val events by lazy { _events.asSharedFlow() }

    abstract fun consumeClientAction(action: ClientAction)

    protected abstract fun consumeInternalEvent(internalEvent: InternalEvent)

    protected abstract fun consumeIntent(intent: Intent)

    open fun onStart(){

    }

    open fun onStop(){

    }

}