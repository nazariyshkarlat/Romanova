package com.tma.romanova.presentation.feature.player.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.tma.romanova.domain.action.PlayerClientAction
import com.tma.romanova.domain.action.TouchAction
import com.tma.romanova.domain.action.playerClientAction
import com.tma.romanova.domain.state.feature.player.WaveFormValuesStatus
import com.tma.romanova.presentation.custom_components.DirectionPriority
import com.tma.romanova.presentation.custom_components.Pager
import com.tma.romanova.presentation.custom_components.ScrollBehavior
import com.tma.romanova.presentation.extensions.toRgb
import com.tma.romanova.presentation.feature.player.state.PlayerUiState
import com.tma.romanova.presentation.feature.player.state.PlayerUiState.Companion.likeIcon
import com.tma.romanova.presentation.feature.player.state.TrackPlayerUi
import com.tma.romanova.presentation.feature.player.view_model.PlayerViewModel
import com.tma.romanova.presentation.theme.ExtraLargeRadius
import com.tma.romanova.presentation.theme.LayoutLargeRLPadding
import com.tma.romanova.presentation.theme.LayoutSmallRLPadding
import com.tma.romanova.presentation.theme.appColors

@Composable
fun Player(
    viewModel: PlayerViewModel
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val onLikeButtonClick = {
            viewModel.consumeClientAction(
                action = PlayerClientAction.LikeClick
            )
        }

        val onRectanglesCountMeasured: (Int) -> Unit = { rectCount ->
            viewModel.consumeClientAction(
                action = PlayerClientAction.WaveFormPartsCountMeasured(
                    partsCount = rectCount
                )
            )
        }

        val onPrevTrackButtonClick = {
            viewModel.consumeClientAction(
                action = PlayerClientAction.ToPreviousTrackClick
            )
        }

        val onNextTrackButtonClick = {
            viewModel.consumeClientAction(
                action = PlayerClientAction.ToNextTrackClick
            )
        }

        val onTimeUpButtonClick = {
            viewModel.consumeClientAction(
                action = PlayerClientAction.TimeUpClick
            )
        }

        val onTimeBackButtonClick = {
            viewModel.consumeClientAction(
                action = PlayerClientAction.TimeBackClick
            )
        }

        val onCenterButtonClick = {
            viewModel.consumeClientAction(
                action = PlayerClientAction.CenterButtonClick
            )
        }

        val onBackButtonClick = {
            viewModel.consumeClientAction(
                action = PlayerClientAction.BackButtonClick
            )
        }

        val onWaveFormTouch: (TouchAction) -> Unit = {
            viewModel.consumeClientAction(
                action = it.playerClientAction
            )
        }


        val (toolbar, guideline, bgBox, trackImage, trackTitle, authorName, waveForm, duration, buttonsBar) = createRefs()

        val state = viewModel.uiState.collectAsState().value

        when(state){
            is PlayerUiState.TrackLoaded -> {

                val onTrackImageScroll: (TrackPlayerUi) -> Unit = {
                    viewModel.consumeClientAction(
                        action = PlayerClientAction.OnTrackImageScrolled(
                            newTrackPosition = state.allTracks.indexOf(it)
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .constrainAs(bgBox) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(guideline.top)
                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                        }
                        .background(
                            color = MaterialTheme.appColors.primary
                        )
                )

                Divider(
                    modifier = Modifier
                        .constrainAs(guideline) {
                            top.linkTo(trackImage.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(trackImage.bottom)
                        }
                        .height(0.dp)
                )

                Toolbar(
                    onBackButtonClick = onBackButtonClick,
                    modifier = Modifier
                        .constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    likeButtonIcon = state.currentTrack.likeIcon,
                    onLikeButtonClick = onLikeButtonClick,
                    toolbarTitle = PlayerUiState.toolbarTitle
                )

                TrackImage(
                    modifier = Modifier
                        .constrainAs(trackImage) {
                            top.linkTo(toolbar.bottom)
                            bottom.linkTo(trackTitle.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                        },
                    tracks = state.allTracks,
                    onItemSelect = onTrackImageScroll,
                    selectedIndex = state.selectedTrackPosition,
                    desiredTrackPosition = state.desiredTrackPosition
                )

                TrackTitle(
                    modifier = Modifier
                        .constrainAs(trackTitle) {
                            bottom.linkTo(authorName.top, margin = 12.dp)
                            start.linkTo(parent.start, margin = LayoutLargeRLPadding)
                            end.linkTo(parent.end, margin = LayoutLargeRLPadding)
                            width = Dimension.fillToConstraints
                        },
                    title = state.currentTrack.trackTitle
                )

                AuthorName(
                    modifier = Modifier
                        .constrainAs(authorName) {
                            bottom.linkTo(waveForm.top, margin = 44.dp)
                            start.linkTo(parent.start, margin = LayoutLargeRLPadding)
                            end.linkTo(parent.end, margin = LayoutLargeRLPadding)
                            width = Dimension.fillToConstraints
                        },
                    authorName = PlayerUiState.authorName
                )

                println(state.playedPercent)
                WaveForm(
                    modifier = Modifier
                        .constrainAs(waveForm) {
                            bottom.linkTo(duration.top, margin = 6.dp)
                            start.linkTo(parent.start, margin = LayoutLargeRLPadding)
                            end.linkTo(parent.end, margin = LayoutLargeRLPadding)
                            width = Dimension.fillToConstraints
                            height = Dimension.value(52.dp)
                        },
                    onRectanglesCountMeasured = onRectanglesCountMeasured,
                    values = (state.waveFormValuesStatus as? WaveFormValuesStatus.ValuesReceived)?.values ?: emptyList(),
                    filledPercent = state.playedPercent,
                    onTouchAction = onWaveFormTouch
                )

                Duration(
                    modifier = Modifier
                        .constrainAs(duration) {
                            bottom.linkTo(buttonsBar.top, margin = 34.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    durationStr = state.currentTimePosition
                )

                ButtonsBar(
                    modifier = Modifier
                        .constrainAs(buttonsBar) {
                            bottom.linkTo(parent.bottom, margin = 20.dp)
                            start.linkTo(parent.start, margin = LayoutLargeRLPadding)
                            end.linkTo(parent.end, margin = LayoutLargeRLPadding)
                            width = Dimension.fillToConstraints
                        },
                    onCenterButtonClick = onCenterButtonClick,
                    onNextTrackClick = onNextTrackButtonClick,
                    onPrevTrackClick = onPrevTrackButtonClick,
                    onTimeBackClick = onTimeBackButtonClick,
                    onTimeUpClick = onTimeUpButtonClick,
                    timeBackIconText = PlayerUiState.timeBackText,
                    timeUpIconText = PlayerUiState.timeUpText,
                    centerButtonIcon = state.currentTrack.centerButtonIcon,
                    nextTrackIcon = PlayerUiState.nextTrackIcon,
                    prevTrackIcon = PlayerUiState.prevBackIcon,
                    timeBackIcon = PlayerUiState.timeBackIcon,
                    timeUpIcon = PlayerUiState.timeUpIcon
                )

            }
            PlayerUiState.TrackLoading -> {


            }
        }
    }

}

@Composable
fun Toolbar(
    onBackButtonClick: () -> Unit,
    onLikeButtonClick: () -> Unit,
    modifier: Modifier,
    toolbarTitle: String,
    likeButtonIcon: Drawable
)
{
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(
                    start = LayoutSmallRLPadding - 12.dp
                )
                .size((24 + 12 * 2).dp),
            onClick = onBackButtonClick
        ) {
            Icon(
                painter = rememberDrawablePainter(drawable = PlayerUiState.backButtonIcon),
                contentDescription = null,
                tint = MaterialTheme.appColors.onPrimary
            )
        }
        Text(
            modifier = Modifier
                .padding(
                    top = 12.dp,
                    bottom = 12.dp
                )
                .align(Alignment.Center)
                .wrapContentSize(),
            text = toolbarTitle,
            style = TextStyle(
                color = MaterialTheme.appColors.onPrimary,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
        )
/*        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(
                    end = LayoutSmallRLPadding - 12.dp
                )
                .size((24 + 12 * 2).dp),
            onClick = onLikeButtonClick
        ) {
            Icon(
                painter = rememberDrawablePainter(
                    drawable = likeButtonIcon
                ),
                contentDescription = null,
                tint = MaterialTheme.appColors.onPrimary
            )
        }*/
    }
}

@Composable
fun ButtonsBar(
    modifier: Modifier,
    onPrevTrackClick: () -> Unit,
    onNextTrackClick: () -> Unit,
    onTimeBackClick: () -> Unit,
    onTimeUpClick: () -> Unit,
    onCenterButtonClick: () -> Unit,
    prevTrackIcon: Drawable,
    nextTrackIcon: Drawable,
    timeUpIcon: Drawable,
    timeBackIcon: Drawable,
    centerButtonIcon: Drawable,
    timeUpIconText: String,
    timeBackIconText: String
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = onPrevTrackClick,
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter = rememberDrawablePainter(
                    drawable = prevTrackIcon
                ),
                contentDescription = null
            )
        }
        Spacer(
            modifier = Modifier
                .weight(weight = 1F)
        )
        IconButton(
            onClick = onTimeBackClick,
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter = rememberDrawablePainter(
                    drawable = timeBackIcon
                ),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(
                    start = 5.dp
                ),
                text = timeBackIconText,
                style = TextStyle(
                    color = MaterialTheme.appColors.onBackground,
                    fontSize = 6.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        Spacer(
            modifier = Modifier
                .weight(weight = 1F)
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.appColors.onBackground
                )
                .clickable(
                    onClick = onCenterButtonClick
                )
                .size(60.dp),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = rememberDrawablePainter(
                    drawable = centerButtonIcon
                ),
                tint = MaterialTheme.appColors.onPrimary,
                contentDescription = null
            )
        }
        Spacer(
            modifier = Modifier
                .weight(weight = 1F)
        )
        IconButton(
            onClick = onTimeUpClick,
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter = rememberDrawablePainter(
                    drawable = timeUpIcon
                ),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(
                    end = 4.dp
                ),
                text = timeUpIconText,
                style = TextStyle(
                    color = MaterialTheme.appColors.onBackground,
                    fontSize = 6.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        Spacer(
            modifier = Modifier
                .weight(weight = 1F)
        )
        IconButton(
            onClick = onNextTrackClick,
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter = rememberDrawablePainter(
                    drawable = nextTrackIcon
                ),
                contentDescription = null
            )
        }
    }
}

@Composable
fun TrackImage(
    modifier: Modifier,
    tracks: List<TrackPlayerUi>,
    selectedIndex: Int,
    onItemSelect: (TrackPlayerUi) -> Unit,
    desiredTrackPosition: Pair<Int, DirectionPriority>
) {
    val tracks = remember {tracks}
    Pager(
        items = tracks,
        modifier = modifier,
        orientation = Orientation.Horizontal,
        onItemSelect = onItemSelect,
        hasInfinitePages = true,
        overshootFraction = .2F,
        newSelectedIndex = desiredTrackPosition,
        initialIndex = selectedIndex,
        itemFraction = 1f,
        scrollBehavior = ScrollBehavior.FULLY_SCROLLED,
        contentFactory = { track: TrackPlayerUi ->
            Card(
                modifier = modifier
                    .padding(
                        start = LayoutLargeRLPadding,
                        end = LayoutLargeRLPadding
                    )
                    .aspectRatio(
                        ratio = 0.92F,
                    ),
                shape = RoundedCornerShape(
                    size = ExtraLargeRadius
                )
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    painter = rememberImagePainter(
                        data = track.artworkUrl,
                        builder = {
                            crossfade(true)
                            diskCachePolicy(CachePolicy.ENABLED)
                            memoryCachePolicy(CachePolicy.ENABLED)
                        }
                    )
                )
            }
        }
    )
}

data class Drawable1(val drawable: Drawable)

@Composable
fun TrackTitle(
    modifier: Modifier,
    title: String
){
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = title,
        style = TextStyle(
            color = MaterialTheme.appColors.primary,
            fontSize = 24.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun AuthorName(
    modifier: Modifier,
    authorName: String
){
    Text(
        modifier = modifier,
        text = authorName,
        textAlign = TextAlign.Center,
        style = TextStyle(
            color = MaterialTheme.appColors.secondary,
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Light
        )
    )
}

@Composable
fun Duration(
    modifier: Modifier,
    durationStr: String
){
    Text(
        modifier = modifier,
        text = durationStr,
        textAlign = TextAlign.Center,
        style = TextStyle(
            color = MaterialTheme.appColors.secondary,
            fontSize = 14.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Light
        )
    )
}

@Composable
fun WaveForm(
    modifier: Modifier,
    values: List<Float>,
    filledPercent: Float,
    onRectanglesCountMeasured: (Int) -> Unit,
    onTouchAction: (TouchAction) -> Unit
){
    Box(modifier = modifier) {
        WaveForm(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            values = values,
            targetHeight = 52.dp,
            waveFormStyle = WaveFormStyle(
                unfilledColor = MaterialTheme.appColors.surface.toRgb(
                    backgroundColor = MaterialTheme.appColors.background
                ),
                filledColor = MaterialTheme.appColors.primary,
                rectanglesCornerRadius = ExtraLargeRadius,
                rectanglesPadding = 3.dp,
                rectangleDesiredWidth = 4.dp
            ),
            filledPercent = filledPercent,
            onRectanglesCountMeasured = onRectanglesCountMeasured,
            onTouchAction = onTouchAction
        )
    }
}