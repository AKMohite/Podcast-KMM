package com.mak.pocketnotes.data.remote

import com.mak.pocketnotes.data.remote.dto.BestPodcastsDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val API_HOST = "listen-api-test.listennotes.com"
internal class PocketNotesAPI(
    private val client: HttpClient
): IPocketNotesAPI {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = API_HOST
                path("api", "v2")
            }
        }
    }

    override suspend fun getBestPodcasts(): BestPodcastsDTO {
        return client.get("/best_podcasts").body()
    }
}

internal interface IPocketNotesAPI {
    suspend fun getBestPodcasts(): BestPodcastsDTO
}