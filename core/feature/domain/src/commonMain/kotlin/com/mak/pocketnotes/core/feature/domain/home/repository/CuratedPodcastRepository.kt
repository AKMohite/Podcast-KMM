package com.mak.pocketnotes.core.feature.domain.home.repository

import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcastsParam
import kotlinx.coroutines.flow.Flow

interface CuratedPodcastRepository {
    fun refresh(param: CuratedPodcastsParam): Flow<List<CuratedPodcast>>
    fun observePodcasts(param: CuratedPodcastsParam): Flow<List<CuratedPodcast>>
}