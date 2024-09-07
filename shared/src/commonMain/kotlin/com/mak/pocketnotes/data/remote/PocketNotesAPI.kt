package com.mak.pocketnotes.data.remote

import com.mak.pocketnotes.data.remote.dto.BestPodcastDTO
import com.mak.pocketnotes.data.remote.dto.CuratedPodcastsDTO
import com.mak.pocketnotes.data.remote.dto.GenresDTO
import com.mak.pocketnotes.data.remote.dto.PodcastDTO
import com.mak.pocketnotes.data.remote.dto.PodcastRecommendationsDTO
import com.mak.pocketnotes.data.remote.dto.SearchEpisodesDTO
import com.mak.pocketnotes.data.util.Dispatcher
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

internal class PocketNotesAPI(
    private val client: HttpClient,
    private val dispatcher: Dispatcher
): IPocketNotesAPI {

    override suspend fun getAllGenres(): GenresDTO = withContext(dispatcher.io) {
        client.get("api/v2/genres").body()
    }
    override suspend fun getBestPodcasts(queryMap: Map<String, String>): BestPodcastDTO = withContext(dispatcher.io) {
        val queries= getAllQueries(queryMap)
        client.get("api/v2/best_podcasts?$queries").body()
    }
    override suspend fun getCuratedPodcasts(page: Int): CuratedPodcastsDTO = withContext(dispatcher.io) {
        client.get("api/v2/curated_podcasts").body()
    }
    override suspend fun getPodcastRecommendations(id: String): PodcastRecommendationsDTO = withContext(dispatcher.io) {
        client.get("api/v2/podcasts/$id/recommendations").body()
    }
    override suspend fun getPodcastDetails(id: String, queryMap: Map<String, String>): PodcastDTO = withContext(dispatcher.io) {
        val queries = getAllQueries(queryMap)
//        TODO: remove copy
        client.get("api/v2/podcasts/$id?$queries").body<PodcastDTO>()
            .copy(id = id)
    }

    override suspend fun search(queries: Map<String, String>): SearchEpisodesDTO = withContext(dispatcher.io) {
        val queryMap= getAllQueries(queries)
        client.get("api/v2/search?$queryMap").body()
    }

    private fun getAllQueries(queries: Map<String, String>) = queries.map {
        "${it.key}=${it.value}"
    }.joinToString("&")
}

internal interface IPocketNotesAPI {
    suspend fun getAllGenres(): GenresDTO
    suspend fun getBestPodcasts(queryMap: Map<String, String>): BestPodcastDTO
    suspend fun getCuratedPodcasts(page: Int): CuratedPodcastsDTO
    suspend fun getPodcastRecommendations(id: String): PodcastRecommendationsDTO
    suspend fun getPodcastDetails(id: String, queryMap: Map<String, String> = emptyMap()): PodcastDTO
    suspend fun search(queries: Map<String, String>): SearchEpisodesDTO
}