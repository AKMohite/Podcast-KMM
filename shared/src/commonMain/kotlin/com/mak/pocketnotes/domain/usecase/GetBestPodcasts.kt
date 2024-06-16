package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.domain.mapper.PodcastMapper
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.local.IPodcastDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetBestPodcasts: KoinComponent {
    private val dao: IPodcastDAO by inject()
    private val mapper: PodcastMapper by inject()

    operator fun invoke(): Flow<List<Podcast>> {
        return dao.getBestPodcasts()
            .map { entities -> mapper.entityToModels(entities) }
    }
}