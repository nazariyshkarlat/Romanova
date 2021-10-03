package com.tma.romanova.di

import android.util.Log
import com.tma.romanova.data.BuildConfig
import com.tma.romanova.extensions.unescape
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single{
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
            encodeDefaults = true
        }
    }
    single{
        HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 25 * 1000
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(get())
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HttpClient", message.unescape().toString())
                    }
                }
                level = LogLevel.BODY
            }
            install(DefaultRequest) {
                headers{
                    appendAll(defHeaders)
                }
            }
        }
    }
}

val defHeaders = headersOf(
    "Authorization" to listOf(
        "OAuth ${BuildConfig.OAUTH_TOKEN}"
    ),
    "Host" to listOf(
        BuildConfig.BASE_URL.removePrefix("https://")
    ),
    "Accept-Encoding" to listOf(
        "utf-8",
        "application/json"
    ),
    "Content-Type" to listOf("application/json; charset=utf-8"),
    "Accept" to listOf(
        "text/javascript",
        "*/*; q=0.01",
        "application/json"
    )
)