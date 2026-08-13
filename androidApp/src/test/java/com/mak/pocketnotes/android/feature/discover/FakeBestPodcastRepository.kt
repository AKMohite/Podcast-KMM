package com.mak.pocketnotes.android.feature.discover

import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeBestPodcastRepository : BestPodcastRepository {

    var sectionState: SectionState<List<Podcast>> = SectionState.Success(samplePodcasts)
    var bannerSectionState: SectionState<List<Podcast>> =
        SectionState.Success(samplePodcasts.take(5))

    val refreshCalls = MutableSharedFlow<BestQueryParam>(replay = 1)

    override fun refresh(param: BestQueryParam): Flow<List<Podcast>> {
        refreshCalls.tryEmit(param)
        return flowOf(samplePodcasts)
    }

    override fun refreshSection(param: BestQueryParam): Flow<SectionState<List<Podcast>>> {
        refreshCalls.tryEmit(param)
        return flowOf(sectionState)
    }

    override fun refreshBannerSection(param: BestQueryParam): Flow<SectionState<List<Podcast>>> {
        refreshCalls.tryEmit(param)
        return flowOf(bannerSectionState)
    }

    override fun observePodcasts(param: BestQueryParam): Flow<List<Podcast>> {
        return flowOf(samplePodcasts)
    }
}
