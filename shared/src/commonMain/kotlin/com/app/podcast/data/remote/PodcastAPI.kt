package com.app.podcast.data.remote

import com.app.podcast.data.remote.dto.BestPodcastDTO
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class PodcastAPI {

    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun getBestPodcasts(): BestPodcastDTO {
        return httpClient.get(BEST_PODCAST)
    }

    companion object {
        private const val BASE_URL = "https://listen-api.listennotes.com/api/v2"
        private const val BEST_PODCAST = "$BASE_URL/best_podcasts"
    }
}