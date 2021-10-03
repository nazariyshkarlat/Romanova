package com.tma.romanova.domain.result

sealed class Result<out T: Any> {
    data class Success<T : Any>(val data: T, val dataSourceType: DataSourceType) : Result<T>()
    data class ServerError(val code: Int, val message: String?) : Result<Nothing>()
    data class LocalException(val cause: Exception) : Result<Nothing>()
    object CacheIsEmpty : Result<Nothing>()
    object NetworkError : Result<Nothing>()
}

fun <T: Any, R: Any>Result<T>.map(
    mapSuccess: (Result.Success<T>) -> R
): Result<R> =
    when(this){
        Result.CacheIsEmpty -> Result.CacheIsEmpty
        is Result.LocalException -> Result.LocalException(
            cause = cause
        )
        Result.NetworkError -> Result.NetworkError
        is Result.ServerError -> Result.ServerError(
            code = code,
            message = message
        )
        is Result.Success -> Result.Success(
            data = mapSuccess(this),
            dataSourceType = dataSourceType
        )
    }