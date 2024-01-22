package org.middle.earth.lotr.data.remote

import arrow.core.Either
import io.ktor.client.statement.HttpResponse

interface TheOneApiHttpApi {

    suspend fun character(): Either<Throwable, HttpResponse>

    suspend fun quote(): Either<Throwable, HttpResponse>

}