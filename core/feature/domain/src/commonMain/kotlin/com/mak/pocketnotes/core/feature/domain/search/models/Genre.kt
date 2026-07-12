package com.mak.pocketnotes.core.feature.domain.search.models

data class Genre(
    val id: Int,
    val name: String,
    val parentId: Int
)