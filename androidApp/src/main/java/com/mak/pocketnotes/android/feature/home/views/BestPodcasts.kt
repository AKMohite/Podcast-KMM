package com.mak.pocketnotes.android.feature.home.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    podcasts: List<List<Podcast>>,
    gotoDetails: (String) -> Unit
) {
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
        LazyRow {
            items(items = podcasts) { podcasts ->
                Column(
                    modifier = Modifier
                        .fillParentMaxWidth(0.9f)
                        .wrapContentHeight()
                ) {
                    podcasts.forEach { podcast ->
                        PodcastRow(
                            modifier = Modifier
                                .clickable { gotoDetails(podcast.id) }
                                .padding(4.dp),
                            podcast = podcast
                        )
                    }
                }
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
                podcasts = samplePodcasts.chunked(2),
                gotoDetails = {}
            )
        }
    }
}