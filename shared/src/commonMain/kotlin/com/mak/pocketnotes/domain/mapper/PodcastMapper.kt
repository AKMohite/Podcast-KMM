package com.mak.pocketnotes.domain.mapper

import com.mak.pocketnotes.data.remote.dto.EpisodeDTO
import com.mak.pocketnotes.data.remote.dto.PodcastDTO
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.models.PodcastEpisode

internal class PodcastMapper {

    fun jsonToModels(dtos: List<PodcastDTO>): List<Podcast> {
        return dtos.map { dto ->
            jsonToModel(dto)
        }
    }

    fun jsonToModel(dto: PodcastDTO) = Podcast(
        id = dto.id!!,
        description = dto.description ?: "",
        image = dto.image ?: "",
        listenScore = dto.listenScore?.toString()?.toIntOrNull() ?: 0,
        publisher = dto.publisher ?: "",
        thumbnail = dto.thumbnail ?: "",
        title = dto.title ?: "",
        totalEpisodes = dto.totalEpisodes ?: 0,
        type = dto.type ?: "",
        website = dto.website ?: "",
        episodes = getPodcastEpisodes(dto.episodes)
    )

    private fun getPodcastEpisodes(episodes: List<EpisodeDTO>?): List<PodcastEpisode> {
        return episodes?.map {
            getPodcastEpisode(it)
        } ?: emptyList()
    }

    private fun getPodcastEpisode(dto: EpisodeDTO): PodcastEpisode {
        return with(dto) {
            PodcastEpisode(
                id = id ?: "",
                title = title ?: "",
                description = description ?: "",
                image = image ?: "",
                listennotesUrl = listennotesUrl ?: "",
                thumbnail = thumbnail ?: "",
                uploadedAt = pubDateMs ?: 0,
                audio = audio ?: "",
                duration = audioLengthSec ?: 0
            )
        }
    }

}
