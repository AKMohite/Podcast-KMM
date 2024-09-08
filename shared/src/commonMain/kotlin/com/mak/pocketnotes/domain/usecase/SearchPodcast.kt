package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.models.SearchResults
import com.mak.pocketnotes.utils.sample.samplePodcasts
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchPodcast: KoinComponent {
    private val api: IPocketNotesAPI by inject()
    private val mapper: PocketMapper by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(query: String): SearchResults {
        val episodeQuery = mapOf(
            "q" to query,
            "type" to "episode"
        )
//        TODO add podcast results too
        val podcastQuery = mapOf(
            "q" to query,
            "type" to "podcast"
        )
        val episodeDTOs = api.search(episodeQuery).results ?: emptyList()
        val episodes = mapper.podcast.getPodcastEpisodes(episodeDTOs, throw IllegalArgumentException("Where can I get podcast ids for episodes?"))
//        val podcasts = api.search(podcastQuery)
        val podcasts = samplePodcasts
        return SearchResults(episodes, podcasts)
    }
}