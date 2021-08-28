package com.app.podcast.data.local

import com.app.podcast.data.remote.dto.PodcastDTO
import com.app.podcast.domain.model.BestPodcast

class PodcastDB(
    databaseDriverFactory: DatabaseDriverFactory
) {
    private val database = PodDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.podDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllBestPodcast()
        }
    }

    internal fun getBestPodcasts(): List<BestPodcast> {
        return dbQuery.getBestPodcasts(::mapBestPodcast).executeAsList()
    }

    private fun mapBestPodcast(
        id: String,
        title: String,
        description: String,
        link: String,
        img_url: String,
        audio_link: String?
    ): BestPodcast {
        return BestPodcast(
            id = id,
            title = title,
            description = description,
            link = link,
            imgURL = img_url,
            audioLink = audio_link ?: "" // todo audio link nullable?
        )
    }

    internal fun insertBestPodcast(podcasts: List<PodcastDTO>) {
        dbQuery.transaction {
            podcasts.forEach { podcast ->
                val pod = dbQuery.getPodcastbyID(podcast.id)
                if (pod == null) {
                    insetPodcast(podcast)
                }

            }
        }
    }

    private fun insetPodcast(podcast: PodcastDTO) {
        dbQuery.insertBestPodcast(
            podcast.id,
            podcast.title,
            podcast.description,
            podcast.website,
            podcast.image,
            podcast.listenNotesUrl
        )
    }

}