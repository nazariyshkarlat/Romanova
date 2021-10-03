package com.tma.romanova.data.feature.waveform

import android.graphics.Bitmap
import com.tma.romanova.data.extensions.booleansList
import com.tma.romanova.data.extensions.columnsChunked
import kotlin.math.*

interface WaveformBitmapProcessor {
    suspend fun process(
        bitmap: Bitmap,
        partsCount: Int
    ): List<Float>
}

class WaveformBitmapProcessorImpl: WaveformBitmapProcessor {
    override suspend fun process(
        bitmap: Bitmap,
        partsCount: Int
    ): List<Float> {
        val size = bitmap.width/partsCount
        return bitmap.booleansList
            .columnsChunked(size = size)
            .map {
                it.flatten().count(Boolean::yes).div(
                    it.flatten().size.toFloat()
                )
        }.normalized.sigmoid
    }
}

val List<Float>.normalized
get() = map{
    (max/(max-min)*(it-max)+max).coerceAtLeast(0.1F)
}

val List<Float>.sigmoid
    get() = map{
        (1F/(1+E.pow(-5*(it-0.5)))).toFloat().coerceAtLeast(0.1F)
    }

val List<Float>.max
get() = this.maxOrNull()!!

val List<Float>.min
    get() = this.minOrNull()!!

val Boolean.yes
    get() = this