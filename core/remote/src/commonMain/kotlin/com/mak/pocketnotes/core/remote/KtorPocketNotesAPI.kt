package com.mak.pocketnotes.core.remote

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.remote.dto.BestPodcastDTO
import com.mak.pocketnotes.core.remote.dto.CuratedPodcastsDTO
import com.mak.pocketnotes.core.remote.dto.GenresDTO
import com.mak.pocketnotes.core.remote.dto.PodcastDTO
import com.mak.pocketnotes.core.remote.dto.PodcastRecommendationsDTO
import com.mak.pocketnotes.core.remote.dto.SearchEpisodesDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

internal class KtorPocketNotesAPI(
    private val client: HttpClient,
    private val dispatcher: DispatcherProvider
) : PocketNotesAPI {

    override suspend fun getAllGenres(): GenresDTO = withContext(dispatcher.io) {
        client.get("api/v2/genres").body()
    }

    override suspend fun getBestPodcasts(queryMap: Map<String, String>): BestPodcastDTO =
        withContext(dispatcher.io) {
            val queries = getAllQueries(queryMap)
            client.get("api/v2/best_podcasts?$queries").body()
        }

    override suspend fun getCuratedPodcasts(page: Int): CuratedPodcastsDTO =
        withContext(dispatcher.io) {
            client.get("api/v2/curated_podcasts").body()
        }

    override suspend fun getPodcastRecommendations(id: String): PodcastRecommendationsDTO =
        withContext(dispatcher.io) {
            client.get("api/v2/podcasts/$id/recommendations").body()
        }

    override suspend fun getPodcastDetails(id: String, queryMap: Map<String, String>): PodcastDTO =
        withContext(dispatcher.io) {
            val queries = getAllQueries(queryMap)
//        TODO: remove copy
            client.get("api/v2/podcasts/$id?$queries").body<PodcastDTO>()
                .copy(id = id)
        }

    override suspend fun search(queries: Map<String, String>): SearchEpisodesDTO =
        withContext(dispatcher.io) {
            val queryMap = getAllQueries(queries)
            client.get("api/v2/search?$queryMap").body()
        }

    private fun getAllQueries(queries: Map<String, String>) = queries.map {
        "${it.key}=${it.value}"
    }.joinToString("&")
}

