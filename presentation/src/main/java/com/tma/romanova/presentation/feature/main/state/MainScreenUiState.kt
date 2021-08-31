package com.tma.romanova.presentation.feature.main.state

import android.graphics.drawable.Drawable
import com.tma.romanova.domain.state.MainScreenState
import com.tma.romanova.presentation.R
import com.tma.romanova.presentation.extensions.drawable
import com.tma.romanova.presentation.extensions.str
import com.tma.romanova.presentation.extensions.string
import com.tma.romanova.presentation.feature.main.entity.MainScreenTrackItemUi
import com.tma.romanova.presentation.feature.main.entity.NowPlayingTrackUi
import com.tma.romanova.presentation.feature.main.entity.mainScreenTrackUi
import com.tma.romanova.presentation.feature.main.entity.toNowPlayingTrackUi

sealed class MainScreenUiState {

    abstract val nowPlayingState: NowPlayingUiState
    val aboutAuthorItem: AboutAuthorItem by lazy {AboutAuthorItem.aboutAuthor}

    data class PlaylistIsLoading(
        override val nowPlayingState: NowPlayingUiState
    ) : MainScreenUiState()

    data class PlaylistLoadingError(
        override val nowPlayingState: NowPlayingUiState,
        val errorText: String
    ) : MainScreenUiState()

    data class PlaylistLoadingSuccess(
        override val nowPlayingState: NowPlayingUiState,
        val tracks: List<MainScreenTrackItemUi>,
        val playlistTitle: String
    ) : MainScreenUiState()

    sealed class NowPlayingUiState{
        object NoAudioAvailable: NowPlayingUiState()
        data class AudioIsPlaying(
            val nowPlayingTrackUi: NowPlayingTrackUi
            ): NowPlayingUiState(){
                val nowPlayingTitle by lazy{
                    R.string.now_playing_title.str
                }
            }
    }

    data class AboutAuthorItem(
        val title: String,
        val authorName: String,
        val authorImage: Drawable,
        val goToIcon: Drawable
    ){
        companion object{
            val aboutAuthor by lazy{
                AboutAuthorItem(
                    title = R.string.main_screen_about_author_title.str,
                    authorName = R.string.about_author_author_name.str,
                    authorImage = R.mipmap.author_small.drawable,
                    goToIcon = R.drawable.ic_chevrone_right.drawable
                )
            }
        }
    }

}

val MainScreenState.ui
get() = when(this){
    is MainScreenState.PlaylistIsLoading -> {
        MainScreenUiState.PlaylistIsLoading(
            nowPlayingState = nowPlayingState.ui
        )
    }
    is MainScreenState.PlaylistLoadingError -> {
        MainScreenUiState.PlaylistLoadingError(
            errorText = errorCause.string,
            nowPlayingState = nowPlayingState.ui
        )
    }
    is MainScreenState.PlaylistLoadingSuccess -> {
        MainScreenUiState.PlaylistLoadingSuccess(
            nowPlayingState = nowPlayingState.ui,
            tracks = playlist.tracks.map{ it.mainScreenTrackUi },
            playlistTitle = playlist.title
        )
    }
}

val MainScreenState.NowPlayingState.ui
get() = when(this){
    is MainScreenState.NowPlayingState.AudioIsPlaying -> {
        MainScreenUiState.NowPlayingUiState.AudioIsPlaying(
            nowPlayingTrackUi = track.toNowPlayingTrackUi(
                isOnPause = isOnPause
            )
        )
    }
    MainScreenState.NowPlayingState.NoAudioAvailable -> MainScreenUiState.NowPlayingUiState.NoAudioAvailable
}