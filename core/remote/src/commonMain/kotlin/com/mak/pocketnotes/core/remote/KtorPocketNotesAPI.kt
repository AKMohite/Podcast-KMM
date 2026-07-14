package com.mak.pocketnotes.core.remote

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.remote.dto.BestPodcastDTO
import com.mak.pocketnotes.core.remote.dto.CuratedPodcastsDTO
import com.mak.pocketnotes.core.remote.dto.GenresDTO
import com.mak.pocketnotes.core.remote.dto.PodcastDTO
import com.mak.pocketnotes.core.remote.dto.PodcastRecommendationsDTO
import com.mak.pocketnotes.core.remote.dto.SearchEpisodesDTO
import com.mak.pocketnotes.core.remote.utils.RemoteResult
import com.mak.pocketnotes.core.remote.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

internal class KtorPocketNotesAPI(
    private val client: HttpClient,
    private val dispatcher: DispatcherProvider
) : PocketNotesAPI {

    override suspend fun getAllGenres(): RemoteResult<GenresDTO> = withContext(dispatcher.io) {
        safeApiCall { client.get("api/v2/genres") }
    }

    override suspend fun getBestPodcasts(queryMap: Map<String, String>): RemoteResult<BestPodcastDTO> =
        withContext(dispatcher.io) {
            safeApiCall {
                val queries = getAllQueries(queryMap)
                client.get("api/v2/best_podcasts?$queries")
            }
        }

    override suspend fun getCuratedPodcasts(page: Int): RemoteResult<CuratedPodcastsDTO> =
        withContext(dispatcher.io) {
            safeApiCall { client.get("api/v2/curated_podcasts") }
        }

    override suspend fun getPodcastRecommendations(id: String): RemoteResult<PodcastRecommendationsDTO> =
        withContext(dispatcher.io) {
            safeApiCall { client.get("api/v2/podcasts/$id/recommendations") }
        }

    override suspend fun getPodcastDetails(
        id: String,
        queryMap: Map<String, String>
    ): RemoteResult<PodcastDTO> =
        withContext(dispatcher.io) {
            safeApiCall {
                val queries = getAllQueries(queryMap)
                client.get("api/v2/podcasts/$id?$queries")
            }
        }

    override suspend fun search(queries: Map<String, String>): RemoteResult<SearchEpisodesDTO> =
        withContext(dispatcher.io) {
            safeApiCall {
                val queryMap = getAllQueries(queries)
                client.get("api/v2/search?$queryMap")
            }
        }

    private fun getAllQueries(queries: Map<String, String>) = queries.map {
        "${it.key}=${it.value}"
    }.joinToString("&")
}

