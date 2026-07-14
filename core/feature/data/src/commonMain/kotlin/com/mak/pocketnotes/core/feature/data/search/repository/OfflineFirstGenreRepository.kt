package com.mak.pocketnotes.core.feature.data.search.repository

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.GenreEntity
import com.mak.pocketnotes.core.database.dao.GenresDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.feature.domain.search.models.Genre
import com.mak.pocketnotes.core.feature.domain.search.repository.GenreRepository
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.dto.GenreDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.Validator
import kotlin.time.Duration.Companion.hours

class OfflineFirstGenreRepository(
    private val api: PocketNotesAPI,
    private val transactionRunner: DatabaseTransactionRunner,
    private val genresDAO: GenresDAO,
    private val lastSyncDAO: LastSyncDAO,
    private val dispatcher: DispatcherProvider
) : GenreRepository {

    private val store by lazy {
        StoreBuilder
            .from<Unit, List<GenreDTO>, List<Genre>>(
                fetcher = Fetcher.of<Unit, List<GenreDTO>> {
                    fetch()
                },
                sourceOfTruth = SourceOfTruth.of<Unit, List<GenreDTO>, List<Genre>>(
                    reader = {
                        observe()
                    },
                    writer = { _, dto ->
                        update(dto)
                    },
//                delete = { dao.removeGenres() },
                    deleteAll = {
                        deleteAll()
                    }
                )
            ).validator(
                Validator.by { genres ->
                    return@by needsRefresh(genres)
                }
            )
            .build()
    }

    override fun refresh(): Flow<List<Genre>> = store
        .stream(StoreReadRequest.cached(Unit, false))
        .filterIsInstance<StoreReadResponse.Data<List<Genre>>>()
        .map { storeResponse ->
            storeResponse.requireData()
        }.flowOn(dispatcher.io)
//        .map { response ->
//            when (response) {
//                is StoreReadResponse.Data -> {
//                    DomainResult.Success(response.value)
//                }
//                is StoreReadResponse.Error -> {
//                    DomainResult.Error(response.errorMessageOrNull())
//                }
////                    StoreReadResponse.Initial -> TODO()
//                is StoreReadResponse.Loading -> DomainResult.Loading
////                    is StoreReadResponse.NoNewData -> TODO()
//                else -> { DomainResult.Success(emptyList()) }
//            }
//        }

    override fun observe(): Flow<List<Genre>> = genresDAO.getGenres()
        .map {
            it.asGenres()
        }.flowOn(dispatcher.computation)

    private suspend fun needsRefresh(genres: List<Genre>): Boolean {
        if (genres.isEmpty()) {
            return false
        }
        return withContext(dispatcher.io) {
            lastSyncDAO.isRequestValid(
                requestType = SyncRequest.GENRES,
                threshold = 24.hours,
            )
        }
    }

    private suspend fun deleteAll() = withContext(dispatcher.io) {
        transactionRunner(genresDAO::removeGenres)
    }

    private suspend fun update(dto: List<GenreDTO>) = withContext(dispatcher.io) {
        transactionRunner {
            lastSyncDAO.insertLastSync(SyncRequest.GENRES)
            genresDAO.insertGenres(dto.asGenreEntities())
        }
    }

    private suspend fun fetch(): List<GenreDTO> = api.getAllGenres().getOrThrow().genres.orEmpty()

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


}