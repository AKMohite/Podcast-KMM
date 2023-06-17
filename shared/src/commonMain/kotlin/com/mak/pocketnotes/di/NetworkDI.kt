package com.mak.pocketnotes.di

import com.mak.pocketnotes.PNPublicConfig
import com.mak.pocketnotes.data.mock.handleMockResponse
import com.mak.pocketnotes.data.remote.dto.ErrorDTO
import com.mak.pocketnotes.domain.exception.ExceptionType
import com.mak.pocketnotes.domain.exception.PocketAPIException
import com.mak.pocketnotes.domain.exception.UnknownAPIException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

internal fun createHttpClient(json: Json, enableNetworkLogs: Boolean = false): HttpClient {
    val httpClient = if (PNPublicConfig.isProd) {
        getApiEngine()
    } else {
        getMockEngine()
    }
    return httpClient
}

private fun getApiEngine(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
        })
    }
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = API_HOST
            header("X-ListenAPI-Key", API_KEY)
//                path("api/")
//                parametersOf("api_key", "")
        }
    }

    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, request ->
            /*val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
            val exceptionResponse = clientException.response
            if (exceptionResponse.status == HttpStatusCode.NotFound) {
                val exceptionResponseText = exceptionResponse.bodyAsText()
                throw MissingPageException(exceptionResponse, exceptionResponseText)
            }*/
            throw handleKtorExceptions(exception) ?: UnknownAPIException(throwable = exception)
        }
    }
}

private fun getMockEngine(): HttpClient = HttpClient(MockEngine) {
    engine {
        val responseHeaders = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
        addHandler { request ->
            handleMockResponse(request, responseHeaders)
        }
    }
}

private suspend fun handleKtorExceptions(exception: Throwable): Throwable? {
//        todo check for ktor exceptions instead java
    return when (exception) {
        is ClientRequestException -> {
            handleAPIExceptions(exception)
        }
//            is java.net.SocketTimeoutException -> RequestTimeoutException(throwable = exception)
//            is java.io.IOException -> NoNetworkException()
        else -> null
    }
}

private suspend fun handleAPIExceptions(exception: ClientRequestException): Throwable? {
    val exceptionResponse = exception.response
    val error = getErrorDTO(exceptionResponse)
    return PocketAPIException(
        code = exceptionResponse.status.value,
        errorMsg = error?.message ?: ExceptionType.UNKNOWN.message,
        throwable = exception
    )
}

/*suspend inline fun <T> safeApiCall(responseFunction: () -> T): ResultWrapper<T> {
    return try {
        ResultWrapper.Success(responseFunction.invoke()) // Or responseFunction()
    } catch (e: Throwable) {
        ResultWrapper.Error<T>(e.message ?: ExceptionType.UNKNOWN.message, e)
    }
}*/

private suspend fun getErrorDTO(exceptionResponse: HttpResponse): ErrorDTO? {
    return try {
        exceptionResponse.body<ErrorDTO>()
    } catch (e: Throwable) {
//        might throw json parse exception
        null
    }
}

//const val a = BuildCo
private const val API_HOST = "listen-api-test.listennotes.com"
//private const val API_HOST = "listen-api.listennotes.com"
private const val API_KEY = ""