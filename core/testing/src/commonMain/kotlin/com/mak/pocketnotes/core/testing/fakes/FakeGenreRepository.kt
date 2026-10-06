package com.mak.pocketnotes.core.testing.fakes

import com.mak.pocketnotes.core.feature.domain.search.models.Genre
import com.mak.pocketnotes.core.feature.domain.search.repository.GenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGenreRepository : GenreRepository {
  var genresToReturn: List<Genre> = listOf(Genre(id = 1, name = "Technology", parentId = 0))

  override fun refresh(): Flow<List<Genre>> {
    return flowOf(genresToReturn)
  }

  override fun observe(): Flow<List<Genre>> {
    return flowOf(genresToReturn)
  }
}
