package org.middle.earth.lotr.data.remote

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.middle.earth.lotr.di.NETWORK_TIME_OUT
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(minSdk = 26, maxSdk = 34)
class PingNetworkTest {

    @Before
    fun setUp() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun ping() {
        val httpClient = getPingHttpClient(mockEngineNoContent)

        val test = runBlocking {
            PingHttpService(httpClient).ping()
        }

        assertEquals(test.status == HttpStatusCode.NoContent, true)
    }

    @Test(expected = RedirectResponseException::class)
    fun pingPermanentRedirect() {

        val httpClient = getPingHttpClient(mockEnginePermanentRedirect)

        runBlocking {
            PingHttpService(httpClient).ping()
        }

    }


    @Test(expected = ClientRequestException::class)
    fun pingBadRequest() {
        val httpClient = getPingHttpClient(mockEngineBadRequest)

        runBlocking {
            PingHttpService(httpClient).ping()
        }
    }

    @Test(expected = ServerResponseException::class)
    fun pingServiceUnavailable() {
        val httpClient = getPingHttpClient(mockEngineServiceUnavailable)
        runBlocking {
            PingHttpService(httpClient).ping()
        }
    }

}


fun getPingHttpClient(mockEngine: MockEngine) = HttpClient(mockEngine) {

    expectSuccess = true
    followRedirects = false

    install(HttpTimeout) {
        requestTimeoutMillis = NETWORK_TIME_OUT
        connectTimeoutMillis = NETWORK_TIME_OUT
        socketTimeoutMillis = NETWORK_TIME_OUT
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println("log() called with: message = $message")
            }
        }
        level = LogLevel.ALL
    }

    defaultRequest {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header(HttpHeaders.Accept, "no-cache")
    }
}