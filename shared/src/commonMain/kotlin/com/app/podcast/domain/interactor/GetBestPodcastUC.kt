package com.app.podcast.domain.interactor

import com.app.podcast.data.local.PodcastDB
import com.app.podcast.data.remote.PodcastAPI
import com.app.podcast.domain.model.BestPodcast

class GetBestPodcastUC(
    private val api: PodcastAPI,
    private val db: PodcastDB
) {

    @Throws(Exception::class)
    suspend operator fun invoke(forceReload: Boolean): List<BestPodcast> {
        val cachedPodcasts = db.getBestPodcasts()
        return if (cachedPodcasts.isNotEmpty() && !forceReload) {
            cachedPodcasts
        } else {
            api.getBestPodcasts().also { dto ->
                db.clearDatabase()
                db.insertBestPodcast(dto.podcasts)
            }
            db.getBestPodcasts()
        }
    }

}