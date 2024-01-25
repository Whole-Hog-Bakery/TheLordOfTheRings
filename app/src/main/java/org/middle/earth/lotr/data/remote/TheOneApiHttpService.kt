package org.middle.earth.lotr.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

const val PAGE_SIZE = 107
const val PAGE = "page"
const val LIMIT = "limit"

data class TheOneApiHttpService @Inject constructor(private val client: HttpClient) : TheOneApiHttpApi {
    override suspend fun character(page: Int): HttpResponse = client.get(urlString = LotrHttpRoute.CHARACTER) {
        url {
            parameters.append(PAGE, "$page")
            parameters.append(LIMIT, "$PAGE_SIZE")
        }
    }

    override suspend fun quote(): HttpResponse = client.get(urlString = LotrHttpRoute.QUOTE)
}
