package org.middle.earth.lotr.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

const val PAGE_SIZE = 107

data class TheOneApiHttpService @Inject constructor(private val client: HttpClient) : TheOneApiHttpApi {

    override suspend fun character(page: Int): HttpResponse = client.get(urlString = LotrHttpRoute.CHARACTER) {

        url {
            parameters.append("page", "$page")
            parameters.append("limit", "$PAGE_SIZE")
        }
    }

    override suspend fun quote(): HttpResponse = client.get(urlString = LotrHttpRoute.QUOTE)

}
