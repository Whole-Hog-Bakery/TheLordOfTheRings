package org.middle.earth.lotr.data.remote

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert
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
class TheOneApiHttpServiceTest {

    @Before
    fun setUp() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun character() {
        val httpClient = getHttpClient(mockEngineCharactersOk)

        runBlocking {
            TheOneApiHttpService(httpClient)
                .character()
                .onLeft { Assert.fail() }
                .onRight { response ->
                    assertEquals(response.status == HttpStatusCode.OK, true)
                    val characters = response.body<CharacterResponse>()
                    assertEquals(characters.docs.isNotEmpty(), true)
                }
        }
    }

    @Test
    fun characterBadRequest() {
        val httpClient = getHttpClient(mockEngineBadRequest)

        runBlocking {
            TheOneApiHttpService(httpClient)
                .character()
                .onLeft { assertEquals(it is ClientRequestException, true) }
                .onRight { Assert.fail() }
        }
    }

    private fun getHttpClient(mockEngine: MockEngine) = HttpClient(mockEngine) {

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
}