package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.models.Podcast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPodcast: KoinComponent {

    private val api: IPocketNotesAPI by inject()
    private val mapper: PocketMapper by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(id: String): Podcast {
        val podcast = api.getPodcastDetails(id)
        val podcasts = mapper.podcast.jsonToModel(podcast)
        return podcasts
    }
}