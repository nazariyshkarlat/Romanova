package com.tma.romanova.presentation.feature.onboarding.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.tma.romanova.domain.navigation.NavigationDirections
import com.tma.romanova.domain.navigation.NavigationManager
import com.tma.romanova.presentation.custom_components.Pager
import com.tma.romanova.presentation.feature.onboarding.view_model.OnBoardingViewModel
import com.tma.romanova.presentation.theme.LargeRadius
import com.tma.romanova.presentation.theme.LayoutRLPadding
import com.tma.romanova.presentation.theme.appColors

@Composable
fun OnBoarding(viewModel: OnBoardingViewModel){

    val selectedItem = viewModel.selectedItem.collectAsState()

    var newSelectedIndex: Int? = remember{
        null
    }

    val onNextClick = {
        viewModel.notifyItemSelected(
            viewModel.nextItem
        )
        newSelectedIndex = viewModel.currentIndex
    }

    val onBeginClick = {
        viewModel.notifyOnBoardingCompleted()
    }

    val onSkipClick = {
        viewModel.notifyOnBoardingCompleted()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .weight(
                    weight = 1F
                ),
            shape = RoundedCornerShape(
                bottomStart = LargeRadius,
                bottomEnd = LargeRadius
            ),
            color = MaterialTheme.appColors.background,
            elevation = 12.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 60.dp
                    )
            ) {
                Pager(
                    items = viewModel.items,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    orientation = Orientation.Horizontal,
                    onItemSelect = {
                        viewModel.notifyItemSelected(it)
                        newSelectedIndex = viewModel.currentIndex
                    },
                    overshootFraction = .2F,
                    newSelectedIndex = newSelectedIndex,
                    itemFraction = 1f,
                    contentFactory = { item: OnBoardingViewModel.OnBoardingUi ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(
                                        start = 18.dp,
                                        end = 18.dp
                                    )
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                painter = rememberDrawablePainter(
                                    drawable = item.image
                                ),
                                contentDescription = null
                            )
                            Text(
                                style = TextStyle(
                                    color = MaterialTheme.appColors.onBackground,
                                    fontSize = 38.sp,

                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(
                                        top = 10.dp,
                                        start = 40.dp,
                                        end = 40.dp
                                    )
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                                text = item.title
                            )
                        }
                    }
                )
                Spacer(
                    modifier = Modifier
                        .weight(
                            weight = 1F
                        )
                )
                Row(
                    modifier = Modifier
                        .padding(
                            top = 13.dp,
                            bottom = 13.dp
                        )
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    viewModel.items.forEach {

                        val circleColor = animateColorAsState(
                            targetValue =
                            if (it == selectedItem.value) MaterialTheme.appColors.onBackground
                            else MaterialTheme.appColors.surface
                        )

                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    color = circleColor.value
                                )
                        )
                    }
                }
                Box(modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null,
                        onClick = if (viewModel.isLastItemSelected) onBeginClick
                        else onNextClick
                    ),
                ) {
                    Crossfade(
                        targetState = viewModel.isLastItemSelected,
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    ) {
                        Text(
                            style = TextStyle(
                                color = MaterialTheme.appColors.onBackground,
                                fontSize = 16.sp,

                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(
                                    top = 10.dp,
                                    bottom = 20.dp
                                )
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            text = if(it) "Начать"
                            else "Дальше",
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    onClick = onSkipClick
                )
        ) {
            Text(
                style = TextStyle(
                    color = MaterialTheme.appColors.onSurface,
                    fontSize = 14.sp,

                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(
                        top = 20.dp,
                        bottom = 20.dp
                    )
                    .wrapContentHeight()
                    .fillMaxWidth(),
                text = "Пропустить"
            )
        }
    }
}
