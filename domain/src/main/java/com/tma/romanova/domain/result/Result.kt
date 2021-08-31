package com.tma.romanova.domain.result

sealed class Result<out T: Any> {
    data class Success<T : Any>(val data: T, val dataSourceType: DataSourceType) : Result<T>()
    data class ServerError(val code: Int, val message: String?) : Result<Nothing>()
    data class LocalException(val cause: Exception) : Result<Nothing>()
    object CacheIsEmpty : Result<Nothing>()
    object NetworkError : Result<Nothing>()
}