package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.SectionPodcast
import com.mak.pocketnotes.local.CuratedSectionWithPodcast
import com.mak.pocketnotes.local.ICuratedPodcastDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCuratedPodcasts: KoinComponent {

    private val dao: ICuratedPodcastDAO by inject()

    operator fun invoke(): Flow<List<CuratedPodcast>> {
        return dao.getCuratedPodcasts()
            .map { sectionedPodcast ->
                sectionedPodcast.groupBy { it.id }
                    .map { (sectionId, podcasts) ->
                        val section = podcasts.first { it.id == sectionId }
                        CuratedPodcast(
                            id = sectionId,
                            title = section.sectionTitle,
                            podcasts = mapPodcasts(podcasts)
                        )
                    }
        }
    }

    private fun mapPodcasts(podcasts: List<CuratedSectionWithPodcast>): List<SectionPodcast> {
        return podcasts.map { podcast ->
            SectionPodcast(
                id = podcast.podcastId,
                image = podcast.thumbnail ?: "",
                title = podcast.podcastTitle
            )
        }
    }
}