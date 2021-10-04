package com.tma.romanova.data.feature.track_stream.network

import com.google.android.exoplayer2.*
import com.tma.romanova.core.application
import com.tma.romanova.domain.feature.track_stream.PlayingEvent
import com.tma.romanova.domain.feature.track_stream.Stream
import com.tma.romanova.domain.feature.track_stream._playingEvent
import com.tma.romanova.domain.result.DataSourceType
import com.tma.romanova.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

private var mediaPlayer: ExoPlayer? = null

class NetworkStream(
    private val url: String,
    override val trackId: Int
    ): Stream(trackId) {

    companion object{
        const val PREPARE_RETRY_COUNT = 3
    }

    private var listener: Player.Listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when(playbackState){

                Player.STATE_BUFFERING -> {

                }
                Player.STATE_ENDED -> {
                    _playingEvent.tryEmit(
                        PlayingEvent.TrackEnd
                    )
                }
                Player.STATE_IDLE -> {

                }
                Player.STATE_READY -> {

                }
            }
        }
    }

    private var channelScope: ProducerScope<Long>? = null

    private var actionsScope: CoroutineContext? = null

    override val currentPlayMsTimeFlow: Flow<Long>
    get() = callbackFlow{
        channelScope = this
        while (true) {
            kotlinx.coroutines.delay(100)
            if (mediaPlayer?.isPlaying == true) {
                val mediaPlayer = mediaPlayer
                if (mediaPlayer != null) {
                    if(mediaPlayer.currentPosition <= mediaPlayer.duration) {
                        trySend(
                            mediaPlayer.currentPosition
                        )
                    }
                } else {
                    close()
                    break

                }
            }
        }
        awaitClose{

        }
    }

    override val currentPlayMsTime: Result<Long>
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

    override val durationMs: Result<Long>
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

    override fun prepareAudio(playWhenPrepared: Boolean): Flow<Result<Unit>>{
        return callbackFlow {

            actionsScope?.cancel()
            actionsScope = this.coroutineContext

            if(mediaPlayer == null) {
                mediaPlayer = SimpleExoPlayer.Builder(application).build()
            }

            mediaPlayer!!.also { player ->

                player.addListener(listener)

                val prepareListener = object : Player.Listener {

                    var retryCount = PREPARE_RETRY_COUNT

                    override fun onPlayerError(error: PlaybackException) {
                        super.onPlayerError(error)
                        if (retryCount > 0) {
                            player.seekTo(0, 0)
                            player.prepare()
                        } else {
                            this@callbackFlow.trySend(
                                Result.NetworkError
                            )
                            close()
                        }
                        retryCount--
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {

                            }
                            Player.STATE_ENDED -> {
                            }
                            Player.STATE_IDLE -> {

                            }
                            Player.STATE_READY -> {
                                _playingEvent.tryEmit(
                                    PlayingEvent.TrackPlayingStart(
                                        trackId = trackId
                                    )
                                )
                                this@callbackFlow.trySend(
                                    Result.Success(
                                        data = Unit,
                                        dataSourceType = DataSourceType.Network
                                    )
                                )
                                close()
                            }
                        }
                    }
                }.also { listener->

                    player.playWhenReady = playWhenPrepared
                    player.addListener(listener)
                    player.clearMediaItems()
                    val mediaItem = MediaItem.fromUri(url)
                    player.setMediaItem(mediaItem)
                    player.seekTo(0, 0)
                    player.prepare()
                }
                awaitClose {
                    mediaPlayer?.removeListener(prepareListener)
                }
            }
        }.flowOn(Dispatchers.Main)
    }

    override fun playAudio(): Flow<Result<Unit>> = flow{

        actionsScope?.cancel()
        actionsScope = currentCoroutineContext()

        mediaPlayer.let{
            try {
                kotlinx.coroutines.delay(1000)
                it!!.play()
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
    }.flowOn(Dispatchers.Main)

    override fun pauseAudio(): Flow<Result<Unit>> = flow{

        actionsScope?.cancel()
        actionsScope = currentCoroutineContext()

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
    }.flowOn(Dispatchers.Main)

    override fun seekTo(ms: Long): Flow<Result<Unit>> = callbackFlow{

        actionsScope?.cancel()
        actionsScope = currentCoroutineContext()

        mediaPlayer.let{

            val listener = object : Player.Listener{
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when(playbackState){

                        Player.STATE_BUFFERING -> {

                        }
                        Player.STATE_ENDED -> {

                        }
                        Player.STATE_IDLE -> {

                        }
                        Player.STATE_READY -> {
                            trySend(
                                Result.Success(
                                    dataSourceType = DataSourceType.Network,
                                    data = Unit
                                )
                            )
                            close()
                        }
                    }
                }
            }

            try {
                it!!.addListener(listener)
                it.seekTo(ms)
                channelScope?.trySend(
                    ms
                )
            }catch (e: Exception){
                e.printStackTrace()
                trySend(
                    Result.LocalException(
                        cause = e
                    )
                )
            }finally {
                awaitClose {
                    it?.removeListener(listener)
                }
            }
        }
    }.flowOn(Dispatchers.Main)

    override fun closeStream(){
        channelScope?.cancel()
        actionsScope?.cancel()
        mediaPlayer?.stop()
        try{
            listener.let { mediaPlayer?.removeListener(it) }
        }catch (e: Exception){}
    }
}