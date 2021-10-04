package com.tma.romanova.data.feature.track_stream.data_source

import com.tma.romanova.domain.feature.track_stream.Stream
import com.tma.romanova.domain.result.Result

interface StreamDataSource {

    suspend fun getStream(): Result<Stream>

}