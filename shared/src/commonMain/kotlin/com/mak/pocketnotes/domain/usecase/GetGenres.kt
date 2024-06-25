package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.dto.GenreDTO
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.models.DomainResult
import com.mak.pocketnotes.domain.models.Genre
import com.mak.pocketnotes.domain.models.SyncRequest
import com.mak.pocketnotes.local.database.DatabaseTransactionRunner
import com.mak.pocketnotes.local.database.dao.GenreEntity
import com.mak.pocketnotes.local.database.dao.IGenresDAO
import com.mak.pocketnotes.local.database.dao.ILastSyncDAO
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
import kotlin.time.Duration.Companion.minutes

class GetGenres: KoinComponent {
    private val dispatcher: Dispatcher by inject()
    private val api: IPocketNotesAPI by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val genresDAO: IGenresDAO by inject()
    private val lastSyncDAO: ILastSyncDAO by inject()

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
                    withContext(dispatcher.io) {
                        lastSyncDAO.isRequestValid(
                            requestType = SyncRequest.GENRES,
                            threshold = if (it.isNotEmpty()) 24.hours else 30.minutes,
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
