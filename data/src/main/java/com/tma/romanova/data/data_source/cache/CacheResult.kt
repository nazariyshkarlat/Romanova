package com.tma.romanova.data.data_source.cache

import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result

sealed class CacheResult<out T: Any> {
    data class Success<T : Any>(val data: T) : CacheResult<T>()
    object DataNotFound : CacheResult<Nothing>()
    data class Exception(val cause: java.lang.Exception): CacheResult<Nothing>()
}

fun <T: Any, R: Any> CacheResult<T>.toResult(transformation: (T) -> R) : Result<R> = when(this){
    is CacheResult.DataNotFound -> Result.CacheIsEmpty
    is CacheResult.Success -> Result.Success(data = transformation(data), dataSourceType = DataSourceType.Cache)
    is CacheResult.Exception -> Result.LocalException(cause = cause)
}