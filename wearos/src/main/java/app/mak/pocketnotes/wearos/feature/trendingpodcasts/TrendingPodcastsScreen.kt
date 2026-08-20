package app.mak.pocketnotes.wearos.feature.trendingpodcasts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnItemScope
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.TransformationSpec
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import app.mak.pocketnotes.wearos.R
import coil.compose.AsyncImage
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
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
        item {
            Text(
                text = stringResource(id = R.string.home_podcasts),
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.fillMaxWidth(),
            )
        }
        items(items = podcastState, key = { podcast -> podcast.id }) { podcast ->
            PodcastChip(
                podcast = podcast,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .transformedHeight(this, transformationSpec),
                transformationSpec = transformationSpec
            )
        }
    }
}

@Composable
fun TransformingLazyColumnItemScope.PodcastChip(
    podcast: Podcast,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    transformationSpec: TransformationSpec
) {

    Button(
        onClick = { onClick(podcast.id) },
        label = {
            Text(
                text = podcast.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .transformedHeight(this, transformationSpec),
        transformation = SurfaceTransformation(transformationSpec),
        icon = {
            AsyncImage(
                model = podcast.image,
                contentDescription = podcast.title,
                modifier = Modifier
                    .size(ButtonDefaults.IconSize)
                    .clip(MaterialTheme.shapes.extraSmall)
            )
        }
    )
}
