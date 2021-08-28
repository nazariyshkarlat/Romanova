package com.tma.romanova.data.data_source.network

import com.tma.romanova.data.BuildConfig
import io.ktor.client.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseNetworkRequest<T: Any>(val httpClient: HttpClient): KoinComponent {

    val baseUrl = BuildConfig.BASE_URL

    internal abstract suspend fun makeRequest(): NetworkResult<T>
}