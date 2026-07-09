package com.mak.pocketnotes.core.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenresDTO(
    @SerialName("genres")
    val genres: List<GenreDTO>? = emptyList()
)