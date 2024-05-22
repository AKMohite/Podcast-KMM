package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.models.Podcast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPodcastRecommendations: KoinComponent {

    private val api: IPocketNotesAPI by inject()
    private val mapper: PocketMapper by inject()

    suspend operator fun invoke(id: String): List<Podcast> {
        val dto = api.getPodcastRecommendations(id).recommendations ?: return emptyList()
        val podcasts = mapper.podcast.jsonToModels(dto)
        return podcasts
    }
}