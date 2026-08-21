package app.mak.pocketnotes.wearos.feature.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
}