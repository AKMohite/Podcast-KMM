package com.mak.pocketnotes.core.testing.fakes

import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.testing.util.TestPodcastData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeBestPodcastRepository : BestPodcastRepository {
  var sectionState: SectionState<List<Podcast>> = SectionState.Success(
    TestPodcastData.samplePodcastList
  )
  var podcastsToReturn: List<Podcast> = TestPodcastData.samplePodcastList

  val refreshCalls = mutableListOf<BestQueryParam>()

  override fun refreshSection(param: BestQueryParam): Flow<SectionState<List<Podcast>>> {
    refreshCalls.add(param)
    return MutableStateFlow(sectionState).asStateFlow()
  }

  override fun refreshBannerSection(param: BestQueryParam): Flow<SectionState<List<Podcast>>> {
    refreshCalls.add(param)
    return MutableStateFlow(sectionState).asStateFlow()
  }

  override fun refresh(param: BestQueryParam): Flow<List<Podcast>> = flowOf(podcastsToReturn)

  override fun observePodcasts(param: BestQueryParam): Flow<List<Podcast>> =
    flowOf(podcastsToReturn)
}
