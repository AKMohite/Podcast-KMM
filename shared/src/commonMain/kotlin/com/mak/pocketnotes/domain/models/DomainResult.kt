package com.mak.pocketnotes.domain.models

sealed interface DomainResult<out T> {
    data object Loading: DomainResult<Nothing>
    data class Success<T>(val data: T): DomainResult<T>
    data class Error(val message: String? = null): DomainResult<Nothing>
}