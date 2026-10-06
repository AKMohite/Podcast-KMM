package com.mak.pocketnotes.android.util.md2

import androidx.compose.animation.core.animate
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.pow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun rememberPullRefreshState(
  refreshing: Boolean,
  onRefresh: () -> Unit,
  refreshThreshold: Dp = PullRefreshDefaults.RefreshThreshold,
  refreshingOffset: Dp = PullRefreshDefaults.RefreshingOffset
): PullRefreshState {
  require(refreshThreshold > 0.dp) { "The refresh trigger must be greater than zero!" }

  val scope = rememberCoroutineScope()
  val onRefreshState = rememberUpdatedState(onRefresh)
  val thresholdPx: Float
  val refreshingOffsetPx: Float

  with(LocalDensity.current) {
    thresholdPx = refreshThreshold.toPx()
    refreshingOffsetPx = refreshingOffset.toPx()
  }

  val state = remember(scope) {
    PullRefreshState(scope, onRefreshState, refreshingOffsetPx, thresholdPx)
  }

  SideEffect {
    state.setRefreshing(refreshing)
    state.setThreshold(thresholdPx)
    state.setRefreshingOffset(refreshingOffsetPx)
  }

  return state
}

class PullRefreshState internal constructor(
  private val animationScope: CoroutineScope,
  private val onRefreshState: State<() -> Unit>,
  refreshingOffset: Float,
  threshold: Float
) {
  val progress get() = adjustedDistancePulled / threshold

  internal val refreshing get() = refreshingState
  internal val position get() = positionState
  internal val threshold get() = thresholdState

  private val adjustedDistancePulled by derivedStateOf { distancePulled * DRAG_MULTIPLIER }

  private var refreshingState by mutableStateOf(false)
  private var positionState by mutableStateOf(0f)
  private var distancePulled by mutableStateOf(0f)
  private var thresholdState by mutableStateOf(threshold)
  private var refreshingOffsetState by mutableStateOf(refreshingOffset)

  internal fun onPull(pullDelta: Float): Float {
    if (refreshingState) return 0f

    val newOffset = (distancePulled + pullDelta).coerceAtLeast(0f)
    val dragConsumed = newOffset - distancePulled
    distancePulled = newOffset
    positionState = calculateIndicatorPosition()
    return dragConsumed
  }

  internal fun onRelease(velocity: Float): Float {
    if (refreshing) return 0f

    if (adjustedDistancePulled > threshold) {
      onRefreshState.value()
    }
    animateIndicatorTo(0f)
    val consumed = when {
      distancePulled == 0f -> 0f
      velocity < 0f -> 0f
      else -> velocity
    }
    distancePulled = 0f
    return consumed
  }

  internal fun setRefreshing(refreshing: Boolean) {
    if (refreshingState != refreshing) {
      refreshingState = refreshing
      distancePulled = 0f
      animateIndicatorTo(if (refreshing) refreshingOffsetState else 0f)
    }
  }

  internal fun setThreshold(threshold: Float) {
    thresholdState = threshold
  }

  internal fun setRefreshingOffset(refreshingOffset: Float) {
    if (refreshingOffsetState != refreshingOffset) {
      refreshingOffsetState = refreshingOffset
      if (refreshing) animateIndicatorTo(refreshingOffset)
    }
  }

  private val mutatorMutex = MutatorMutex()

  private fun animateIndicatorTo(offset: Float) = animationScope.launch {
    mutatorMutex.mutate {
      animate(initialValue = positionState, targetValue = offset) { value, _ ->
        positionState = value
      }
    }
  }

  private fun calculateIndicatorPosition(): Float = when {
    adjustedDistancePulled <= threshold -> adjustedDistancePulled

    else -> {
      val overshootPercent = abs(progress) - 1.0f
      val linearTension = overshootPercent.coerceIn(0f, 2f)
      val tensionPercent = linearTension - linearTension.pow(2) / 4
      val extraOffset = threshold * tensionPercent
      threshold + extraOffset
    }
  }
}

object PullRefreshDefaults {
  val RefreshThreshold = 80.dp
  val RefreshingOffset = 56.dp
}

private const val DRAG_MULTIPLIER = 0.5f
