package com.tma.romanova.presentation.feature.player.ui

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.tma.romanova.presentation.extensions.androidColor

@Composable
fun WaveForm(
    modifier: Modifier,
    values: List<Float>,
    waveFormStyle: WaveFormStyle,
    filledPercent: Float,
    onRectanglesCountMeasured: (Int) -> Unit
){

    val state = remember(filledPercent, values){
        WaveFormState(
            filledPercent = filledPercent,
            values = values
        )
    }

    val values = state.values
    val filledPercent = state.filledPercent

    BoxWithConstraints(modifier = modifier) {
        val width = this.maxWidth
        val height = this.maxHeight
        val rectDesiredWidth = waveFormStyle.rectangleDesiredWidth
        val rectPadding = waveFormStyle.rectanglesPadding
        val cornerRadius = waveFormStyle.rectanglesCornerRadius

        val desiredRectCount = remember(key1 = width, key2 = values, key3 = rectPadding) {
            ((width - rectPadding) / (rectPadding + rectDesiredWidth)).toInt()
        }

        DisposableEffect(key1 = desiredRectCount) {
            onRectanglesCountMeasured(
                desiredRectCount
            )
            onDispose {

            }
        }

        println(filledPercent)

        if (values.isNotEmpty()) {
            val density = LocalDensity.current

            val rectCount = values.size
            val rectWidth = remember(key1 = width, key2 = values, key3 = rectPadding){
                ((width-rectPadding)-rectPadding*rectCount)/(rectCount)
            }


            val drawRLPadding = remember(key1 = width, key2 = values, key3 = rectPadding) {
                (width - ((rectPadding + rectWidth) * rectCount - rectPadding)) / 2F
            }

            val widthPx = with(LocalDensity.current) { width.toPx() }.toInt()
            val heightPx = with(LocalDensity.current) { height.toPx() }.toInt()

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

            val maskBitmap = remember {
                Bitmap.createBitmap(
                    widthPx, heightPx, Bitmap.Config.ARGB_8888
                )
            }
            val maskCanvas = remember {
                android.graphics.Canvas(maskBitmap)
            }

            DisposableEffect(key1 = width, key2 = values, key3 = rectPadding) {
                maskCanvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                values.forEachIndexed { idx, factor ->
                    with(density) {
                        maskCanvas.drawRoundRect(
                            (drawRLPadding + (rectPadding + rectWidth) * idx).toPx(),
                            height.toPx() * ((1F - factor) / 2F),
                            (drawRLPadding + (rectPadding + rectWidth) * idx + rectWidth).toPx(),
                            height.toPx() - height.toPx() * ((1F - factor) / 2F),
                            cornerRadius.toPx(),
                            cornerRadius.toPx(),
                            maskPaint
                        )
                    }
                }
                onDispose {

                }
            }
            val rectBitmap = remember {
                Bitmap.createBitmap(
                    widthPx, heightPx, Bitmap.Config.ARGB_8888
                )
            }

            val rectCanvas = remember {
                android.graphics.Canvas(rectBitmap)
            }

            val bufferBitmap = remember {
                Bitmap.createBitmap(
                    widthPx, heightPx, Bitmap.Config.ARGB_8888
                )
            }

            val bufferCanvas = remember {
                android.graphics.Canvas(bufferBitmap)
            }

            val filledRectanglesCount = (remember(filledPercent){
                (width*filledPercent)/(rectWidth+rectPadding)
            }).toInt()

            val linesBeforeAdditionalWidth = remember(rectPadding, filledRectanglesCount){
                rectPadding * (filledRectanglesCount)
            }

            val filledWidth = remember(filledPercent, rectCount, rectPadding, width){
                (width-rectPadding*(rectCount-1))*filledPercent
            }

            DisposableEffect(key1 = filledPercent) {
                rectCanvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                with(density) {
                    rectCanvas.drawRect(
                        drawRLPadding.toPx(),
                        0F,
                        (drawRLPadding + filledWidth + linesBeforeAdditionalWidth).toPx(),
                        heightPx.toFloat(),
                        fillPaint
                    )
                }
                onDispose {

                }
            }

            Canvas(
                modifier = Modifier
                    .padding(
                        start = drawRLPadding,
                        end = drawRLPadding
                    )
                    .fillMaxSize()
            ) {
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