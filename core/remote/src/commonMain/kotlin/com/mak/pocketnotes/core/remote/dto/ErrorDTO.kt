package com.mak.pocketnotes.core.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorDTO(
    val code: Int? = null,
    val message: String? = null
)