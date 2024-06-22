package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.local.IPodcastDAO
import com.mak.pocketnotes.local.ITrendingPodcastDAO
import com.mak.pocketnotes.local.TrendingPodcastEntity
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
        val podcasts = mapper.podcast.jsonToEntities(bestPodcastAPI.podcasts ?: emptyList())
        val trendingPodcasts = podcasts.map {
            TrendingPodcastEntity(
                id = 0L,
                podcast_id = it.id,
                page = page
            )
        }
        podcastDAO.insertPodcasts(podcasts)
        trendingPodcastDAO.upsertPage(trendingPodcasts)
//        TODO: this is for iOS we can remove it after local db for iOS is implemented
        return mapper.podcast.entityToModels(podcasts)
    }
}