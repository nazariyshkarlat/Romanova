package com.tma.romanova.domain.extensions

fun <T>List<T>.atIndexOrFirst(index: Int): T{
    return if(index in 0..lastIndex) this[index]
    else first()
}

fun <T>List<T>.atIndexOrLast(index: Int): T{
    return if(index in 0..lastIndex) this[index]
    else last()
}