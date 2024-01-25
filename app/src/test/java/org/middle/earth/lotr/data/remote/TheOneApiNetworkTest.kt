package org.middle.earth.lotr.data.remote

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.middle.earth.lotr.BuildConfig
import org.middle.earth.lotr.data.remote.dto.character.CharacterResponse
import org.middle.earth.lotr.di.NETWORK_TIME_OUT
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(minSdk = 26, maxSdk = 34)
class TheOneApiNetworkTest {

    @Before
    fun setUp() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun character() {
        val httpClient = getTheOneApiHttpClient(mockEngineCharactersOk)

        runBlocking {
            val response = TheOneApiHttpService(httpClient).character(1)
            val characters = response.body<CharacterResponse>()
            assertEquals(characters.characters.isNotEmpty(), true)
        }

    }

    @Test(expected = ClientRequestException::class)
    fun characterBadRequest() {
        val httpClient = getTheOneApiHttpClient(mockEngineBadRequest)

        runBlocking {
            TheOneApiHttpService(httpClient).character(1)
        }
    }

    @Test(expected = ServerResponseException::class)
    fun characterServerResponseException() {
        val httpClient = getTheOneApiHttpClient(mockEngineServiceUnavailable)

        runBlocking {
            TheOneApiHttpService(httpClient).character(1)
        }
    }

}


fun getTheOneApiHttpClient(mockEngine: MockEngine) = HttpClient(mockEngine) {

    expectSuccess = true

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                useAlternativeNames = true
                ignoreUnknownKeys = false
                encodeDefaults = true
            }
        )
    }

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

    install(ResponseObserver) {
        onResponse { response ->
            println("providesHttpClient() called with: response = $response\n${response.status.value}")
        }
    }

    defaultRequest {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header(HttpHeaders.Authorization, "Bearer ${BuildConfig.THE_ONE_API_ACCESS_TOKEN}")
        header(HttpHeaders.UserAgent, "Android Mock User Agent")
    }
}