package com.vinson.base.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import com.vinson.base.ui.theme.BoldBigTitle
import com.vinson.base.ui.theme.Text10
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@Composable
private fun rememberCollapsingToolbarState(
        threshold: Float
): CollapsingToolbarState {
    return remember {
        CollapsingToolbarState(threshold = threshold)
    }
}

@Stable
private class CollapsingToolbarState(
        private val threshold: Float
) {
    var scrollOffset = threshold
        private set
    private val mutatorMutex = MutatorMutex()

    var progress: Float by mutableStateOf(1f)

    suspend fun setOffsetTo(offset: Float) {
        mutatorMutex.mutate {
            if (scrollOffset == offset) return@mutate
            scrollOffset = offset.coerceIn(0f, threshold)
            progress = calculateProgress()
        }
    }

    fun isOverflow() = scrollOffset >= threshold

    private fun calculateProgress() = scrollOffset / threshold
}

private class CollapsingToolbarNestedScrollConnection(
        private val state: CollapsingToolbarState,
        private val coroutineScope: CoroutineScope
) : NestedScrollConnection {

    override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
    ): Offset = when {
        source == NestedScrollSource.Drag &&
                (!state.isOverflow() || (state.isOverflow() && available.y < 0)) -> onScroll(available)
        else -> Offset.Zero
    }

    override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
    ): Offset = when {
        source == NestedScrollSource.Drag &&
                (!state.isOverflow() || (state.isOverflow() && available.y < 0)) -> onScroll(available)
        else -> Offset.Zero
    }

    private fun onScroll(available: Offset): Offset {
        val newOffset = (available.y + state.scrollOffset).coerceAtLeast(0f)
        val dragConsumed = newOffset - state.scrollOffset

        return if (dragConsumed.absoluteValue >= 0.5f) {
            coroutineScope.launch {
                state.setOffsetTo(newOffset)
            }
            // Return the consumed Y
            Offset(x = 0f, y = dragConsumed)
        } else {
            Offset.Zero
        }
    }
}

@Composable
fun CollapsingToolbar(
    title: String,
    modifier: Modifier = Modifier,
    leaderPainter: Painter? = null,
    leaderPainterClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val config = if (leaderPainter != null) configWithIcon else config

    val coroutineScope = rememberCoroutineScope()
    val state = rememberCollapsingToolbarState(threshold = config.getPxScrollOffset())

    // Our nested scroll connection, which updates our state.
    val nestedScrollConnection = remember(state, coroutineScope) {
        CollapsingToolbarNestedScrollConnection(state, coroutineScope)
    }
    val paddingTop by animateDpAsState(
        targetValue = max(config.paddingTop.first, config.paddingTop.second * state.progress)
    )
    val paddingStart by animateDpAsState(
        targetValue = max(
            config.paddingStart.first,
            config.paddingStart.first + (config.paddingStart.second - config.paddingStart.first) * (1 - state.progress)
        )
    )
    val textStyle by animateFloatAsState(
        targetValue = kotlin.math.max(config.fontSize.first, config.fontSize.second * state.progress)
    )
    Column(modifier.nestedScroll(connection = nestedScrollConnection)) {
        Surface(color = Text10) {
            if (leaderPainter != null) {
                Icon(
                        painter = leaderPainter,
                        contentDescription = null,
                        modifier = Modifier
                                .padding(16.dp)
                                .size(24.dp)
                                .clickable { leaderPainterClick() }
                )
            }
            Text(
                    text = title,
                    style = BoldBigTitle + TextStyle(fontSize = textStyle.sp),
                    modifier = Modifier
                            .padding(paddingStart, paddingTop, 0.dp, 0.dp)
            )
        }
        content()
    }
}

private val config = CollapsingToolbarConfig(
    fontSize = 18f to 26f,
    paddingTop = 16.dp to 48.dp,
    paddingStart = 20.dp to 20.dp
)
private val configWithIcon = CollapsingToolbarConfig(
    fontSize = 18f to 26f,
    paddingTop = 16.dp to 64.dp,
    paddingStart = 16.dp to 72.dp
)

class CollapsingToolbarConfig(
    val fontSize: Pair<Float, Float>,
    val paddingTop: Pair<Dp, Dp>,
    val paddingStart: Pair<Dp, Dp>
) {
    @Composable
    fun getPxScrollOffset() = with(LocalDensity.current) {
        ((fontSize.second - fontSize.first).sp.toDp() + (paddingTop.second - paddingTop.first)).toPx()
    }
}