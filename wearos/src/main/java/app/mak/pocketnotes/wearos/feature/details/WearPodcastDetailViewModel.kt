package app.mak.pocketnotes.wearos.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.core.feature.domain.home.models.EpisodeQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

internal class PodcastDetailViewModel(
  private val podcastRepository: PodcastRepository,
  private val episodeRepository: EpisodeRepository,
  podcastId: String,
) : ViewModel() {
  internal val uiState: StateFlow<PodcastDetailState> =
    combine(
      podcastRepository.refresh(podcastId),
      episodeRepository.refresh(
        EpisodeQueryParam(
          podcastId = podcastId,
          nextEpisodeDate = null,
        ),
      ),
    ) { podcast, episodes ->
      PodcastDetailState(podcast = podcast, loading = false, episodes = episodes)
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = PodcastDetailState(loading = true),
    )
}

internal data class PodcastDetailState(
  val loading: Boolean = false,
  val podcast: Podcast? = null,
  val episodes: List<PodcastEpisode> = emptyList(),
)
