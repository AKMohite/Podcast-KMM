package com.mak.pocketnotes.core.remote.utils

import com.mak.pocketnotes.core.common.models.DomainException
import com.mak.pocketnotes.core.common.models.ErrorType
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

// Define Result and Error types if not already present
sealed interface RemoteResult<out T> {
    fun getOrThrow(): T {
        return when (this) {
            is Failure -> throw DomainException(error)
            is Success -> this.data
        }
    }

    data class Success<out T>(val data: T) : RemoteResult<T>
    data class Failure(val error: ErrorType) : RemoteResult<Nothing>
}

internal suspend inline fun <reified T> safeApiCall(
    block: suspend () -> HttpResponse
): RemoteResult<T> {
    return try {
        val response = block()
        if (response.status.isSuccess()) {
            RemoteResult.Success(response.body<T>())
        } else {
            RemoteResult.Failure(response.status.toErrorType())
        }
    } catch (e: Exception) {
        when (e) {
            is JsonConvertException, is SerializationException -> RemoteResult.Failure(ErrorType.PARSE)
            is HttpRequestTimeoutException, is IOException -> RemoteResult.Failure(ErrorType.NO_CONNECTIVITY)
            is CancellationException -> throw e
            else -> RemoteResult.Failure(ErrorType.UNKNOWN)
        }
    }
}

private fun HttpStatusCode.toErrorType(): ErrorType = when (value) {
    in HttpStatusCode.InternalServerError.value..HttpStatusCode(
        599,
        "Network Connect Timeout"
    ).value -> ErrorType.SERVER_ERROR

    HttpStatusCode.NotFound.value -> ErrorType.NOT_FOUND
    HttpStatusCode.Unauthorized.value -> ErrorType.UNAUTHORIZED
    HttpStatusCode.RequestTimeout.value -> ErrorType.NO_CONNECTIVITY
    else -> ErrorType.UNKNOWN
}