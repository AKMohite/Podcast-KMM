package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.dto.GenreDTO
import com.mak.pocketnotes.domain.models.Genre
import com.mak.pocketnotes.local.GenreEntity
import com.mak.pocketnotes.local.IGenresDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.Validator

class GetGenres: KoinComponent {
    private val api: IPocketNotesAPI by inject()
    private val dao: IGenresDAO by inject()

    operator fun invoke(): Flow<List<Genre>> = StoreBuilder
        .from<Unit, List<GenreDTO>, List<Genre>>(
            fetcher = Fetcher.of<Unit, List<GenreDTO>> {
                api.getAllGenres().genres ?: emptyList()
            },
            sourceOfTruth = SourceOfTruth.of<Unit, List<GenreDTO>, List<Genre>>(
                reader = {
                    dao.getGenres()
                        .mapNotNull {
                            it.asGenres()
                        }
                 },
                writer = { _, dto ->
                    dao.insertGenres(dto.asGenreEntities())
                },
//                delete = { dao.removeGenres() },
                deleteAll = dao::removeGenres
            )
        ).validator(
            Validator.by {
                it.isNotEmpty()
            }
        )
        .build()
        .stream(StoreReadRequest.cached(Unit, false))
        .map { response ->
            when (response) {
                is StoreReadResponse.Data -> {
                    response.value
                }
//                    is StoreReadResponse.Error -> {
//                        response.errorMessageOrNull()
//                    }
//                    StoreReadResponse.Initial -> TODO()
//                    is StoreReadResponse.Loading -> TODO()
//                    is StoreReadResponse.NoNewData -> TODO()
                else -> { emptyList() }
            }
        }
}

private fun List<GenreDTO>.asGenreEntities(): List<GenreEntity> {
    return map { dto ->
        GenreEntity(
            id = dto.id!!,
            title = dto.name ?: "",
            parent_id = dto.parentId ?: 0
        )
    }
}

private fun List<GenreEntity>.asGenres(): List<Genre> {
    return map { entity ->
        Genre(
            id = entity.id,
            name = entity.title,
            parentId = entity.parent_id ?: 0
        )
    }
}
