package com.tma.romanova.domain.state.base

import com.tma.romanova.domain.result.ErrorCause

sealed class PaginationState<out T> {

    abstract val loadedItems: List<T>

    data class PaginationError<T>(
        override val loadedItems: List<T>,
        val errorCause: ErrorCause
        ) : PaginationState<T>()

    data class PaginationLoading<T>(
        override val loadedItems: List<T>
        ) : PaginationState<T>()

    data class PaginationLastPage<T>(
        override val loadedItems: List<T>
        ) : PaginationState<T>()
}