package com.mak.pocketnotes.android.feature.discover

import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcastsParam
import com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository
import com.mak.pocketnotes.utils.sample.sampleCuratedPodcasts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeCuratedPodcastRepository : CuratedPodcastRepository {
  var sectionState: SectionState<List<CuratedPodcast>> =
    SectionState.Success(sampleCuratedPodcasts)

  val refreshCalls = MutableSharedFlow<CuratedPodcastsParam>(replay = 1)

  override fun refresh(param: CuratedPodcastsParam): Flow<List<CuratedPodcast>> {
    refreshCalls.tryEmit(param)
    return flowOf(sampleCuratedPodcasts)
  }

  override fun refreshSection(
    param: CuratedPodcastsParam
  ): Flow<SectionState<List<CuratedPodcast>>> {
    refreshCalls.tryEmit(param)
    return flowOf(sectionState)
  }

  override fun observePodcasts(param: CuratedPodcastsParam): Flow<List<CuratedPodcast>> =
    flowOf(sampleCuratedPodcasts)
}
