package app.mak.pocketnotes.wearos.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import coil.compose.AsyncImage
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PodcastDetailsScreen(
    id: String,
    modifier: Modifier = Modifier
) {
    val detailViewModel: PodcastDetailViewModel = koinViewModel(
        parameters = { parametersOf(id) }
    )
    val state by detailViewModel.uiState.collectAsStateWithLifecycle()
    PodcastDetailsContent(state)
}

@Composable
private fun PodcastDetailsContent(
    uiState: PodcastDetailState,
    modifier: Modifier = Modifier,
    columnState: TransformingLazyColumnState = rememberTransformingLazyColumnState()
) {
    TransformingLazyColumn(
        state = columnState,
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            AsyncImage(
                model = uiState.podcast?.thumbnail,
                contentDescription = uiState.podcast?.title,
                modifier = Modifier
                    .size(50.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
            )
        }
        item {
            Column {
                Text(
                    text = uiState.podcast?.title.orEmpty(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = uiState.podcast?.publisher.orEmpty(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        items(items = uiState.episodes, key = { episode -> episode.id }) { episode ->
            EpisodeChip(episode)
        }
    }
}

@Composable
private fun EpisodeChip(
    episode: PodcastEpisode,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
    ) {
        Text(
            text = episode.title,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
        )
        Text(
            text = episode.readableDuration(),
            style = MaterialTheme.typography.labelMedium
        )
    }
}
