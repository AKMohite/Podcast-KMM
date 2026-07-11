package com.mak.pocketnotes.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.queries.Genres
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

typealias GenreEntity = Genres

internal class SQLDelightGenresDAO(
    database: PocketDatabase,
    private val dispatcher: DispatcherProvider
) : GenresDAO {

    private val dbQuery = database.genre_entityQueries

    override fun insertGenres(genres: List<GenreEntity>) {
        genres.forEach { genre ->
            dbQuery.insert(genre)
        }
    }

    override fun getGenres(): Flow<List<GenreEntity>> {
        return dbQuery.getGenres()
            .asFlow()
            .mapToList(dispatcher.io)
            .distinctUntilChanged()
            .flowOn(dispatcher.io)
    }

    override fun removeGenres() {
        dbQuery.deleteAll()
    }
}

interface GenresDAO {
    fun insertGenres(genres: List<GenreEntity>)
    fun removeGenres()
    fun getGenres(): Flow<List<GenreEntity>>
}
