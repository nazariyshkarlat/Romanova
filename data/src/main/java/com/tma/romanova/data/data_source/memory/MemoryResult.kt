package com.tma.romanova.data.data_source.memory

import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result

sealed class MemoryResult<out T: Any> {
    data class Success<T : Any>(val data: T) : MemoryResult<T>()
    object DataNotFound : MemoryResult<Nothing>()
    data class Exception(val cause: java.lang.Exception): MemoryResult<Nothing>()
}

fun <T: Any, R: Any> MemoryResult<T>.toResult(transformation: (T) -> R) : Result<R> = when(this){
    is MemoryResult.DataNotFound -> Result.CacheIsEmpty
    is MemoryResult.Success -> Result.Success(data = transformation(data), dataSourceType = DataSourceType.Cache)
    is MemoryResult.Exception -> Result.LocalException(cause = cause)
}