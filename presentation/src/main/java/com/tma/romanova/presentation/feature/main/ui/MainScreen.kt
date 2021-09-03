package com.tma.romanova.presentation.feature.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.tma.romanova.domain.action.MainScreenClientAction
import com.tma.romanova.presentation.feature.main.entity.MainScreenTrackItemUi
import com.tma.romanova.presentation.feature.main.entity.NowPlayingTrackUi
import com.tma.romanova.presentation.feature.main.state.MainScreenUiState
import com.tma.romanova.presentation.feature.main.view_model.MainScreenViewModel
import com.tma.romanova.presentation.theme.*

@Composable
fun MainScreen(viewModel: MainScreenViewModel){

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val state = viewModel.uiState.collectAsState().value

        val onAboutAuthorClick = {
            viewModel.consumeClientAction(
                action = MainScreenClientAction.AboutAuthorItemClick
            )
        }

        AuthorItem(
            onClick = onAboutAuthorClick,
            aboutAuthorItem = state.aboutAuthorItem,
            modifier = Modifier.padding(
                top = 14.dp
            )
        )

        when(state){
            is MainScreenUiState.PlaylistIsLoading -> {

            }
            is MainScreenUiState.PlaylistLoadingError -> {

            }
            is MainScreenUiState.PlaylistLoadingSuccess -> {
                AlbumTitle(
                    title = state.playlistTitle,
                    modifier = Modifier.padding(
                        top = 28.dp
                    )
                )
                Podcasts(
                    tracks = state.tracks,
                    modifier = Modifier
                        .padding(
                            top = 20.dp
                        ),
                    onItemClick = { itemPosition->
                        viewModel.consumeClientAction(
                            MainScreenClientAction.TrackItemClick(
                                position = itemPosition
                            )
                        )
                    },
                    onItemLikeClick = { itemPosition->
                        viewModel.consumeClientAction(
                            MainScreenClientAction.LikeClick(
                                position = itemPosition
                            )
                        )
                    },
                    onItemCommentsClick = { itemPosition->
                        viewModel.consumeClientAction(
                            MainScreenClientAction.CommentsClick(
                                position = itemPosition
                            )
                        )
                    }
                )
            }
        }

        when(val nowPlaying = state.nowPlayingState){
            is MainScreenUiState.NowPlayingUiState.AudioIsPlaying -> {
                Spacer(
                    modifier = Modifier
                        .weight(weight = 1F)
                )
                NowPlaying(
                    nowPlaying = nowPlaying,
                    onButtonClick = {
                        viewModel.consumeClientAction(
                            MainScreenClientAction.NowPlayingTrackButtonClick
                        )
                    },
                    onClick = {
                        viewModel.consumeClientAction(
                            MainScreenClientAction.NowPlayingTrackClick
                        )
                    },
                    modifier = Modifier
                )
            }
            MainScreenUiState.NowPlayingUiState.NoAudioAvailable -> Unit
        }
    }

}

@Composable
fun AuthorItem(
    onClick: () -> Unit,
    aboutAuthorItem: MainScreenUiState.AboutAuthorItem,
    modifier: Modifier
){
    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = onClick
            )
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = LayoutLargeRLPadding,
                    end = LayoutLargeRLPadding
                )
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp),
                painter= rememberDrawablePainter(
                    drawable = aboutAuthorItem.authorImage
                ),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp
                    )
                    .wrapContentSize()
            ) {
                Text(
                    style = TextStyle(
                        color = MaterialTheme.appColors.secondary,
                        fontSize = 14.sp,

                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .wrapContentSize()
                        .offset(
                            y = (2).dp
                        ),
                    text = aboutAuthorItem.title,
                )
                Text(
                    style = TextStyle(
                        color = MaterialTheme.appColors.onBackground,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .wrapContentSize()
                        .offset(
                            y = (-2).dp
                        ),

                    text = aboutAuthorItem.authorName,
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(weight = 1F)
            )
            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Icon(
                    painter = rememberDrawablePainter(
                        drawable = aboutAuthorItem.goToIcon
                    ),
                    tint = MaterialTheme.appColors.accent,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun AlbumTitle(
    title: String,
    modifier: Modifier
){
    Text(
        style = TextStyle(
            color = MaterialTheme.appColors.onBackground,
            fontSize = 24.sp,

            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Start,
        modifier = modifier
            .padding(
                start = LayoutLargeRLPadding,
                end = LayoutLargeRLPadding
            )
            .wrapContentSize(),
        text = title,
    )
}

@Composable
fun Podcasts(
    tracks: List<MainScreenTrackItemUi>,
    modifier: Modifier,
    onItemClick: (position: Int) -> Unit,
    onItemLikeClick: (position: Int) -> Unit,
    onItemCommentsClick: (position: Int) -> Unit,
    ){
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentPadding = PaddingValues(
            start = LayoutLargeRLPadding,
            end = LayoutLargeRLPadding,
            top = 16.dp,
            bottom = 16.dp
        )
    ){
        tracks.forEachIndexed { idx, track->
            item(key = track.id) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    if(idx != 0)
                        Divider(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .width(14.dp),
                            color = Color.Transparent
                        )
                    PodcastItem(
                        podcastTrackItemUi = track,
                        onClick = {
                            onItemClick(idx)
                        },
                        onLikeClick = {
                            onItemLikeClick(idx)
                        },
                        onCommentsClick = {
                            onItemCommentsClick(idx)
                        },
                        modifier = Modifier
                    )
                    if(idx != tracks.lastIndex)
                        Divider(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .width(14.dp),
                            color = Color.Transparent
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PodcastItem(
    podcastTrackItemUi: MainScreenTrackItemUi,
    onClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentsClick: () -> Unit,
    modifier: Modifier
){
    Card(
        modifier = modifier
            .size(
                width = 200.dp,
                height = 246.dp
            ),
        onClick = onClick,
        shape = RoundedCornerShape(MediumRadius),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.appColors.background
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = rememberImagePainter(
                data = podcastTrackItemUi.artworkUrl,
                builder = {
                    placeholder(
                        drawable = podcastTrackItemUi.progressBarDrawable.apply {
                            setColorSchemeColors(MaterialTheme.appColors.accent.toArgb())
                        }
                    )
                    crossfade(true)
                    diskCachePolicy(CachePolicy.ENABLED)
                    memoryCachePolicy(CachePolicy.ENABLED)
                }
            ),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = (12 - 5).dp,
                    bottom = (12-5).dp
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = podcastTrackItemUi.title,
                textAlign = TextAlign.Start,
                style = TextStyle(
                    color = MaterialTheme.appColors.onPrimary,
                    fontSize = 16.sp,

                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Row(
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                    )
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = podcastTrackItemUi.duration,
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        color = MaterialTheme.appColors.onPrimary,
                        fontSize = 14.sp,

                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(
                    modifier = Modifier
                        .weight(weight = 1F)
                )
                IconButton(
                    modifier = Modifier
                        .then(Modifier.size(34.dp)),
                    onClick = onLikeClick
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = rememberDrawablePainter(
                                drawable = podcastTrackItemUi.likeButtonIcon
                            ),
                            tint = MaterialTheme.appColors.onPrimary,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(
                                bottom = 2.dp
                            ),
                            text = podcastTrackItemUi.likesText,
                            style = TextStyle(
                                color = if(podcastTrackItemUi.isOnPrimaryLikesTextColor) MaterialTheme.appColors.onBackground
                                    else MaterialTheme.appColors.onPrimary,
                                fontSize = 6.sp,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
                IconButton(
                    modifier = Modifier
                        .then(Modifier.size(34.dp)),
                    onClick = onCommentsClick
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = rememberDrawablePainter(
                                drawable = podcastTrackItemUi.commentButtonIcon
                            ),
                            tint = MaterialTheme.appColors.onPrimary,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(
                                bottom = 2.dp
                            ),
                            text = podcastTrackItemUi.commentsText,
                            style = TextStyle(
                                color = MaterialTheme.appColors.onPrimary,
                                fontSize = 6.sp,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NowPlaying(
    nowPlaying: MainScreenUiState.NowPlayingUiState.AudioIsPlaying,
    onButtonClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier
){
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = LayoutLargeRLPadding,
                    end = LayoutLargeRLPadding
                )
                .wrapContentSize(),
            text = nowPlaying.nowPlayingTitle,
            style = TextStyle(
                color = MaterialTheme.appColors.onBackground,
                fontSize = 18.sp,

                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
        )
        NowPlayingItem(
            nowPlayingTrackUi = nowPlaying.nowPlayingTrackUi,
            onButtonClick = onButtonClick,
            onClick = onClick,
            modifier = Modifier
                .padding(
                    top = 20.dp,
                    bottom = 44.dp,
                    start = LayoutLargeRLPadding-10.dp,
                    end = LayoutLargeRLPadding-10.dp
                )
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NowPlayingItem(
    nowPlayingTrackUi: NowPlayingTrackUi,
    onButtonClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = onClick,
        elevation = 4.dp,
        shape = RoundedCornerShape(size = MediumRadius),
        backgroundColor = MaterialTheme.appColors.background
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 6.dp,
                    bottom = 6.dp
                )
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(size = SmallRadius)),
                painter = rememberImagePainter(
                    data = nowPlayingTrackUi.artworkUrl,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = 10.dp
                    )
                    .weight(weight = 1F),
                text = nowPlayingTrackUi.trackTitle,
                style = TextStyle(
                    color = MaterialTheme.appColors.onBackground,
                    fontSize = 14.sp,

                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Box(
                modifier = Modifier
                    .padding(
                        start = 32.dp
                    )
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.appColors.onBackground
                    )
                    .clickable(
                        onClick = onButtonClick
                    ),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = rememberDrawablePainter(
                        drawable = nowPlayingTrackUi.buttonIcon
                    ),
                    tint = MaterialTheme.appColors.onPrimary,
                    contentDescription = null
                )
            }
        }
    }
}
