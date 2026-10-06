package com.mak.pocketnotes.android.util.md2

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

@Composable
internal fun PullRefreshIndicator(
  refreshing: Boolean,
  state: PullRefreshState,
  modifier: Modifier = Modifier,
  backgroundColor: Color = MaterialTheme.colorScheme.surface,
  contentColor: Color = contentColorFor(backgroundColor),
  scale: Boolean = false
) {
  val showElevation by remember(refreshing, state) {
    derivedStateOf { refreshing || state.position > 0.5f }
  }

  val color = backgroundColor

  Box(
    modifier = modifier
      .size(INDICATOR_SIZE)
      .pullRefreshIndicatorTransform(state, scale)
      .shadow(if (showElevation) ELEVATION else 0.dp, SPINNER_SHAPE, clip = true)
      .background(color = color, shape = SPINNER_SHAPE)
  ) {
    Crossfade(
      targetState = refreshing,
      animationSpec = tween(durationMillis = CROSSFADE_DURATION_MS)
    ) { refreshingState ->
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        val spinnerSize = (ARC_RADIUS + STROKE_WIDTH).times(2)

        if (refreshingState) {
          CircularProgressIndicator(
            color = contentColor,
            strokeWidth = STROKE_WIDTH,
            modifier = Modifier.size(spinnerSize)
          )
        } else {
          CircularArrowIndicator(state, contentColor, Modifier.size(spinnerSize))
        }
      }
    }
  }
}

@Composable
private fun CircularArrowIndicator(state: PullRefreshState, color: Color, modifier: Modifier) {
  val path = remember { Path().apply { fillType = PathFillType.EvenOdd } }

  val targetAlpha by remember(state) {
    derivedStateOf {
      if (state.progress >= 1f) MAX_ALPHA else MIN_ALPHA
    }
  }

  val alphaState = animateFloatAsState(targetValue = targetAlpha, animationSpec = ALPHA_TWEEN)

  Canvas(modifier.semantics {}) {
    val values = ArrowValues(state.progress)
    val alpha = alphaState.value

    rotate(degrees = values.rotation) {
      val arcRadius = ARC_RADIUS.toPx() + STROKE_WIDTH.toPx() / 2f
      val arcBounds = Rect(
        size.center.x - arcRadius,
        size.center.y - arcRadius,
        size.center.x + arcRadius,
        size.center.y + arcRadius
      )
      drawArc(
        color = color,
        alpha = alpha,
        startAngle = values.startAngle,
        sweepAngle = values.endAngle - values.startAngle,
        useCenter = false,
        topLeft = arcBounds.topLeft,
        size = arcBounds.size,
        style = Stroke(
          width = STROKE_WIDTH.toPx(),
          cap = StrokeCap.Square
        )
      )
      drawArrow(path, arcBounds, color, alpha, values)
    }
  }
}

@Immutable
private class ArrowValues(
  val rotation: Float,
  val startAngle: Float,
  val endAngle: Float,
  val scale: Float
)

private fun ArrowValues(progress: Float): ArrowValues {
  val adjustedPercent = max(min(1f, progress) - 0.4f, 0f) * 5 / 3
  val overshootPercent = abs(progress) - 1.0f
  val linearTension = overshootPercent.coerceIn(0f, 2f)
  val tensionPercent = linearTension - linearTension.pow(2) / 4

  val endTrim = adjustedPercent * MAX_PROGRESS_ARC
  val rotation = (-0.25f + 0.4f * adjustedPercent + tensionPercent) * 0.5f
  val startAngle = rotation * 360
  val endAngle = (rotation + endTrim) * 360
  val scale = min(1f, adjustedPercent)

  return ArrowValues(rotation, startAngle, endAngle, scale)
}

private fun DrawScope.drawArrow(
  arrow: Path,
  bounds: Rect,
  color: Color,
  alpha: Float,
  values: ArrowValues
) {
  arrow.reset()
  arrow.moveTo(0f, 0f)
  arrow.lineTo(x = ARROW_WIDTH.toPx() * values.scale, y = 0f)

  arrow.lineTo(
    x = ARROW_WIDTH.toPx() * values.scale / 2,
    y = ARROW_HEIGHT.toPx() * values.scale
  )

  val radius = min(bounds.width, bounds.height) / 2f
  val inset = ARROW_WIDTH.toPx() * values.scale / 2f
  arrow.translate(
    Offset(
      x = radius + bounds.center.x - inset,
      y = bounds.center.y + STROKE_WIDTH.toPx() / 2f
    )
  )
  arrow.close()
  rotate(degrees = values.endAngle) {
    drawPath(path = arrow, color = color, alpha = alpha)
  }
}

private const val CROSSFADE_DURATION_MS = 100
private const val MAX_PROGRESS_ARC = 0.8f

private val INDICATOR_SIZE = 40.dp
private val SPINNER_SHAPE = CircleShape
private val ARC_RADIUS = 7.5.dp
private val STROKE_WIDTH = 2.5.dp
private val ARROW_WIDTH = 10.dp
private val ARROW_HEIGHT = 5.dp
private val ELEVATION = 6.dp

private const val MIN_ALPHA = 0.3f
private const val MAX_ALPHA = 1f
private val ALPHA_TWEEN = tween<Float>(300, easing = LinearEasing)
