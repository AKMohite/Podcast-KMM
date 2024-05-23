package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.dto.GenreDTO
import com.mak.pocketnotes.domain.models.Genre
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetGenres: KoinComponent {
    private val api: IPocketNotesAPI by inject()

    suspend operator fun invoke(): List<Genre> {
        val genreDTOS = api.getAllGenres().genres ?: return emptyList()
        return genreDTOS.asGenres()
    }
}

private fun List<GenreDTO>.asGenres(): List<Genre> {
    return map { dto ->
        Genre(
            id = dto.id ?: 0,
            name = dto.name ?: "",
            parentId = dto.parentId ?: 0
        )
    }
}
