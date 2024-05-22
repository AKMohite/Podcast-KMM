package com.mak.pocketnotes.data.remote

import com.mak.pocketnotes.data.remote.dto.BestPodcastDTO
import com.mak.pocketnotes.data.remote.dto.CuratedPodcastsDTO
import com.mak.pocketnotes.data.remote.dto.PodcastDTO
import com.mak.pocketnotes.data.util.Dispatcher
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

internal class PocketNotesAPI(
    private val client: HttpClient,
    private val dispatcher: Dispatcher
): IPocketNotesAPI {
    override suspend fun getBestPodcasts(page: Int): BestPodcastDTO = withContext(dispatcher.io) {
        client.get("api/v2/best_podcasts?page=$page").body()
    }
    override suspend fun getCuratedPodcasts(page: Int): CuratedPodcastsDTO = withContext(dispatcher.io) {
        client.get("api/v2/curated_podcasts").body()
    }
    override suspend fun getPodcastDetails(id: String): PodcastDTO = withContext(dispatcher.io) {
        client.get("api/v2/podcasts/$id").body()
    }
}

internal interface IPocketNotesAPI {
    suspend fun getBestPodcasts(page: Int): BestPodcastDTO
    suspend fun getCuratedPodcasts(page: Int): CuratedPodcastsDTO
    suspend fun getPodcastDetails(id: String): PodcastDTO
}