package com.mak.pocketnotes.core.remote

import com.mak.pocketnotes.core.remote.dto.BestPodcastDTO
import com.mak.pocketnotes.core.remote.dto.CuratedPodcastsDTO
import com.mak.pocketnotes.core.remote.dto.GenresDTO
import com.mak.pocketnotes.core.remote.dto.PodcastDTO
import com.mak.pocketnotes.core.remote.dto.PodcastRecommendationsDTO
import com.mak.pocketnotes.core.remote.dto.SearchEpisodesDTO
import com.mak.pocketnotes.core.remote.utils.RemoteResult

interface PocketNotesAPI {
    suspend fun getAllGenres(): RemoteResult<GenresDTO>
    suspend fun getBestPodcasts(queryMap: Map<String, String>): RemoteResult<BestPodcastDTO>
    suspend fun getCuratedPodcasts(page: Int): RemoteResult<CuratedPodcastsDTO>
    suspend fun getPodcastRecommendations(id: String): RemoteResult<PodcastRecommendationsDTO>
    suspend fun getPodcastDetails(
        id: String,
        queryMap: Map<String, String> = emptyMap()
    ): RemoteResult<PodcastDTO>

    suspend fun search(queries: Map<String, String>): RemoteResult<SearchEpisodesDTO>
}