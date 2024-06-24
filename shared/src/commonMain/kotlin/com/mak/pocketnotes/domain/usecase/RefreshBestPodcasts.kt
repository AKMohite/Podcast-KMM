package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.local.database.DatabaseTransactionRunner
import com.mak.pocketnotes.local.database.dao.IPodcastDAO
import com.mak.pocketnotes.local.database.dao.ITrendingPodcastDAO
import com.mak.pocketnotes.local.database.dao.PodcastEntity
import com.mak.pocketnotes.local.database.dao.TrendingPodcastEntity
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RefreshBestPodcasts: KoinComponent {

    private val api: IPocketNotesAPI by inject()
    private val dispatcher: Dispatcher by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
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
        updateLocal(podcasts, page, trendingPodcasts)
//        TODO: this is for iOS we can remove it after local db for iOS is implemented
        return mapper.podcast.entityToModels(podcasts)
    }

    private suspend fun updateLocal(
        podcasts: List<PodcastEntity>,
        page: Int,
        trendingPodcasts: List<TrendingPodcastEntity>
    ) = withContext(dispatcher.io) {
        transactionRunner {
            podcastDAO.insertPodcasts(podcasts)
            trendingPodcastDAO.deletePage(page)
            trendingPodcastDAO.upsertPage(trendingPodcasts)
        }
    }
}