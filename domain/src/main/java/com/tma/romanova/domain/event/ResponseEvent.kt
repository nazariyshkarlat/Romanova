package com.tma.romanova.domain.event

import com.tma.romanova.domain.result.ErrorCause

sealed class ResponseEvent : GetPlaylistEvent, GetTrackEvent, GetWaveFormEvent, GetNowPlayingTrackEvent{
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
    data class Exception(val cause: kotlin.Exception): ResponseEvent(){
        val errorCase
            get() = ErrorCause.Exception(
                cause = cause
            )
    }
}

inline fun <reified T : ResponseEvent, reified R : ResponseEvent> T.transform(transformation: (T) -> R): R =
    when(this){
        ResponseEvent.Loading -> ResponseEvent.Loading as R
        ResponseEvent.DoNothing -> ResponseEvent.DoNothing as R
        is ResponseEvent.Exception -> this as R
        ResponseEvent.NetworkUnavailable -> ResponseEvent.NetworkUnavailable as R
        is ResponseEvent.ServerError -> this as R
        else -> transformation(this)
    }