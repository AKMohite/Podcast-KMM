package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.local.IPodcastDAO
import com.mak.pocketnotes.local.ITrendingPodcastDAO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RefreshBestPodcasts: KoinComponent {

    private val api: IPocketNotesAPI by inject()
    private val podcastDAO: IPodcastDAO by inject()
    private val trendingPodcastDAO: ITrendingPodcastDAO by inject()
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
        val entities = mapper.podcast.jsonToEntities(bestPodcastAPI.podcasts ?: emptyList())
        podcastDAO.insertPodcasts(entities)
        trendingPodcastDAO.upsertPage(page, entities)
//        TODO: this is for iOS we can remove it after local db for iOS is implemented
        return mapper.podcast.entityToModels(entities)
    }
}