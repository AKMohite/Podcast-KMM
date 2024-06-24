package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.mapper.PodcastMapper
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.local.database.dao.ITrendingPodcastDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetBestPodcasts: KoinComponent {
    private val dao: ITrendingPodcastDAO by inject()
    private val mapper: PodcastMapper by inject()
    private val dispatcher: Dispatcher by inject()

    operator fun invoke(): Flow<List<Podcast>> {
        return dao.getBestPodcasts()
            .distinctUntilChanged()
            .flowOn(dispatcher.io)
            .map { entities -> mapper.entityToModels(entities) }
            .flowOn(dispatcher.computation)
    }
}