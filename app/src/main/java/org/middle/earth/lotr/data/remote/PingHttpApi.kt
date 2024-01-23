package org.middle.earth.lotr.data.remote

import arrow.core.Either
import io.ktor.client.statement.HttpResponse

interface PingHttpApi {
    //suspend fun ping(): Either<Throwable, HttpResponse>
    suspend fun ping(): HttpResponse
}