package com.mak.pocketnotes.domain.models

data class Genre(
    val id: Int,
    val name: String,
    val parentId: Int
)
