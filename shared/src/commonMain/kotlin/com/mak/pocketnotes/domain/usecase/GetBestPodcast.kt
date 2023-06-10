package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.PocketNotesAPI
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.models.Podcast

internal class GetBestPodcast(
    private val api: PocketNotesAPI,
    private val mapper: PocketMapper
) {
    suspend operator fun invoke(): List<Podcast> {
        val bestPodcastAPI = api.getBestPodcasts()
        val podcasts = mapper.podcast.jsonToModel(bestPodcastAPI.podcasts)
        return podcasts
    }
}