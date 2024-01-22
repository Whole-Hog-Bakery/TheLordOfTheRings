package org.middle.earth.lotr.data.remote

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

val mockEngineOk = MockEngine { _ ->
    httpResponseData(HttpStatusCode.OK)
}

val mockEngineNoContent = MockEngine { _ ->
    httpResponseData(HttpStatusCode.NoContent)
}

val mockEnginePermanentRedirect = MockEngine { _ ->
    httpResponseData(HttpStatusCode.MovedPermanently)
}

val mockEngineBadRequest = MockEngine { _ ->
    httpResponseData(HttpStatusCode.BadRequest)
}

val mockEngineServiceUnavailable = MockEngine { _ ->
    httpResponseData(HttpStatusCode.ServiceUnavailable)
}

private fun MockRequestHandleScope.httpResponseData(statusCode: HttpStatusCode) = respond(
    content = ByteReadChannel(""),
    status = statusCode,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
)