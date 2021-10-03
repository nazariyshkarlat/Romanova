package com.tma.romanova.data

import com.tma.romanova.data.feature.playlist.PlaylistRepositoryImpl
import com.tma.romanova.data.feature.now_playing_track.data_source.NowPlayingTrackDataSource
import com.tma.romanova.data.feature.now_playing_track.data_source.NowPlayingTrackDataSourceProvider
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RepositoryTest {
    @Test
    fun getPlaylists(){
        runBlocking {
            val dataSource = NowPlayingTrackDataSource()
            PlaylistRepositoryImpl(
                playlistDataSourceProvider = NowPlayingTrackDataSourceProvider(dataSource),
                playlistDataSource = dataSource,
                httpClient = HttpClient {
                    install(HttpTimeout) {
                        requestTimeoutMillis = 25 * 1000
                    }
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(
                            kotlinx.serialization.json.Json {
                                ignoreUnknownKeys = true
                                isLenient = true
                                prettyPrint = true
                                encodeDefaults = true
                            }
                        )
                    }
                    install(Logging) {
                        logger = object : Logger {
                            override fun log(message: String) {
                                println(message)
                            }
                        }
                        level = LogLevel.BODY
                    }
                    install(DefaultRequest) {
                        accept(ContentType.Application.Xml)
                    }
                }
            ).getPlaylist().also {
                println(it)
            }
        }
    }
}