package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.GenreEntity
import com.mak.pocketnotes.core.database.dao.GenresDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.dto.GenreDTO
import com.mak.pocketnotes.domain.models.DomainResult
import com.mak.pocketnotes.domain.models.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.Validator
import kotlin.time.Duration.Companion.hours

class GetGenres: KoinComponent {
    private val dispatcher: DispatcherProvider by inject()
    private val api: PocketNotesAPI by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val genresDAO: GenresDAO by inject()
    private val lastSyncDAO: LastSyncDAO by inject()

    operator fun invoke(): Flow<DomainResult<List<Genre>>> = StoreBuilder
        .from<Unit, List<GenreDTO>, List<Genre>>(
            fetcher = Fetcher.of<Unit, List<GenreDTO>> {
                api.getAllGenres().genres ?: emptyList()
            },
            sourceOfTruth = SourceOfTruth.of<Unit, List<GenreDTO>, List<Genre>>(
                reader = {
                    genresDAO.getGenres()
                        .mapNotNull {
                            it.asGenres()
                        }.flowOn(dispatcher.computation)
                 },
                writer = { _, dto ->
                    withContext(dispatcher.io) {
                        transactionRunner {
                            lastSyncDAO.insertLastSync(SyncRequest.GENRES)
                            genresDAO.insertGenres(dto.asGenreEntities())
                        }
                    }
                },
//                delete = { dao.removeGenres() },
                deleteAll = {
                    withContext(dispatcher.io) {
                        transactionRunner(genresDAO::removeGenres)
                    }
                }
            )
        ).validator(
            Validator.by {
                if (it.isEmpty()) {
                    return@by false
                }
                withContext(dispatcher.io) {
                    lastSyncDAO.isRequestValid(
                        requestType = SyncRequest.GENRES,
                        threshold = 24.hours,
                    )
                }
            }
        )
        .build()
        .stream(StoreReadRequest.cached(Unit, false))
        .map { response ->
            when (response) {
                is StoreReadResponse.Data -> {
                    DomainResult.Success(response.value)
                }
                    is StoreReadResponse.Error -> {
                        DomainResult.Error(response.errorMessageOrNull())
                    }
//                    StoreReadResponse.Initial -> TODO()
                    is StoreReadResponse.Loading -> DomainResult.Loading
//                    is StoreReadResponse.NoNewData -> TODO()
                else -> { DomainResult.Success(emptyList()) }
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
