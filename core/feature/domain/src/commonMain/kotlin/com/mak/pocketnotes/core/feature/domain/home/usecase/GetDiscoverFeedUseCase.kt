package com.mak.pocketnotes.core.feature.domain.home.usecase

import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcastsParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

data class DiscoverFeed(
  val bannerSection: SectionState<List<Podcast>>,
  val trendingSection: SectionState<List<Podcast>>,
  val curatedSection: SectionState<List<CuratedPodcast>>,
  val isPullToRefreshing: Boolean
)

class GetDiscoverFeedUseCase(
  private val bestPodcastRepository: BestPodcastRepository,
  private val curatedPodcastRepository: CuratedPodcastRepository
) {
  operator fun invoke(forceRefresh: Boolean = false): Flow<DiscoverFeed> {
    val bestParam = BestQueryParam(forceRefresh = forceRefresh)
    val curatedParam = CuratedPodcastsParam(forceRefresh = forceRefresh)

    return combine(
      bestPodcastRepository.refreshBannerSection(bestParam).distinctUntilChanged(),
      bestPodcastRepository.refreshSection(bestParam).distinctUntilChanged(),
      curatedPodcastRepository.refreshSection(curatedParam).distinctUntilChanged()
    ) { banner, trending, curated ->
      DiscoverFeed(
        bannerSection = banner,
        trendingSection = trending,
        curatedSection = curated,
        isPullToRefreshing = banner.isInFlight() || trending.isInFlight() || curated.isInFlight()
      )
    }
  }
}
