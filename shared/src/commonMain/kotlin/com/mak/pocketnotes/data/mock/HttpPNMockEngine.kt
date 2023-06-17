package com.mak.pocketnotes.data.mock

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*

internal fun MockRequestHandleScope.handleMockResponse(
    request: HttpRequestData,
    responseHeaders: Headers
) = if (request.url.encodedPath == "/posts") {
    respond("", HttpStatusCode.OK, responseHeaders)
} else {
    error("Unhandled ${request.url.encodedPath}")
}