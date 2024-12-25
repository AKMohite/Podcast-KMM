package com.mak.pocketnotes.android.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.PlayableEpisode
import com.mak.pocketnotes.utils.sample.sampleEpisodes

@Composable
internal fun MiniPlayer(
    isMediaPlaying: Boolean,
    modifier: Modifier = Modifier,
    episode: PlayableEpisode,
    play: () -> Unit,
    next: () -> Unit
) {
    Row(
        //        TODO add image palette colors
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(30.dp)
                .clip(MaterialTheme.shapes.small),
            model = episode.image,
            contentDescription = episode.title,
            contentScale = ContentScale.Crop,
            placeholder = debugPlaceholder()
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = episode.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onTertiary
        )
        IconButton(onClick = play) {
//            TODO playpause icon
            Icon(
                imageVector = if (isMediaPlaying) Icons.Default.Close else Icons.Outlined.PlayArrow,
                contentDescription = stringResource(R.string.player_play_pause),
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
        IconButton(onClick = next) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = stringResource(R.string.next),
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Preview
@Composable
private fun MiniPlayerPreview() {
    PocketNotesTheme {
        Surface {
            MiniPlayer(
                episode = sampleEpisodes[0].asPlayableEpisode(),
                play = {},
                next = {},
                isMediaPlaying = false
            )
        }
    }
}