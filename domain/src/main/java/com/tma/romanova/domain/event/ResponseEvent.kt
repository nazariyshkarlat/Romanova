package com.tma.romanova.domain.event

sealed class ResponseEvent : GetPlaylistEvent {
    object Loading: ResponseEvent()
    object DoNothing: ResponseEvent()
    object ServerError: ResponseEvent()
    object NetworkUnavailable: ResponseEvent()
    object Exception: ResponseEvent()
}