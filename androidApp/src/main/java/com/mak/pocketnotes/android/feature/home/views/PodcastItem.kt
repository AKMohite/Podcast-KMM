package com.mak.pocketnotes.android.feature.home.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun PodcastItem(
    modifier: Modifier = Modifier,
    podcast: Podcast,
    gotoDetails: (Podcast) -> Unit
) {
    Card(
        modifier = modifier
            .height(220.dp)
            .clickable { gotoDetails(podcast) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = podcast.thumbnail,
                    contentDescription = podcast.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp)),
                    placeholder = debugPlaceholder()

                )
//                Surface(
//                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
//                    modifier = Modifier
//                        .size(50.dp)
//                        .clip(CircleShape)
//                        .padding(12.dp)
//                ) {
//                    Image(painter = painterResource(id = coil.base.R.drawable.notification_icon_background), contentDescription = )
//                }
            }

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = podcast.publisher,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun PodcastItemPreview() {
    PocketNotesTheme {
        PodcastItem(
            podcast = samplePodcasts[0],
            gotoDetails = {}
        )
    }
}
