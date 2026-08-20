package app.mak.pocketnotes.wearos.feature.trendingpodcasts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingPodcastsScreen(
    modifier: Modifier = Modifier,
    state: TransformingLazyColumnState = rememberTransformingLazyColumnState(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val viewModel = koinViewModel<TrendingPodcastsViewModel>()
    val podcastState by viewModel.state.collectAsStateWithLifecycle()
    val transformationSpec = rememberTransformationSpec()

    TransformingLazyColumn(
        state = state,
        contentPadding = contentPadding,
        modifier = modifier.fillMaxWidth()
    ) {
        items(items = podcastState, key = { podcast -> podcast.id }) { podcast ->
            Button(
                onClick = { /* TODO: Navigate to Podcast details */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .transformedHeight(this, transformationSpec),
                transformation = SurfaceTransformation(transformationSpec)
            ) {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
