package app.mak.pocketnotes.wearos.feature.trendingpodcasts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Text
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingPodcastsScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<TrendingPodcastsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    ScalingLazyColumn {
        items(items = state, key = { podcast -> podcast.id }) { podcast ->
            Text(text = podcast.title)
        }
    }
}