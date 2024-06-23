package com.mak.pocketnotes.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal typealias GenreEntity = Genres

internal class GenresDAO(
    database: PocketDatabase,
    private val dispatcher: Dispatcher
): IGenresDAO {

    private val dbQuery = database.genre_entityQueries

    override suspend fun insertGenres(genres: List<GenreEntity>) = withContext(dispatcher.io)  {
        dbQuery.transaction {
            genres.forEach { genre ->
                dbQuery.insert(genre)
            }
        }
    }

    override fun getGenres(): Flow<List<GenreEntity>> {
        return dbQuery.getGenres()
            .asFlow()
            .mapToList(dispatcher.io)
    }

    override suspend fun removeGenres() = withContext(dispatcher.io) {
        dbQuery.deleteAll()
    }
}

internal interface IGenresDAO {
    suspend fun insertGenres(genres: List<GenreEntity>)
    suspend fun removeGenres()
    fun getGenres(): Flow<List<GenreEntity>>
}
