package com.mak.pocketnotes.data.remote

import com.mak.pocketnotes.data.remote.dto.BestPodcastsDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

internal class PocketNotesAPI(
    private val client: HttpClient
): IPocketNotesAPI {

    override suspend fun getBestPodcasts(): BestPodcastsDTO {
        return client.get("/best_podcasts").body()
    }
}

internal interface IPocketNotesAPI {
    suspend fun getBestPodcasts(): BestPodcastsDTO
}