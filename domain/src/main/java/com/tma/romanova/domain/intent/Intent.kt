package com.tma.romanova.domain.intent

sealed class Intent : MainScreenIntent {
    object DoNothing: Intent()
}