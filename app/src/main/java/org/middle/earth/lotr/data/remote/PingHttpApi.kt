package org.middle.earth.lotr.data.remote

import io.ktor.client.statement.HttpResponse

interface PingHttpApi {
    suspend fun ping(): HttpResponse
}