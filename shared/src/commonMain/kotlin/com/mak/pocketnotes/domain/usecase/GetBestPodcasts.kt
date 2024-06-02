package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.domain.mapper.PodcastMapper
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.local.IPodcastDAO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetBestPodcasts: KoinComponent {
    private val dao: IPodcastDAO by inject()
    private val mapper: PodcastMapper by inject()

    suspend operator fun invoke(): List<Podcast> {
        val entities = dao.getBestPodcasts()
        return mapper.entityToModels(entities)
    }
}