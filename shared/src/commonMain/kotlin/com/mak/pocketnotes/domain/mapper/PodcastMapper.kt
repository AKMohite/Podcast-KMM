package com.mak.pocketnotes.domain.mapper

import com.mak.pocketnotes.data.remote.dto.EpisodeDTO
import com.mak.pocketnotes.data.remote.dto.PodcastDTO
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.local.database.dao.PodcastEntity

internal class PodcastMapper {

    fun jsonToModels(dtos: List<PodcastDTO>): List<Podcast> {
        return dtos.map { dto ->
            jsonToModel(dto)
        }
    }

    fun jsonToEntity(dto: PodcastDTO): PodcastEntity {
        return PodcastEntity(
            id = dto.id!!,
            title = dto.title ?: "",
            description = dto.description ?: "",
            image = dto.image,
            thumbnail = dto.thumbnail
        )
    }

    fun jsonToEntities(dtos: List<PodcastDTO>): List<PodcastEntity> = dtos.map { dto ->
        jsonToEntity(dto)
    }

    fun jsonToModel(dto: PodcastDTO) = Podcast(
        id = dto.id!!,
        title = dto.title ?: "",
        description = dto.description ?: "",
        image = dto.image ?: "",
        thumbnail = dto.thumbnail ?: "",
        publisher = dto.publisher ?: "",
        episodes = getPodcastEpisodes(dto.episodes),
//        listenScore = dto.listenScore?.toString()?.toIntOrNull() ?: 0,
//        totalEpisodes = dto.totalEpisodes ?: 0,
//        type = dto.type ?: "",
//        website = dto.website ?: ""
    )

    fun getPodcastEpisodes(episodes: List<EpisodeDTO>?): List<PodcastEpisode> {
        return episodes?.map {
            getPodcastEpisode(it)
        } ?: emptyList()
    }

    private fun getPodcastEpisode(dto: EpisodeDTO): PodcastEpisode {
        return with(dto) {
            PodcastEpisode(
                id = id ?: "",
                title = title ?: titleOriginal ?: "",
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

    fun entityToModels(entities: List<PodcastEntity>): List<Podcast> = entities.map { entity ->
        entityToModel(entity)
    }

    private fun entityToModel(entity: PodcastEntity): Podcast {
        return Podcast(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            image = entity.image ?: "",
            thumbnail = entity.thumbnail ?: "",
        )
    }

}
