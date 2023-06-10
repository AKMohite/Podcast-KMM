package com.mak.pocketnotes.domain.mapper

import com.mak.pocketnotes.data.remote.dto.PodcastDTO
import com.mak.pocketnotes.domain.models.Podcast

internal class PodcastMapper {

    fun jsonToModel(dtos: List<PodcastDTO>): List<Podcast> {
        return dtos.map { dto ->
            Podcast(
                id = dto.id,
                description = dto.description,
                image = dto.image,
                listenScore = dto.listenScore,
                publisher = dto.publisher,
                thumbnail = dto.thumbnail,
                title = dto.title,
                totalEpisodes = dto.totalEpisodes,
                type = dto.type,
                website = dto.website
            )
        }
    }

}
