package com.tma.romanova.domain.state.feature.player

import com.tma.romanova.domain.feature.playlist.entity.Track

sealed class PlayerState {

    val currentTrackNullable
    get() = when(this){
        Loading -> null
        is TrackIsPlaying -> currentTrack
    }

    data class TrackIsPlaying(
        val desiredCurrentTrack: Pair<Track, DirectionPriority>?,
        val currentTrack: Track,
        val allTracks: List<Track>,
        val waveFormFilledPercent: Float,
        val waveFormLastTouchStartPosition: Float?,
        val waveFormValuesStatus: WaveFormValuesStatus
    ): PlayerState(){
        val playedPercent
        get() = currentTrack.playingState.positionMs?.div(
            currentTrack.duration.toFloat()
        ) ?: 0F

        val timeLineIsPressed: Boolean
        get() = waveFormLastTouchStartPosition != null
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

enum class DirectionPriority{
    Left, Right
}