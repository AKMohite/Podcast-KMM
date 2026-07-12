package com.mak.pocketnotes.core.feature.domain.home.repository

import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import kotlinx.coroutines.flow.Flow

interface BestPodcastRepository {
    fun refresh(param: BestQueryParam): Flow<List<Podcast>>

    fun observePodcasts(param: BestQueryParam): Flow<List<Podcast>>
}