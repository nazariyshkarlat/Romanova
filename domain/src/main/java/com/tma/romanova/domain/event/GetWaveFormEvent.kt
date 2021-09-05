package com.tma.romanova.domain.event

import com.tma.romanova.domain.result.Result

sealed interface GetWaveFormEvent {
    object Error: GetWaveFormEvent
    data class ValuesReceived(val values: List<Float>): GetWaveFormEvent
}

val Result<List<Float>>.waveFormEvent
get() = when(this){
    Result.CacheIsEmpty -> GetWaveFormEvent.Error
    is Result.LocalException -> GetWaveFormEvent.Error
    Result.NetworkError -> GetWaveFormEvent.Error
    is Result.ServerError -> GetWaveFormEvent.Error
    is Result.Success -> GetWaveFormEvent.ValuesReceived(
        values = this.data
    )
}