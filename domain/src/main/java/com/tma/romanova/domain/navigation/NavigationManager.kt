package com.tma.romanova.domain.navigation

import com.tma.romanova.domain.feature.metadata.Metadata
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow

object NavigationManager {

    val Metadata.startDirection
    get() = if(this.onBoardingWasCompleted) NavigationDirections.MainScreen
    else NavigationDirections.OnBoarding

    private val _command = Channel<NavigationCommand.CommandResult>(Channel.BUFFERED)
    val command = _command.receiveAsFlow()

    fun navigate(directions: NavigationCommand.CommandResult) {
        _command.trySend(directions)
    }

    fun navigateBack() {
        _command.trySend(NavigationCommand.CommandResult.navigateBack)
    }
}
