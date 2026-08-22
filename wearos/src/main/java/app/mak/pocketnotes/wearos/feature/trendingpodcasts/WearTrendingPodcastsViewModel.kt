package app.mak.pocketnotes.wearos.feature.trendingpodcasts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class WearTrendingPodcastsViewModel(
  private val podcastRepository: BestPodcastRepository
) : ViewModel() {
  val state =
    podcastRepository
      .refresh(BestQueryParam())
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
      )
}
