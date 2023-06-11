package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.models.Podcast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetBestPodcasts: KoinComponent {

    private val api: IPocketNotesAPI by inject()
    private val mapper: PocketMapper by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(page:Int): List<Podcast> {
        val bestPodcastAPI = api.getBestPodcasts(page)
        val podcasts = mapper.podcast.jsonToModels(bestPodcastAPI.podcasts ?: emptyList())
        return podcasts
    }
}