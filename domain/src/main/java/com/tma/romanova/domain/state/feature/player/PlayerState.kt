package com.tma.romanova.domain.state.feature.player

import com.tma.romanova.domain.feature.playlist.entity.Track

sealed class PlayerState {
    data class TrackIsPlaying(
        val track: Track
    ): PlayerState()
    object Loading: PlayerState()
}