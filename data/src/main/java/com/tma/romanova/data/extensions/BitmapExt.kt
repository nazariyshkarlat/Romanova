package com.tma.romanova.data.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

val Drawable.bitmap: Bitmap
get() = run{
    if(this is BitmapDrawable) return bitmap.copy(Bitmap.Config.ARGB_8888, true)

    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

fun Drawable.compressedBitmap(compressionFactor: Float): Bitmap{
        return bitmap.compressed(
            compressionFactor = compressionFactor
        )
    }

fun Bitmap.compressed(compressionFactor: Float): Bitmap {
    val scaleWidth = width*compressionFactor
    val scaleHeight = height*compressionFactor
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    println(scaleWidth)
    println(scaleHeight)
    return Bitmap.createBitmap(
        this, 0, 0, width, height,
        matrix, false
    )
}

val Bitmap.booleansList: List<List<Boolean>>
get() = run{
    return List(this.height){y->List(this.width){x-> this[x, y] == Color.TRANSPARENT} }
}

fun <E>List<E>.reduceList(keepPercent: Float): List<E>{
    require(keepPercent > 0 && keepPercent <= 1)
    val keepIdx = (100*keepPercent).toInt()
    return filterIndexed { idx, _->
        idx%keepIdx == 0
    }
}

inline operator fun Bitmap.get(x: Int, y: Int): Int = getPixel(x, y)