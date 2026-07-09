package com.mak.pocketnotes.core.remote

import com.mak.pocketnotes.core.remote.dto.BestPodcastDTO
import com.mak.pocketnotes.core.remote.dto.CuratedPodcastsDTO
import com.mak.pocketnotes.core.remote.dto.GenresDTO
import com.mak.pocketnotes.core.remote.dto.PodcastDTO
import com.mak.pocketnotes.core.remote.dto.PodcastRecommendationsDTO
import com.mak.pocketnotes.core.remote.dto.SearchEpisodesDTO

interface PocketNotesAPI {
    suspend fun getAllGenres(): GenresDTO
    suspend fun getBestPodcasts(queryMap: Map<String, String>): BestPodcastDTO
    suspend fun getCuratedPodcasts(page: Int): CuratedPodcastsDTO
    suspend fun getPodcastRecommendations(id: String): PodcastRecommendationsDTO
    suspend fun getPodcastDetails(
        id: String,
        queryMap: Map<String, String> = emptyMap()
    ): PodcastDTO

    suspend fun search(queries: Map<String, String>): SearchEpisodesDTO
}