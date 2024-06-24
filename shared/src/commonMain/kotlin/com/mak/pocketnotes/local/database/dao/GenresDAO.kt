package com.mak.pocketnotes.local.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.local.Genres
import kotlinx.coroutines.flow.Flow

internal typealias GenreEntity = Genres

internal class GenresDAO(
    database: PocketDatabase,
    private val dispatcher: Dispatcher
): IGenresDAO {

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
    }

    override fun removeGenres() {
        dbQuery.deleteAll()
    }
}

internal interface IGenresDAO {
    fun insertGenres(genres: List<GenreEntity>)
    fun removeGenres()
    fun getGenres(): Flow<List<GenreEntity>>
}
