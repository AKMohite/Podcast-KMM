package app.mak.pocketnotes.wearos.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class PodcastDetailViewModel(
    val podcastRepository: PodcastRepository,
    private val podcastId: String
) : ViewModel() {

    internal val uiState: StateFlow<PodcastDetailState> = podcastRepository.refresh(podcastId)
        .map { podcast ->
            PodcastDetailState(podcast = podcast, loading = false, episodes = podcast.episodes)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PodcastDetailState(loading = true)
        )
}


internal data class PodcastDetailState(
    val loading: Boolean = false,
    val podcast: Podcast? = null,
    val episodes: List<PodcastEpisode> = emptyList(),
)