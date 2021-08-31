package com.tma.romanova.presentation.feature.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.tma.romanova.domain.navigation.NavigationManager
import com.tma.romanova.presentation.R
import com.tma.romanova.presentation.extensions.drawable
import com.tma.romanova.presentation.extensions.str
import com.tma.romanova.presentation.theme.ExtraLargeRadius
import com.tma.romanova.presentation.theme.LayoutLargeRLPadding
import com.tma.romanova.presentation.theme.LayoutSmallRLPadding
import com.tma.romanova.presentation.theme.appColors

@Composable
fun Player() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val (toolbar, guideline, bgBox, trackImage, trackTitle, authorName, waveForm, duration, buttonsBar) = createRefs()


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
            onBackButtonClick = {
                NavigationManager.navigateBack()
            },
            modifier = Modifier
                .constrainAs(toolbar) {
                    top.linkTo(parent.top, margin = LayoutSmallRLPadding)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = LayoutSmallRLPadding)
                }
        )

        TrackImage(
            modifier = Modifier
                .constrainAs(trackImage) {
                    top.linkTo(toolbar.bottom)
                    bottom.linkTo(trackTitle.top)
                    start.linkTo(parent.start, margin = LayoutLargeRLPadding)
                    end.linkTo(parent.end, margin = LayoutLargeRLPadding)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )

        TrackTitle(
            modifier = Modifier
                .constrainAs(trackTitle) {
                    bottom.linkTo(authorName.top, margin = 12.dp)
                    start.linkTo(parent.start, margin = LayoutLargeRLPadding)
                    end.linkTo(parent.end, margin = LayoutLargeRLPadding)
                    width = Dimension.fillToConstraints
                }
        )

        AuthorName(
            modifier = Modifier
                .constrainAs(authorName) {
                    bottom.linkTo(waveForm.top, margin = 44.dp)
                    start.linkTo(parent.start, margin = LayoutLargeRLPadding)
                    end.linkTo(parent.end, margin = LayoutLargeRLPadding)
                    width = Dimension.fillToConstraints
                }
        )

        WaveForm(
            modifier = Modifier
                .constrainAs(waveForm) {
                    bottom.linkTo(duration.top, margin = 6.dp)
                    start.linkTo(parent.start, margin = LayoutLargeRLPadding)
                    end.linkTo(parent.end, margin = LayoutLargeRLPadding)
                    width = Dimension.fillToConstraints
                }
        )

        Duration(
            modifier = Modifier
                .constrainAs(duration) {
                    bottom.linkTo(buttonsBar.top, margin = 34.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )

        ButtonsBar(
            modifier = Modifier
                .constrainAs(buttonsBar) {
                    bottom.linkTo(parent.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = LayoutLargeRLPadding)
                    end.linkTo(parent.end, margin = LayoutLargeRLPadding)
                    width = Dimension.fillToConstraints
                }
        )
    }

}

@Composable
fun Toolbar(
    onBackButtonClick: () -> Unit,
    modifier: Modifier
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
                imageVector = Icons.Rounded.ArrowBack,
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
            text = "Now Playing",
            style = TextStyle(
                color = MaterialTheme.appColors.onPrimary,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(
                    start = LayoutSmallRLPadding - 12.dp
                )
                .size((24 + 12 * 2).dp),
            onClick = onBackButtonClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_like),
                contentDescription = null,
                tint = MaterialTheme.appColors.onPrimary
            )
        }
    }
}

@Composable
fun ButtonsBar(
    modifier: Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_player_previous
                ),
                contentDescription = null
            )
        }
        Spacer(
            modifier = Modifier
                .weight(weight = 1F)
        )
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_player_back
                ),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(
                    start = 5.dp
                ),
                text = "-15",
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
                    onClick = { TODO() }
                )
                .size(60.dp),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = rememberDrawablePainter(
                    drawable = R.drawable.ic_play.drawable
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
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_player_up
                ),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(
                    end = 4.dp
                ),
                text = "+15",
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
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_player_next
                ),
                contentDescription = null
            )
        }
    }
}

@Composable
fun TrackImage(
    modifier: Modifier
){
    Card(
        modifier = modifier
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
                data = "https://i1.sndcdn.com/artworks-ZKEyONOnOFmgRZmg-lbS7QQ-t500x500.jpg",
                builder = {
                    crossfade(true)
                    diskCachePolicy(CachePolicy.ENABLED)
                    memoryCachePolicy(CachePolicy.ENABLED)
                }
            )
        )
    }
}

@Composable
fun TrackTitle(
    modifier: Modifier
){
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = "Уникальный экземпляр №1",
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
    modifier: Modifier
){
    Text(
        modifier = modifier,
        text = "Вера Романова",
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
    modifier: Modifier
){
    Text(
        modifier = modifier,
        text = "21:34",
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
    modifier: Modifier
){
    Box(
        modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(
                color = MaterialTheme.appColors.primary
            )
    )
}