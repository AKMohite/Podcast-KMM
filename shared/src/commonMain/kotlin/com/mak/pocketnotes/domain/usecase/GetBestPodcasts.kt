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
    suspend operator fun invoke(page:Int, genreId: Int? = null): List<Podcast> {
        val queryMap = mutableMapOf(
            "page" to page.toString()
        )
        genreId?.let { id ->
            queryMap["genre_id"] = id.toString()
        }
        val bestPodcastAPI = api.getBestPodcasts(queryMap)
        val podcasts = mapper.podcast.jsonToModels(bestPodcastAPI.podcasts ?: emptyList())
        return podcasts
    }
}