package org.middle.earth.lotr.data.remote

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.middle.earth.lotr.di.NETWORK_TIME_OUT
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(minSdk = 26, maxSdk = 34)
class PingHttpServiceTest {

    @Before
    fun setUp() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun ping() {
        val httpClient = getHttpClient(mockEngineNoContent)

        val test = runBlocking {
            PingHttpService(httpClient).ping()
        }

        Assert.assertEquals(test!!.status == HttpStatusCode.NoContent, true)
    }

    @Test
    fun pingPermanentRedirect() {

        val httpClient = getHttpClient(mockEnginePermanentRedirect)

        val test = runBlocking {
            PingHttpService(httpClient).ping()
        }

        Assert.assertEquals(test, true)
    }

    @Test
    fun pingBadRequest() {

        val httpClient = getHttpClient(mockEngineBadRequest)

        val test = runBlocking {
            PingHttpService(httpClient).ping()
        }

        Assert.assertEquals(test, true)
    }

    @Test
    fun pingServiceUnavailable() {

        val httpClient = getHttpClient(mockEngineServiceUnavailable)

        val test = runBlocking {
            PingHttpService(httpClient).ping()
        }

        Assert.assertEquals(test, true)
    }

    private fun getHttpClient(mockEngine: MockEngine) = HttpClient(mockEngine) {

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
}