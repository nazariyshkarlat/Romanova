package com.tma.romanova.data.feature.waveform

import android.graphics.Bitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.tma.romanova.core.application
import com.tma.romanova.data.extensions.bitmap

interface BitmapDownloader {
    suspend fun download(compressionFactor: Float): ImageDownloadResult
}

class BitmapDownloaderImpl(
    private val url: String
): BitmapDownloader {
    override suspend fun download(compressionFactor: Float): ImageDownloadResult {
        val request = ImageRequest.Builder(context = application)
            .size(
                width = (1800*compressionFactor).toInt(),
                height = (280*compressionFactor).toInt()
            )
            .data(url)
            .build()
        val drawable = application.imageLoader.execute(request).drawable
        return if(drawable == null) ImageDownloadResult.Error else ImageDownloadResult.Success(
            bitmap = drawable.bitmap
        )
    }

}

sealed class ImageDownloadResult{
    object Error : ImageDownloadResult()
    data class Success(val bitmap: Bitmap): ImageDownloadResult()
}
