package com.tma.romanova.domain.state.feature.player

import com.tma.romanova.domain.feature.playlist.entity.Track

sealed class PlayerState {
    data class TrackIsPlaying(
        val track: Track,
        val waveFormValuesStatus: WaveFormValuesStatus
    ): PlayerState(){
        val playedPercent
        get() = track.playingState.positionMs?.div(
            track.duration.toFloat()
        ) ?: 0F
    }
    object Loading: PlayerState()
}

sealed class WaveFormValuesStatus{
   object Error: WaveFormValuesStatus()
    object Loading: WaveFormValuesStatus()
    data class ValuesReceived(
        val values: List<Float>
    ): WaveFormValuesStatus()
}