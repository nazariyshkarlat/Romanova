package com.tma.romanova.domain.result

sealed class ErrorCause {
    object NetworkError : ErrorCause()

    data class ServerError(
        val code: Int,
        val message: String?,
        ) : ErrorCause()

    data class Exception(
        val cause: Throwable
    ) : ErrorCause()
}