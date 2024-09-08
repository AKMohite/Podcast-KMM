package com.mak.pocketnotes.domain.mapper

import com.mak.pocketnotes.data.remote.dto.EpisodeDTO
import com.mak.pocketnotes.data.remote.dto.PodcastDTO
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.local.database.dao.EpisodeEntity
import com.mak.pocketnotes.local.database.dao.PodcastEntity
import com.mak.pocketnotes.local.database.dao.RelatedPodcastEntity
import kotlinx.datetime.Instant

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
            thumbnail = dto.thumbnail,
            publisher = dto.publisher ?: "",
            genres = dto.genreIds?.joinToString(separator = ",") { it.toString() } ?: ""
        )
    }

    fun jsonToEntities(dtos: List<PodcastDTO>): List<PodcastEntity> = dtos.map { dto ->
        jsonToEntity(dto)
    }

    fun jsonToRelatedEntities(podcastId: String, dtos: List<PodcastDTO>): List<RelatedPodcastEntity> = dtos.map { dto ->
        RelatedPodcastEntity(
            id = "${podcastId}_${dto.id}",
            podcast_id = podcastId,
            other_podcast_id = dto.id!!
        )
    }

    fun jsonToModel(dto: PodcastDTO) = Podcast(
        id = dto.id!!,
        title = dto.title ?: "",
        description = dto.description ?: "",
        image = dto.image ?: "",
        thumbnail = dto.thumbnail ?: "",
        publisher = dto.publisher ?: "",
        episodes = getPodcastEpisodes(dto.episodes, dto.id),
//        listenScore = dto.listenScore?.toString()?.toIntOrNull() ?: 0,
//        totalEpisodes = dto.totalEpisodes ?: 0,
//        type = dto.type ?: "",
//        website = dto.website ?: ""
    )

    /**
     * map network episode dto with each episode having respective next episode dates
     * and for last episode to have next paginated date
     */
    fun mapEpisodeEntities(episodes: List<EpisodeDTO>?, podcastId: String, nextEpisodeDate: Long?): List<EpisodeEntity> {
        val podcastEpisodes = (episodes?.map {
            mapEpisodeEntity(it, podcastId)
        } ?: return emptyList()).sortedByDescending { it.published_on }

        return podcastEpisodes.mapIndexed { index, episode ->
            val element = if (index != podcastEpisodes.size - 1) {
                episode.copy(next_episode_published_on = podcastEpisodes.getOrNull(index + 1)?.published_on)
            } else {
                episode.copy(next_episode_published_on = nextEpisodeDate?.let { Instant.fromEpochMilliseconds(it) })
            }
            element
        }

    }

    private fun mapEpisodeEntity(dto: EpisodeDTO, podcastId: String): EpisodeEntity {
        return with(dto) {
            EpisodeEntity(
                id = id!!,
                title = title ?: "",
                description = description ?: "",
                thumbnail = thumbnail ?: "",
                listen_notes_url = listennotesUrl ?: "",
                image = image ?: "",
                audio = audio ?: "",
                audio_length = audioLengthSec?.toLong() ?: 0L,
                podcast_id = podcastId,
                published_on = Instant.fromEpochMilliseconds(dto.pubDateMs ?: Instant.DISTANT_PAST.toEpochMilliseconds()),
                next_episode_published_on = null
            )
        }
    }

    fun getPodcastEpisodes(episodes: List<EpisodeDTO>?, id: String): List<PodcastEpisode> {
        return episodes?.map {
            getPodcastEpisode(it).copy(
                podcastId = id
            )
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
                nextEpisodeAt = null,
                audio = audio ?: "",
                duration = audioLengthSec ?: 0,
                podcastId = ""
            )
        }
    }

    fun entityToModels(entities: List<PodcastEntity>): List<Podcast> = entities.map { entity ->
        entityToModel(entity)
    }

    fun entityToModel(entity: PodcastEntity): Podcast {
        return Podcast(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            image = entity.image ?: "",
            thumbnail = entity.thumbnail ?: "",
            publisher = entity.publisher
        )
    }

    fun episodeEntityToModels(entities: List<EpisodeEntity>): List<PodcastEpisode> {
        return entities.map { entity ->
            with(entity) {
                PodcastEpisode(
                    id = id,
                    title = title,
                    description = description,
                    image = image,
                    listennotesUrl = listen_notes_url,
                    thumbnail = thumbnail,
                    uploadedAt = published_on.toEpochMilliseconds(),
                    nextEpisodeAt = next_episode_published_on?.toEpochMilliseconds(),
                    audio = audio,
                    duration = audio_length.toInt(),
                    podcastId = podcast_id
                )
            }
        }
    }

}
