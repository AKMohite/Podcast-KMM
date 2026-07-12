package com.mak.pocketnotes.core.feature.domain.search.repository

import com.mak.pocketnotes.core.feature.domain.search.models.Genre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    fun refresh(): Flow<List<Genre>>
    fun observe(): Flow<List<Genre>>
}