package com.tma.romanova.extensions

fun CharSequence.unescape(): CharSequence {
    if (indexOf("\\u") == -1)
        return this

    var escaped = this
    val processed = StringBuffer("")

    var position = escaped.indexOf("\\u")
    while (position != -1) {
        if (position != 0)
            processed.append(escaped.substring(0, position))
        val token = escaped.substring(position + 2, position + 6)
        escaped = escaped.substring(position + 6)
        processed.append(token.toInt(16).toChar())
        position = escaped.indexOf("\\u")
    }
    return processed
}
