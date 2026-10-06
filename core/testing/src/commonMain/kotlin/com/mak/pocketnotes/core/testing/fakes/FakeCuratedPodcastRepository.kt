package com.mak.pocketnotes.core.testing.fakes

import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcastsParam
import com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository
import com.mak.pocketnotes.core.testing.util.TestPodcastData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeCuratedPodcastRepository : CuratedPodcastRepository {
  var sectionState: SectionState<List<CuratedPodcast>> = SectionState.Success(listOf(TestPodcastData.sampleCuratedPodcast))
  var curatedListToReturn: List<CuratedPodcast> = listOf(TestPodcastData.sampleCuratedPodcast)

  val refreshCalls = mutableListOf<CuratedPodcastsParam>()

  override fun refreshSection(param: CuratedPodcastsParam): Flow<SectionState<List<CuratedPodcast>>> {
    refreshCalls.add(param)
    return MutableStateFlow(sectionState).asStateFlow()
  }

  override fun refresh(param: CuratedPodcastsParam): Flow<List<CuratedPodcast>> {
    return flowOf(curatedListToReturn)
  }

  override fun observePodcasts(param: CuratedPodcastsParam): Flow<List<CuratedPodcast>> {
    return flowOf(curatedListToReturn)
  }
}
