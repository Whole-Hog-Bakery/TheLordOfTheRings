package org.middle.earth.lotr.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.withContext
import org.middle.earth.lotr.data.remote.PingHttpRoute.GOOGLE_CONNECTIVITY_CHECK_URL
import org.middle.earth.lotr.di.NETWORK
import javax.inject.Inject

data class PingHttpService @Inject constructor(private val client: HttpClient) : PingHttpApi {
    override suspend fun ping(): HttpResponse = withContext(NETWORK) { client.get(urlString = GOOGLE_CONNECTIVITY_CHECK_URL) }
}
