package com.tma.romanova.data.feature.playlist.data_source.track_stream

import com.tma.romanova.data.BuildConfig
import com.tma.romanova.data.feature.playlist.data_source.track_stream.network.NetworkStream

class StreamDataSourceImpl(private val trackId: Int): StreamDataSource {
    override val stream: Stream = NetworkStream(
        url = "${BuildConfig.BASE_URL}/tracks/$trackId/stream"
    )
}