package com.tma.romanova.domain.event.event_handler

import com.tma.romanova.domain.event.Event
import com.tma.romanova.domain.event.MainScreenClientEvent
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.mvi.EventHandler
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState

fun MainScreenEventHandler() =
    EventHandler<MainScreenState, MainScreenIntent, MainScreenClientEvent>{ currentState, intent->
        Event.NothingHappened
    }