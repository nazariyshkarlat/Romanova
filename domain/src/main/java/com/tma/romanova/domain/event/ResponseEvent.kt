package com.tma.romanova.domain.event

import com.tma.romanova.domain.result.ErrorCause

sealed class ResponseEvent : GetPlaylistEvent, GetTrackEvent, GetWaveFormEvent{
    object Loading: ResponseEvent()
    object DoNothing: ResponseEvent()
    data class ServerError(val code: Int, val message: String?): ResponseEvent(){
        val errorCase
        get() = ErrorCause.ServerError(
            code = code,
            message = message
        )
    }
    object NetworkUnavailable: ResponseEvent(){
        val errorCase
            get() = ErrorCause.NetworkError
    }
    data class Exception(val cause: Throwable): ResponseEvent(){
        val errorCase
            get() = ErrorCause.Exception(
                cause = cause
            )
    }
}