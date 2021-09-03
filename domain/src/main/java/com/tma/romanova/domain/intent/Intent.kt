package com.tma.romanova.domain.intent

sealed class Intent : MainScreenIntent, PlayerIntent {
    object DoNothing: Intent()
}