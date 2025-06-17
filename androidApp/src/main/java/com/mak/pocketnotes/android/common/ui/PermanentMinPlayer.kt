package com.mak.pocketnotes.android.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.ui.res.painterResource
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
internal fun PermanentMinPlayer(
    episode: PlayableEpisode,
    playPause: () -> Unit,
    isMediaPlaying: Boolean,
    previousClick: () -> Unit,
    nextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            modifier = Modifier
                .size(250.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .align(Alignment.CenterHorizontally),
            model = episode.image,
            contentDescription = episode.title,
            contentScale = ContentScale.Crop,
            placeholder = debugPlaceholder()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier
                .padding(4.dp),
            text = episode.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .padding(4.dp),
            text = "Podcast title", // TODO map title
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            IconButton(onClick = previousClick) {
                Icon(
                    painter = painterResource(R.drawable.icon_previous),
                    contentDescription = stringResource(R.string.player_previous)
                )
            }
            FilledIconButton(onClick = playPause) {
                Icon(
                    imageVector = if (isMediaPlaying) Icons.Default.Close else Icons.Outlined.PlayArrow,
                    contentDescription = stringResource(R.string.player_play_pause)
                )
            }
            IconButton(onClick = nextClick) {
                Icon(
                    painter = painterResource(R.drawable.icon_next),
                    contentDescription = stringResource(R.string.player_next)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PermanentMinPlayerPreview() {
    PocketNotesTheme {
        Surface {
            PermanentMinPlayer(
                episode = sampleEpisodes[0].asPlayableEpisode(),
                playPause = {},
                isMediaPlaying = false,
                previousClick = {},
                nextClick = {}
            )
        }
    }
}