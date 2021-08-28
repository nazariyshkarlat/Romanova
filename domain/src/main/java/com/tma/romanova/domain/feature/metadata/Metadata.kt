package com.tma.romanova.domain.feature.metadata

interface Metadata {
    val onBoardingWasCompleted: Boolean

    fun rememberOnBoardingCompletion()
}