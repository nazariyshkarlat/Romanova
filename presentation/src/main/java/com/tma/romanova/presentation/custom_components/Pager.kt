package com.tma.romanova.presentation.custom_components

import android.os.Looper
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun <T : Any> Pager(
    items: List<T>,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    initialIndex: Int = 0,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    itemFraction: Float = 1f,
    itemSpacing: Dp = 0.dp,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    hasInfinitePages: Boolean = false,
    overshootFraction: Float = .5f,
    onItemSelect: (T) -> Unit = {},
    newSelectedIndex: Pair<Int, DirectionPriority>? = remember { null },
    contentFactory: @Composable (T) -> Unit,
    scrollBehavior: ScrollBehavior = ScrollBehavior.HALF_SIZE_SCROLLED
) {
    require(initialIndex in 0..items.lastIndex) { "Initial index out of bounds" }
    require(itemFraction > 0f && itemFraction <= 1f) { "Item fraction must be in the (0f, 1f] range" }
    require(overshootFraction > 0f && itemFraction <= 1f) { "Overshoot fraction must be in the (0f, 1f] range" }
    val scope = rememberCoroutineScope()
    val state = rememberPagerState()
    state.initialIndex = initialIndex
    state.scrollBehavior = scrollBehavior
    state.numberOfItems = items.size
    state.hasInfinitePages = hasInfinitePages
    state.itemFraction = itemFraction
    state.overshootFraction = overshootFraction
    state.itemSpacing = with(LocalDensity.current) { itemSpacing.toPx() }
    state.orientation = orientation
    state.listener = { index -> onItemSelect(items[index]) }
    state.scope = scope

    Layout(
        content = {
            (if(hasInfinitePages) buildList {
                add(items.last())
                addAll(items)
                if(items.size == 1) addAll(items)
                add(items.first())
            } else items).map { item ->
                Box(
                    modifier = when (orientation) {
                        Orientation.Horizontal -> Modifier.fillMaxWidth()
                        Orientation.Vertical -> Modifier.fillMaxHeight()
                    },
                    contentAlignment = Alignment.Center,
                ) {
                    contentFactory(item)
                }
            }
        },
        modifier = modifier
            .clipToBounds()
            .then(state.inputModifier),
    ) { measurables, constraints ->
        val dimension = constraints.dimension(orientation)
        val looseConstraints = constraints.toLooseConstraints(orientation, state.itemFraction)
        val placeables = measurables.map { measurable -> measurable.measure(looseConstraints) }
        val size = placeables.getSize(orientation, dimension)
        val itemDimension = (dimension * state.itemFraction).roundToInt()
        state.itemDimension = itemDimension
        val halfItemDimension = itemDimension / 2
        layout(size.width, size.height) {
            val centerOffset = dimension / 2 - halfItemDimension
            val dragOffset = state.dragOffset.value
            val roundedDragOffset = dragOffset.roundToInt()
            val spacing = state.itemSpacing.roundToInt()
            val itemDimensionWithSpace = itemDimension + state.itemSpacing
            val first = ceil(
                (dragOffset -itemDimension - centerOffset) / itemDimensionWithSpace
            ).toInt().let {
                if(!hasInfinitePages) it.coerceAtLeast(0) else it
            }
            val last = ((dimension + dragOffset - centerOffset) / itemDimensionWithSpace).toInt().let {
                if(!hasInfinitePages) it.coerceAtMost(state.numberOfItems-1) else it
            }

            fun Int.inv(numOfItems: Int): Int{
                val i = this%(numOfItems*2)
                return when{
                    i == 0 -> 0
                    i < 0 -> i
                    i > 0 -> i-(numOfItems*2)
                    else -> i
                }
            }

            for (i in first..last) {
                val offset = i * (itemDimension + spacing) - roundedDragOffset + centerOffset
                val placeableIdx = if(hasInfinitePages) {
                    val numOfItems = if(items.size == 1) 2 else state.numberOfItems
                    val i = if(i<=0) i else i.inv(numOfItems)
                    when{
                        i % (numOfItems*2) == 0 -> 0
                        (i % (numOfItems*2)).absoluteValue == (numOfItems-2+1) -> numOfItems+2-1
                        i % (numOfItems*2) == numOfItems -> numOfItems
                        i % (numOfItems*2) == 1 -> 1
                        else -> if(i > 0) i%numOfItems else (i%numOfItems)+numOfItems
                    }
                } else i
                placeables[placeableIdx].place(
                    x = when (orientation) {
                        Orientation.Horizontal -> offset
                        Orientation.Vertical -> 0
                    },
                    y = when (orientation) {
                        Orientation.Horizontal -> 0
                        Orientation.Vertical -> offset
                    }
                )
            }
        }
    }

    var isInitialSnap = remember{ true }
    LaunchedEffect(key1 = items, key2 = initialIndex) {
        state.snapTo(initialIndex, isInitialSnap = isInitialSnap)
        isInitialSnap = false
    }

    LaunchedEffect(key1 = newSelectedIndex) {
        newSelectedIndex?.let {
            state.animateTo(index = newSelectedIndex.first, directionPriority = newSelectedIndex.second)
        }
    }


}

@Composable
private fun rememberPagerState(): PagerState = remember { PagerState() }

private fun Constraints.dimension(orientation: Orientation) = when (orientation) {
    Orientation.Horizontal -> maxWidth
    Orientation.Vertical -> maxHeight
}

private fun Constraints.toLooseConstraints(
    orientation: Orientation,
    itemFraction: Float,
): Constraints {
    val dimension = dimension(orientation)
    return when (orientation) {
        Orientation.Horizontal -> copy(
            minWidth = (dimension * itemFraction).roundToInt(),
            maxWidth = (dimension * itemFraction).roundToInt(),
            minHeight = 0,
        )
        Orientation.Vertical -> copy(
            minWidth = 0,
            minHeight = (dimension * itemFraction).roundToInt(),
            maxHeight = (dimension * itemFraction).roundToInt(),
        )
    }
}

private fun List<Placeable>.getSize(
    orientation: Orientation,
    dimension: Int,
): IntSize {
    return when (orientation) {
        Orientation.Horizontal -> IntSize(
            dimension,
            maxByOrNull { it.height }?.height ?: 0
        )
        Orientation.Vertical -> IntSize(
            maxByOrNull { it.width }?.width ?: 0,
            dimension
        )
    }
}

private class PagerState {
    var itemDimension by mutableStateOf(0)
    var hasInfinitePages by mutableStateOf(false)
    var initialIndex by mutableStateOf(0)
    var numberOfItems by mutableStateOf(0)
    var currentIndex by mutableStateOf(0)
    var itemFraction by mutableStateOf(0f)
    var overshootFraction by mutableStateOf(0f)
    var itemSpacing by mutableStateOf(0f)
    var scrollBehavior by mutableStateOf(ScrollBehavior.HALF_SIZE_SCROLLED)
    var orientation by mutableStateOf(Orientation.Horizontal)
    var scope: CoroutineScope? by mutableStateOf(null)
    var listener: (Int) -> Unit by mutableStateOf({})
    val dragOffset = Animatable(0f)

    private val animationSpec = SpringSpec<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium/2,
    )

    suspend fun snapTo(index: Int, isInitialSnap: Boolean = false) {
        val snapOffset = if(hasInfinitePages) dragOffset.value+calcNearestItemDist(index)*(itemSpacing+itemDimension)
        else dragOffset.value+(currentIndex-index)*(itemSpacing+itemDimension)

        if(isInitialSnap) {
            currentIndex = index
        }

        val snapTo = if(isInitialSnap && hasInfinitePages) (itemSpacing+itemDimension)*(initialIndex+1) else snapOffset

        dragOffset.snapTo(
            snapTo
        )

        updateIndex(snapTo)
    }

    fun calcNearestItemDist(index: Int, directionPriority: DirectionPriority = DirectionPriority.Right): Int = run {

        val rightDistance = currentIndex - index
        val rightDistanceAbs = rightDistance.absoluteValue
        val leftDistance = numberOfItems - rightDistanceAbs
        val min = min(rightDistanceAbs, leftDistance)
        when {
            rightDistanceAbs == leftDistance -> {
                rightDistanceAbs * if(directionPriority == DirectionPriority.Right) 1 else -1
            }
            min == rightDistanceAbs -> rightDistanceAbs * if(rightDistance<0) 1 else -1
            else -> leftDistance * if(rightDistance<0) -1 else 1
        }
    }

    suspend fun animateTo(index: Int, directionPriority: DirectionPriority){
        val targetOffset = (
                    if(hasInfinitePages) dragOffset.value+calcNearestItemDist(index, directionPriority)*(itemSpacing+itemDimension)
                    else index * (itemDimension + itemSpacing) * (index-currentIndex).sign
                ).apply {
            if(!hasInfinitePages)
                coerceIn(0f, (numberOfItems - 1).toFloat() * (itemDimension + itemSpacing))
        }

        dragOffset.animateTo(
            animationSpec = animationSpec,
            targetValue = targetOffset
        )

        updateIndex(targetOffset)
    }

    fun itemIndex(offset: Int): Int = run {
        val itemSize = (itemDimension + itemSpacing)
        val offset = if(offset >= 0) offset%(itemSize*numberOfItems)
        else offset%(itemSize*numberOfItems)+numberOfItems*(itemSize*numberOfItems)

        val positiveOffset: Float = if(offset>=itemSize) offset else numberOfItems*(itemDimension + itemSpacing)+offset
        val floatValue = (if(hasInfinitePages) positiveOffset else offset) / (itemDimension + itemSpacing)
        return@run when (scrollBehavior) {
            ScrollBehavior.HALF_SIZE_SCROLLED -> {
                if (hasInfinitePages) (floatValue.roundToInt()-1).absoluteValue % numberOfItems
                else (floatValue).roundToInt()
            }
            ScrollBehavior.FULLY_SCROLLED -> {
                val floatIndexValue: Float =
                    if (hasInfinitePages) (floatValue-1).absoluteValue % numberOfItems
                    else floatValue

                if ((floatIndexValue - currentIndex).absoluteValue % 1F == 0F) floatIndexValue.toInt()
                else currentIndex
            }
        }
    }

    fun updateIndex(offset: Float): Int {
        val index = itemIndex(offset.roundToInt())

        if (index != currentIndex) {
            currentIndex = index
            listener(index)
        }
        return currentIndex
    }

    val inputModifier = Modifier.pointerInput(numberOfItems) {

        fun calculateOffsetLimit(): OffsetLimit {
            val dimension = when (orientation) {
                Orientation.Horizontal -> size.width
                Orientation.Vertical -> size.height
            }
            val itemSideMargin = (dimension - itemDimension) / 2f
            return OffsetLimit(
                min = -dimension * overshootFraction + itemSideMargin,
                max = numberOfItems * (itemDimension + itemSpacing) - (1f - overshootFraction) * dimension + itemSideMargin,
            )
        }

        forEachGesture {
            awaitPointerEventScope {
                val tracker = VelocityTracker()
                val decay = splineBasedDecay<Float>(this)
                val down = awaitFirstDown()
                val offsetLimit = calculateOffsetLimit()
                val dragHandler = { change: PointerInputChange ->
                    scope?.launch {
                        val dragChange = change.calculateDragChange(orientation)
                        dragOffset.snapTo(
                            (dragOffset.value - dragChange).let {
                                if(!hasInfinitePages) {
                                    it.coerceIn(
                                        offsetLimit.min,
                                        offsetLimit.max
                                    )
                                }else it
                            }
                        )
                       updateIndex(dragOffset.value)
                    }
                    tracker.addPosition(change.uptimeMillis, change.position)
                }
                when (orientation) {
                    Orientation.Horizontal -> horizontalDrag(down.id, dragHandler)
                    Orientation.Vertical -> verticalDrag(down.id, dragHandler)
                }
                val velocity = tracker.calculateVelocity(orientation).coerceIn(
                    -(itemDimension+itemSpacing), (itemDimension+itemSpacing)
                )
                scope?.launch{
                    var targetOffset = decay.calculateTargetValue(dragOffset.value, -velocity)
                    val remainder = targetOffset.toInt().absoluteValue % itemDimension
                    val extra = if (remainder > itemDimension / 2f) 1 else 0
                    val lastVisibleIndex =
                        (targetOffset.absoluteValue / itemDimension.toFloat()).toInt() + extra
                    targetOffset = (lastVisibleIndex * (itemDimension + itemSpacing) * targetOffset.sign).apply {
                        if(!hasInfinitePages)
                            coerceIn(0f, (numberOfItems - 1).toFloat() * (itemDimension + itemSpacing))
                    }
                    dragOffset.animateTo(
                        animationSpec = animationSpec,
                        targetValue = targetOffset,
                        initialVelocity = -velocity
                    )
                    updateIndex(targetOffset)
                }
            }
        }
    }

    data class OffsetLimit(
        val min: Float,
        val max: Float,
    )
}

enum class ScrollBehavior{
    HALF_SIZE_SCROLLED, FULLY_SCROLLED
}

private fun VelocityTracker.calculateVelocity(orientation: Orientation) = when (orientation) {
    Orientation.Horizontal -> calculateVelocity().x
    Orientation.Vertical -> calculateVelocity().y
}

private fun PointerInputChange.calculateDragChange(orientation: Orientation) =
    when (orientation) {
        Orientation.Horizontal -> positionChange().x
        Orientation.Vertical -> positionChange().y
    }


enum class DirectionPriority{
    Left, Right
}