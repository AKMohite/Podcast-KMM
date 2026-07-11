package com.mak.pocketnotes.android.feature.player.v2.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.player.v2.PlayerTestTags
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode

@Composable
internal fun NowPlayingInfo(
    episode: PodcastEpisode?,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    Column(modifier = modifier) {
        Text(
            text = episode?.title ?: stringResource(R.string.nothing_playing),
            style = titleStyle,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag(PlayerTestTags.EPISODE_TITLE),
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = episode?.title.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag(PlayerTestTags.PODCAST_NAME),
        )
    }
}