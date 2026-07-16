package com.mak.pocketnotes.core.common.models

/**
 * This interface is for different sections in screen so that each section can be managed differently
 */
sealed interface SectionState<out T> {
    data object Loading : SectionState<Nothing>
    data class Success<T>(val data: T, val isRefreshing: Boolean = false) :
        SectionState<T>

    data object Empty : SectionState<Nothing>
    data class Error<T>(val type: ErrorType, val cachedData: T? = null) :
        SectionState<T>

    fun isInFlight(): Boolean = when (this) {
        is Loading -> true
        is Success -> isRefreshing
        else -> false
    }
}