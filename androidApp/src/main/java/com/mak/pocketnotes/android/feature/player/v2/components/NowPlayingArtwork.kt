package com.mak.pocketnotes.android.feature.player.v2.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.player.v2.PlayerTestTags

@Composable
internal fun NowPlayingArtwork(
    artworkUrl: String?,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 280.dp,
) {
    val scale by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0.92f,
        animationSpec = tween(durationMillis = 300),
        label = "artwork_scale",
    )

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(16.dp))
            .testTag(PlayerTestTags.ARTWORK),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = artworkUrl,
            contentDescription = stringResource(R.string.podcast_artwork),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
    }
}