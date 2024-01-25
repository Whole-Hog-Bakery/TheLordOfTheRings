package org.middle.earth.lotr.data.remote

import io.ktor.client.statement.HttpResponse

interface TheOneApiHttpApi {
    suspend fun character(page: Int): HttpResponse?
    suspend fun quote(): HttpResponse?
}