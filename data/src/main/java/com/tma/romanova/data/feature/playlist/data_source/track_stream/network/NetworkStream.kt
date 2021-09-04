package com.tma.romanova.data.feature.playlist.data_source.track_stream.network

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.tma.romanova.data.BuildConfig
import com.tma.romanova.data.feature.playlist.data_source.track_stream.Stream
import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class NetworkStream(
    private val url: String
    ): Stream {
    private var mediaPlayer: MediaPlayer? = null

    private var channelScope: ProducerScope<Int>? = null
    override val currentPlayMsTimeFlow: Flow<Int>
    get() = callbackFlow{
        channelScope = this
        while (true) {
            if (mediaPlayer?.isPlaying == true) {
                kotlinx.coroutines.delay(100)
                val mediaPlayer = mediaPlayer
                if (mediaPlayer != null) {
                    trySend(
                        mediaPlayer.currentPosition
                    )
                } else {
                    close()
                    break

                }
            }
        }
        awaitClose{

        }
    }

    override val currentPlayMsTime: Result<Int>
        get() = try{
            Result.Success(
                data = mediaPlayer!!.currentPosition,
                dataSourceType = DataSourceType.Network
            )
        } catch (e: Exception){
            Result.LocalException(
                cause = e
            )
        }

    override val durationMs: Result<Int>
        get() = try{
            Result.Success(
                data = mediaPlayer!!.duration,
                dataSourceType = DataSourceType.Network
            )
        } catch (e: Exception){
            Result.LocalException(
                cause = e
            )
        }

    override fun prepareAudio(): Flow<Result<Unit>> = callbackFlow {
        mediaPlayer = MediaPlayer().apply {
            setDataSource("$url?client_id=${BuildConfig.CLIENT_ID}")
            setOnErrorListener { mediaPlayer, i, i2 -> 
                this@callbackFlow.trySend(
                    Result.NetworkError
                )
                close()
                false
            }
            setOnPreparedListener {
                this@callbackFlow.trySend(
                    Result.Success(data = Unit, dataSourceType = DataSourceType.Network)
                )
                close()
            }
            setAudioAttributes(
                AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            prepareAsync()
            awaitClose {

            }
        }
    }

    override fun playAudio(): Flow<Result<Unit>> = flow{
        mediaPlayer.let{
            try {
                it!!.start()
                emit(
                    Result.Success(
                        dataSourceType = DataSourceType.Network,
                        data = Unit
                    )
                )
            }catch (e: Exception){
                emit(
                    Result.LocalException(
                        cause = e
                    )
                )
            }
        }
    }

    override fun pauseAudio(): Flow<Result<Unit>> = flow{
        mediaPlayer.let{
            try {
                it!!.pause()
                emit(
                    Result.Success(
                        dataSourceType = DataSourceType.Network,
                        data = Unit
                    )
                )
            }catch (e: Exception){
                emit(
                    Result.LocalException(
                        cause = e
                    )
                )
            }
        }
    }

    override fun seekTo(ms: Int): Flow<Result<Unit>> = callbackFlow{
        mediaPlayer.let{
            try {
                it!!.seekTo(ms)
                it.setOnSeekCompleteListener {
                    channelScope?.trySend(
                        ms
                    )
                    trySend(
                        Result.Success(
                            dataSourceType = DataSourceType.Network,
                            data = Unit
                        )
                    )
                }
            }catch (e: Exception){
                trySend(
                    Result.LocalException(
                        cause = e
                    )
                )
            }finally {
                awaitClose {

                }
            }
        }
    }

    override fun endStream(): Flow<Result<Unit>> = flow{
        mediaPlayer.let{
            try {
                it!!.release()
                emit(
                    Result.Success(
                        dataSourceType = DataSourceType.Network,
                        data = Unit
                    )
                )
            }catch (e: Exception){
                emit(
                    Result.LocalException(
                        cause = e
                    )
                )
            }
        }
    }
}