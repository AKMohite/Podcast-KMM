package com.mak.pocketnotes.core.feature.domain.podcastdetails.repository

import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import kotlinx.coroutines.flow.Flow

typealias EpisodeParams = Pair<String, Long?>

interface EpisodeRepository {
    fun refresh(params: EpisodeParams): Flow<List<PodcastEpisode>>
    fun observeEpisodes(params: EpisodeParams): Flow<List<PodcastEpisode>>
}