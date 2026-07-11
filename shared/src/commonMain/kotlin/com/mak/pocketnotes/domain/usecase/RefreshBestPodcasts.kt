package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.PodcastEntity
import com.mak.pocketnotes.core.database.dao.TrendingPodcastDAO
import com.mak.pocketnotes.core.database.dao.TrendingPodcastEntity
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.models.DomainResult
import com.mak.pocketnotes.domain.models.safeCall
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RefreshBestPodcasts: KoinComponent {

    private val api: PocketNotesAPI by inject()
    private val dispatcher: DispatcherProvider by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val podcastDAO: PodcastDAO by inject()
    private val trendingPodcastDAO: TrendingPodcastDAO by inject()
    private val mapper: PocketMapper by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(page:Int, genreId: Int? = null): DomainResult<List<Podcast>> = safeCall {
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
//        WARN: this is for iOS we can remove it after local db for iOS is implemented
        mapper.podcast.entityToModels(podcasts)
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