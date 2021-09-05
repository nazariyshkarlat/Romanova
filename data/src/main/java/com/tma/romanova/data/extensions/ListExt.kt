package com.tma.romanova.data.extensions

typealias Matrix<E> = List<List<E>>

fun <E> Matrix<E>.columnsChunked(size: Int): List<Matrix<E>> {

    fun <E> Matrix<E>.rowsChunked(size: Int): List<Matrix<E>> =
        this.map { it.chunked(size) }

    return rowsChunked(size).transposed
}

val <E> Matrix<E>.transposed
    get(): Matrix<E> {

        fun <E> List<E>.head(): E = this.first()
        fun <E> List<E>.tail(): List<E> = this.takeLast(this.size - 1)
        fun <E> E.append(xs: List<E>): List<E> = listOf(this).plus(xs)

        filter { it.isNotEmpty() }.let { ys ->
            return when (ys.isNotEmpty()) {
                true -> ys.map { it.head() }.append(ys.map { it.tail() }.transposed)
                else -> emptyList()
            }
        }
    }