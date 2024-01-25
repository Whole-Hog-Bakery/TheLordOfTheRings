package org.middle.earth.lotr.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import org.junit.Assert.*

import junit.framework.TestCase
import kotlinx.coroutines.runBlocking

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.middle.earth.lotr.data.remote.PingHttpService
import org.middle.earth.lotr.data.remote.TheOneApiHttpService
import org.middle.earth.lotr.data.remote.dto.character.CharacterResponse
import org.middle.earth.lotr.data.remote.getPingHttpClient
import org.middle.earth.lotr.data.remote.getTheOneApiHttpClient
import org.middle.earth.lotr.data.remote.mockEngineBadRequest
import org.middle.earth.lotr.data.remote.mockEngineCharactersOk
import org.middle.earth.lotr.data.remote.mockEngineNoContent
import org.middle.earth.lotr.data.remote.mockEngineServiceUnavailable
import org.middle.earth.lotr.di.DATABASE_NAME
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(minSdk = 26, maxSdk = 34)
class MiddleEarthRepositoryTest : TestCase() {

    private lateinit var database: TheLordOfTheRingsDatabase

    @Before
    public override fun setUp() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())

        database = Room
            .inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), TheLordOfTheRingsDatabase::class.java)
            .build()
    }

    @After
    public override fun tearDown() {
        database.close()
    }

    @Test
    fun characterOK() {

        val repository = MiddleEarthRepository(
            pingService = PingHttpService(getPingHttpClient(mockEngineNoContent)),
            theOneApiService = TheOneApiHttpService(getTheOneApiHttpClient(mockEngineCharactersOk)),
            database = database
        )

        runBlocking {
            val response: HttpResponse? = repository.character(1)
            assertEquals(response == null, false)
            assertEquals(response!!.status == HttpStatusCode.OK, true)

            val characters: CharacterResponse = response.body()
            assertEquals(characters.characters.isNotEmpty(), true)

        }
    }
    @Test
    fun characterPingFail() {

        val repository = MiddleEarthRepository(
            pingService = PingHttpService(getPingHttpClient(mockEngineServiceUnavailable)),
            theOneApiService = TheOneApiHttpService(getTheOneApiHttpClient(mockEngineCharactersOk)),
            database = database
        )

        runBlocking {
            val response: HttpResponse? = repository.character(1)
            assertEquals(response == null, true)
        }
    }
    @Test
    fun characterPingOKCharacterFail() {

        val repository = MiddleEarthRepository(
            pingService = PingHttpService(getPingHttpClient(mockEngineNoContent)),
            theOneApiService = TheOneApiHttpService(getTheOneApiHttpClient(mockEngineBadRequest)),
            database = database
        )

        runBlocking {
            val response: HttpResponse? = repository.character(1)
            assertEquals(response == null, true)
        }
    }
}