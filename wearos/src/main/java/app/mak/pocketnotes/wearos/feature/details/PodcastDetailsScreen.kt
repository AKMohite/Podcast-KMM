package app.mak.pocketnotes.wearos.feature.details

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnItemScope
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListHeaderDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import androidx.wear.compose.material3.lazy.TransformationSpec
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import coil.compose.AsyncImage
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PodcastDetailsScreen(
    id: String,
    modifier: Modifier = Modifier,
    columnState: TransformingLazyColumnState = rememberTransformingLazyColumnState(),
) {
    val detailViewModel: PodcastDetailViewModel = koinViewModel(
        parameters = { parametersOf(id) }
    )
    val state by detailViewModel.uiState.collectAsStateWithLifecycle()
    PodcastDetailsContent(
        uiState = state,
        columnState = columnState,
        modifier = modifier
    )
}

@Composable
private fun PodcastDetailsContent(
    uiState: PodcastDetailState,
    modifier: Modifier = Modifier,
    columnState: TransformingLazyColumnState = rememberTransformingLazyColumnState(),
) {
    val transformationSpec = rememberTransformationSpec()
    ScreenScaffold(
        scrollState = columnState,
    ) {
        TransformingLazyColumn(
            state = columnState,
            contentPadding = PaddingValues(
                top = ListHeaderDefaults.minimumTopListContentPadding,
                bottom = ListHeaderDefaults.minimumBottomListContentPadding,
                start = 8.dp,
                end = 8.dp
            ),
            modifier = modifier.fillMaxWidth()
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    AsyncImage(
                        model = uiState.podcast?.thumbnail,
                        contentDescription = uiState.podcast?.title,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }
            }
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    Text(
                        text = uiState.podcast?.title.orEmpty(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    Text(
                        text = uiState.podcast?.publisher.orEmpty(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            items(items = uiState.episodes, key = { episode -> episode.id }) { episode ->
                EpisodeChip(
                    episode = episode,
                    transformationSpec = transformationSpec
                )
            }
        }
    }
}

@Composable
private fun TransformingLazyColumnItemScope.EpisodeChip(
    episode: PodcastEpisode,
    transformationSpec: TransformationSpec,
    modifier: Modifier = Modifier
) {
    TitleCard(
        onClick = { /* TODO */ },
        title = {
            Text(
                text = episode.title,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
            )
        },
        subtitle = {
            Text(
                text = episode.readableDuration(),
                style = MaterialTheme.typography.labelMedium
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .transformedHeight(this, transformationSpec),
        transformation = SurfaceTransformation(transformationSpec),
    )
}
