package com.tma.romanova.presentation.feature.player.ui

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.MotionEvent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tma.romanova.domain.action.TouchAction
import com.tma.romanova.presentation.extensions.androidColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WaveForm(
    targetHeight: Dp,
    modifier: Modifier,
    values: List<Float>,
    waveFormStyle: WaveFormStyle,
    filledPercent: Float,
    onRectanglesCountMeasured: (Int) -> Unit = {},
    onTouchAction: (TouchAction) -> Unit = {}
){

    val state = remember(filledPercent, values){
        WaveFormState(
            filledPercent = filledPercent,
            values = values
        )
    }

    var currentHeight: Dp by remember{ mutableStateOf(0.dp) }

    val values = state.values
    val filledPercent = state.filledPercent

    val animToValue = if (values.isEmpty()) (targetHeight.div(10.dp)).dp else targetHeight

    val height: Dp by animateDpAsState(animToValue, animationSpec = tween(
        durationMillis = if(currentHeight > animToValue) 0 else 500,
        easing = FastOutSlowInEasing
    ))

    BoxWithConstraints(modifier = modifier.height(height = height)) {
        val width = this.maxWidth
        currentHeight = this.maxHeight
        val rectDesiredWidth = waveFormStyle.rectangleDesiredWidth
        val rectPadding = waveFormStyle.rectanglesPadding
        val cornerRadius = waveFormStyle.rectanglesCornerRadius

        val desiredRectCount = remember(key1 = width, key2 = values, key3 = rectPadding) {
            ((width - rectPadding) / (rectPadding + rectDesiredWidth)).toInt()
        }

        DisposableEffect(key1 = values) {
            if(values.isEmpty()) {
                onRectanglesCountMeasured(
                    desiredRectCount
                )
            }

            onDispose {

            }
        }

        if(currentHeight == 0.dp) return@BoxWithConstraints

        if (values.isNotEmpty()) {
            val density = LocalDensity.current

            val rectCount = values.size
            val rectWidth = remember(key1 = width, key2 = values, key3 = rectPadding){
                ((width)-rectPadding*(rectCount-1))/(rectCount)
            }

            val widthPx = with(LocalDensity.current) { width.toPx() }.toInt()
            val heightPx = with(LocalDensity.current) { currentHeight.toPx() }.toInt()

            val maskPaint = remember(
                key1 = waveFormStyle.unfilledColor.value
            ) {
                Paint().apply {
                    color = waveFormStyle.unfilledColor.androidColor
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }
            }

            val fillPaint = remember(
                key1 = waveFormStyle.filledColor.value
            ) {
                Paint().apply {
                    color = waveFormStyle.filledColor.androidColor
                    style = Paint.Style.FILL
                }
            }

            val rectBitmapPaint = remember {
                Paint().apply {
                    xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
                }
            }

            val maskBitmap = remember(currentHeight) {
                Bitmap.createBitmap(
                    widthPx, heightPx, Bitmap.Config.ARGB_8888
                )
            }
            val maskCanvas = remember(currentHeight) {
                android.graphics.Canvas(maskBitmap)
            }

            DisposableEffect(width, values, rectPadding, currentHeight) {
                maskCanvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                values.forEachIndexed { idx, factor ->
                    with(density) {
                        maskCanvas.drawRoundRect(
                            ((rectPadding + rectWidth) * idx).toPx(),
                            currentHeight.toPx() * ((1F - factor) / 2F),
                            ((rectPadding + rectWidth) * idx + rectWidth).toPx(),
                            currentHeight.toPx() - currentHeight.toPx() * ((1F - factor) / 2F),
                            cornerRadius.toPx(),
                            cornerRadius.toPx(),
                            maskPaint
                        )
                    }
                }
                onDispose {

                }
            }
            val rectBitmap = remember(currentHeight) {
                Bitmap.createBitmap(
                    widthPx, heightPx, Bitmap.Config.ARGB_8888
                )
            }

            val rectCanvas = remember(currentHeight) {
                android.graphics.Canvas(rectBitmap)
            }

            val bufferBitmap = remember(currentHeight) {
                Bitmap.createBitmap(
                    widthPx, heightPx, Bitmap.Config.ARGB_8888
                )
            }

            val bufferCanvas = remember(currentHeight) {
                android.graphics.Canvas(bufferBitmap)
            }

            val scaledWidth = remember(rectCount, rectPadding, width){
                width-rectPadding*(rectCount-1)
            }

            val filledScaledWidth = remember(scaledWidth, filledPercent){
                scaledWidth*filledPercent
            }

            val filledRectanglesCount = (remember(filledPercent){
                filledScaledWidth/rectWidth
            }).toInt()

            val paddingAdditionalWidth = remember(rectPadding, filledRectanglesCount){
                rectPadding * (filledRectanglesCount)
            }

            DisposableEffect(key1 = filledPercent, key2 = currentHeight) {
                rectCanvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                with(density) {
                    rectCanvas.drawRect(
                        0F,
                        0F,
                        (filledScaledWidth+paddingAdditionalWidth).toPx(),
                        heightPx.toFloat(),
                        fillPaint
                    )
                }
                onDispose {

                }
            }

            Canvas(
                modifier = Modifier
                    .pointerInteropFilter {
                        val position = (it.x).coerceIn(
                            0F, widthPx.toFloat()
                        ) / widthPx.toFloat()
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                onTouchAction(
                                    TouchAction.DownAction(
                                        position = position
                                    )
                                )
                                return@pointerInteropFilter true
                            }
                            MotionEvent.ACTION_MOVE -> {
                                onTouchAction(
                                    TouchAction.MoveAction(
                                        position = position
                                    )
                                )
                                return@pointerInteropFilter true
                            }
                            MotionEvent.ACTION_CANCEL -> {
                                onTouchAction(
                                    TouchAction.CancelAction
                                )
                                return@pointerInteropFilter true
                            }
                            MotionEvent.ACTION_UP -> {
                                onTouchAction(
                                    TouchAction.UpAction(
                                        position = position
                                    )
                                )
                                return@pointerInteropFilter true
                            }
                            else -> {
                                return@pointerInteropFilter false
                            }
                        }
                    }
                    .fillMaxSize()
            ) {
                bufferCanvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                bufferCanvas.drawBitmap(rectBitmap, 0F, 0F, null)
                bufferCanvas.drawBitmap(maskBitmap, 0F, 0F, rectBitmapPaint)
                drawImage(
                    image = bufferBitmap.asImageBitmap()
                )
            }
        }
    }
}

data class WaveFormStyle(
    val unfilledColor: Color,
    val filledColor: Color,
    val rectanglesPadding: Dp,
    val rectanglesCornerRadius: Dp,
    val rectangleDesiredWidth: Dp
)

data class WaveFormState(
    val filledPercent: Float,
    val values: List<Float>
)