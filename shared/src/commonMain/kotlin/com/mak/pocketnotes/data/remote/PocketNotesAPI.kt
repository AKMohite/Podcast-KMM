package com.mak.pocketnotes.data.remote

import com.mak.pocketnotes.data.remote.dto.BestPodcastDTO
import com.mak.pocketnotes.data.remote.dto.PodcastDTO
import com.mak.pocketnotes.data.util.Dispatcher
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.withContext

internal class PocketNotesAPI(
    private val client: HttpClient,
    private val dispatcher: Dispatcher
): IPocketNotesAPI {
    override suspend fun getBestPodcasts(page: Int): BestPodcastDTO = withContext(dispatcher.io) {
        client.get("api/v2/best_podcasts?page=$page").body()
    }
    override suspend fun getPodcastDetails(id: String): PodcastDTO = withContext(dispatcher.io) {
        client.get("/podcasts/$id").body()
    }
}

internal interface IPocketNotesAPI {
    suspend fun getBestPodcasts(page: Int): BestPodcastDTO

    suspend fun getPodcastDetails(id: String): PodcastDTO
}