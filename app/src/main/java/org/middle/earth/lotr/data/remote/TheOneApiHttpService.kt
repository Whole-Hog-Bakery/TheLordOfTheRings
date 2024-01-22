package org.middle.earth.lotr.data.remote

import arrow.core.Either
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

data class TheOneApiHttpService @Inject constructor(private val client: HttpClient) : TheOneApiHttpApi {

    override suspend fun character(): Either<Throwable, HttpResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun quote(): Either<Throwable, HttpResponse> {
        TODO("Not yet implemented")
    }
}
