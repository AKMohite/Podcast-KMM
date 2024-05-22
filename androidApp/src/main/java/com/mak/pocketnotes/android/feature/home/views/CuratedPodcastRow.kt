package com.mak.pocketnotes.android.feature.home.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.SectionPodcast
import com.mak.pocketnotes.utils.sample.sampleCuratedPodcasts

@Composable
internal fun CuratedPodcastRow(
    modifier: Modifier = Modifier,
    podcast: CuratedPodcast,
    goToDetails: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = podcast.title,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(items = podcast.getCuratedPodcasts()) { podcasts ->
                CuratedPodcastItem(
                    modifier = Modifier
                        .fillParentMaxWidth(0.9f)
                        .wrapContentHeight()
                        .padding(4.dp),
                    podcasts = podcasts,
                    goToDetails = goToDetails
                )
            }
        }
    }
}

@Composable
private fun CuratedPodcastItem(
    modifier: Modifier = Modifier,
    podcasts: List<SectionPodcast>,
    goToDetails: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        podcasts.forEach { podcast ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { goToDetails(podcast.id) }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = podcast.image,
                    contentDescription = podcast.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    placeholder = debugPlaceholder()
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun CuratedPodcastRowPreview() {
    PocketNotesTheme {
        Surface {
            CuratedPodcastRow(
                podcast = sampleCuratedPodcasts[0],
                goToDetails = {}
            )
        }
    }
}

@Preview
@Composable
private fun CuratedPodcastItemPreview() {
    PocketNotesTheme {
        Surface {
            CuratedPodcastItem(
                podcasts = sampleCuratedPodcasts[0].podcasts,
                goToDetails = {}
            )
        }
    }
}