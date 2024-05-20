package com.mak.pocketnotes.android.feature.home.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun HomeCarouselCard(
    modifier: Modifier = Modifier,
    podcast: Podcast
) {
    Row(
        modifier = modifier
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = podcast.thumbnail,
            contentDescription = podcast.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp)),
            placeholder = debugPlaceholder()

        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = podcast.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = podcast.publisher,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun HomeCarouselCardPreview() {
    PocketNotesTheme {
        Surface {
            HomeCarouselCard(
                modifier = Modifier.fillMaxWidth(),
                podcast = samplePodcasts[0]
            )
        }
    }
}