package com.mak.pocketnotes.android.feature.discover.components.loading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium

@Composable
internal fun DiscoverShimmer(
    sizeClass: WindowSizeClass
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            sizeClass.isExpanded() -> DiscoverShimmerLarge()
            sizeClass.isMedium() -> DiscoverShimmerMedium()
            else -> DiscoverShimmerCompact()
        }
    }
}

@Composable
fun DiscoverShimmerCompact(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerBackground()
        )

        Spacer(
            Modifier.height(12.dp)
        )

        repeat(10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerBackground()
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun DiscoverShimmerMedium(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerBackground()
        )
        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerBackground()
        )
        Spacer(Modifier.height(12.dp))
        repeat(10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerBackground()
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun DiscoverShimmerLarge(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerBackground()
        )
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerBackground()
        )
        Spacer(Modifier.height(12.dp))
        repeat(10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerBackground()
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

/** A gently pulsing background, standing in for a real shimmer shader - fine for a
 * loading placeholder, not meant to be a polished animation. */
internal fun Modifier.shimmerBackground(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(animation = tween(800), repeatMode = RepeatMode.Reverse),
        label = "shimmerAlpha",
    )
    background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
}

@Preview
@PreviewScreenSizes
@Composable
private fun DiscoverShimmerPreview() {
    PocketNotesTheme {
        DiscoverShimmer(sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass)
    }
}