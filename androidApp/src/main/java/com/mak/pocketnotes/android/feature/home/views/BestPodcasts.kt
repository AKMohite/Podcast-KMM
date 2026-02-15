package com.mak.pocketnotes.android.feature.home.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun BestPodcasts(
    modifier: Modifier = Modifier,
    podcasts: List<Podcast>,
    gotoDetails: (String) -> Unit
) {
    // TODO need to handle this with width class
    val isTablet = booleanResource(R.bool.is_tablet)
    val columnFraction = if (isTablet) 0.35f else 0.8f
    val rowCount = if (isTablet) 4 else 2
    val maxHeight = if (isTablet) 300.dp else 150.dp
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = stringResource(R.string.trending),
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyHorizontalGrid(
            rows = GridCells.Fixed(rowCount),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxHeight),
        ) {
            items(items = podcasts, key = Podcast::id) { podcast ->
                PodcastRow(
                    modifier = Modifier
                        .fillMaxWidth(columnFraction)
                        .clickable { gotoDetails(podcast.id) }
                        .padding(horizontal = 4.dp),
                    podcast = podcast
                )
            }
        }
    }
}

@Preview
@Composable
private fun BestPodcastsPreview() {
    PocketNotesTheme {
        Surface {
            BestPodcasts(
                podcasts = samplePodcasts.take(8),
                gotoDetails = {}
            )
        }
    }
}