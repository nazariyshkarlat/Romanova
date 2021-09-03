package com.tma.romanova.domain.event.event_handler

import com.tma.romanova.domain.event.Event
import com.tma.romanova.domain.event.MainScreenClientEvent
import com.tma.romanova.domain.event.PlayerClientEvent
import com.tma.romanova.domain.intent.MainScreenIntent
import com.tma.romanova.domain.intent.PlayerIntent
import com.tma.romanova.domain.mvi.EventHandler
import com.tma.romanova.domain.state.feature.main_screen.MainScreenState
import com.tma.romanova.domain.state.feature.player.PlayerState

fun PlayerEventHandler() =
    EventHandler<PlayerState, PlayerIntent, PlayerClientEvent>{ currentState, intent->
        Event.DoNothing
    }