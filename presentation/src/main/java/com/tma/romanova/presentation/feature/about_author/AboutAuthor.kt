package com.tma.romanova.presentation.feature.about_author

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tma.romanova.domain.navigation.NavigationManager
import com.tma.romanova.presentation.theme.appColors
import com.tma.romanova.presentation.R
import com.tma.romanova.presentation.extensions.str
import com.tma.romanova.presentation.theme.ExtraLargeRadius
import com.tma.romanova.presentation.theme.LayoutSmallRLPadding

@Composable
fun AboutAuthor(){

    val onBackButtonClick = NavigationManager::navigateBack

    val onShareMessengerClick = {

    }
    val onShareTelegramClick = {

    }
    val onShareGeneralClick = {

    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(
                state = rememberScrollState()
            )
    ) {

        val (toolbar, guideline, bgBox, authorInfo, bodyText) = createRefs()

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

        Toolbar(
            onBackButtonClick = onBackButtonClick,
            modifier = Modifier
                .constrainAs(toolbar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )


        Divider(
            modifier = Modifier
                .height(0.dp)
                .constrainAs(guideline) {
                    top.linkTo(authorInfo.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(authorInfo.bottom)
                }
        )

        AuthorInfo(
            onShareMessengerClick = onShareMessengerClick,
            onShareTelegramClick = onShareTelegramClick,
            onShareGeneralClick = onShareGeneralClick,
            modifier = Modifier
                .constrainAs(authorInfo) {
                    top.linkTo(toolbar.bottom, margin = 34.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Text(
            text = R.string.about_author_body.str,
            modifier = Modifier
                .constrainAs(bodyText) {
                    top.linkTo(authorInfo.bottom, margin = 20.dp)
                    start.linkTo(parent.start, margin = LayoutSmallRLPadding)
                    end.linkTo(parent.end, margin = LayoutSmallRLPadding)
                    width = Dimension.fillToConstraints
                }
                .padding(
                    bottom = 20.dp
                ),
            style = TextStyle(
                color = MaterialTheme.appColors.primary,
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal
            )
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
            text = R.string.about_author_toolbar_title.str,
            style = TextStyle(
                color = MaterialTheme.appColors.onPrimary,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun AuthorInfo(
    onShareMessengerClick: () -> Unit,
    onShareTelegramClick: () -> Unit,
    onShareGeneralClick: () -> Unit,
    modifier: Modifier
){
    Row(
        modifier = modifier
            .padding(
                start = LayoutSmallRLPadding,
                end = LayoutSmallRLPadding
            )
            .height(160.dp)
            .fillMaxWidth()
    ){
        Image(
            modifier = Modifier
                .width(146.dp)
                .height(160.dp)
                .clip(
                    RoundedCornerShape(ExtraLargeRadius)
                ),
            painter = painterResource(id = R.mipmap.about_author),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(weight = 1F)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1F)
            ){
                Text(
                    modifier = Modifier
                        .padding(
                            bottom = 16.dp,
                            start = 20.dp
                        )
                        .align(Alignment.BottomStart)
                        .wrapContentSize(),
                    text = R.string.about_author_title.str,
                    style = TextStyle(
                        color = MaterialTheme.appColors.onPrimary,
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1F)
            ){
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            top = 10.dp,
                            start = 20.dp
                        ),
                    text = R.string.about_author_subtitle.str,
                    style = TextStyle(
                        color = MaterialTheme.appColors.secondary,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal
                    )
                )
                Row(
                    modifier = Modifier
                        .padding(start = (20 - 5).dp)
                        .wrapContentSize()
                ){
                    IconButton(
                        modifier = Modifier
                            .size(size = (24+5*2).dp),
                        onClick = onShareMessengerClick,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sharing_messenger),
                            contentDescription = null,
                            tint = MaterialTheme.appColors.secondary
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .padding(
                                start = 8.dp
                            )
                            .size(size = (24 + 5 * 2).dp),
                        onClick = onShareTelegramClick,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sharing_telegram),
                            contentDescription = null,
                            tint = MaterialTheme.appColors.secondary
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .padding(
                                start = 8.dp
                            )
                            .size(size = (24 + 5 * 2).dp),
                        onClick = onShareGeneralClick,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sharing_more),
                            contentDescription = null,
                            tint = MaterialTheme.appColors.secondary
                        )
                    }
                }
            }
        }
    }
}