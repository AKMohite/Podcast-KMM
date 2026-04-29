package com.mak.pocketnotes.domain.models

import com.mak.pocketnotes.domain.exception.PocketException

sealed interface DomainResult<out T> {
    data object Loading: DomainResult<Nothing>
    data class Success<T>(val data: T): DomainResult<T>
    data class Error(val message: String? = null, val throwable: Throwable? = null): DomainResult<Nothing>
}

suspend fun <T> safeCall(call: suspend () -> T): DomainResult<T> {
    return try {
        DomainResult.Success(call())
    } catch (e: PocketException) {
        DomainResult.Error(e.message, e)
    } catch (e: Throwable) {
        DomainResult.Error(e.message ?: "An unknown error occurred", e)
    }
}
