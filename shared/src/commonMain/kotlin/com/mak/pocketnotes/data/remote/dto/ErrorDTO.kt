package com.mak.pocketnotes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorDTO(
    val code: Int? = null,
    val message: String? = null
)