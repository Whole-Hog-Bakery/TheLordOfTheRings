package org.middle.earth.lotr.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import org.junit.Assert.*

import junit.framework.TestCase
import kotlinx.coroutines.runBlocking

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.middle.earth.lotr.data.remote.PingHttpService
import org.middle.earth.lotr.data.remote.TheOneApiHttpService
import org.middle.earth.lotr.data.remote.dto.character.CharacterResponse
import org.middle.earth.lotr.data.remote.getPingHttpClient
import org.middle.earth.lotr.data.remote.getTheOneApiHttpClient
import org.middle.earth.lotr.data.remote.mockEngineBadRequest
import org.middle.earth.lotr.data.remote.mockEngineCharactersOk
import org.middle.earth.lotr.data.remote.mockEngineCharactersOkEmpty
import org.middle.earth.lotr.data.remote.mockEngineNoContent
import org.middle.earth.lotr.data.remote.mockEngineServiceUnavailable
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(minSdk = 26, maxSdk = 34)
class CharacterRemoteMediatorTest {

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    private lateinit var database: TheLordOfTheRingsDatabase

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()

        FirebaseApp.initializeApp(context)

        database = Room
            .inMemoryDatabaseBuilder(context, TheLordOfTheRingsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun initialize() {
        val repository = MiddleEarthRepository(
            pingService = PingHttpService(getPingHttpClient(mockEngineNoContent)),
            theOneApiService = TheOneApiHttpService(getTheOneApiHttpClient(mockEngineCharactersOk)),
            database = database
        )

        runBlocking {
            val initializeAction: RemoteMediator.InitializeAction = CharacterRemoteMediator(repository).initialize()
            assertEquals(true, initializeAction == RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH)
        }
    }

    @Test
    fun load() {
        val repository = MiddleEarthRepository(
            pingService = PingHttpService(getPingHttpClient(mockEngineNoContent)),
            theOneApiService = TheOneApiHttpService(getTheOneApiHttpClient(mockEngineCharactersOk)),
            database = database
        )

        val pagingState = PagingState<Int, CharacterDO>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )

        runBlocking {
            when (val result = CharacterRemoteMediator(repository).load(loadType = LoadType.REFRESH, state = pagingState)) {
                is RemoteMediator.MediatorResult.Success -> {
                    assertEquals(false, result.endOfPaginationReached)
                }

                is RemoteMediator.MediatorResult.Error -> fail()
            }
        }
    }

    @Test
    fun loadEmpty() {
        val repository = MiddleEarthRepository(
            pingService = PingHttpService(getPingHttpClient(mockEngineNoContent)),
            theOneApiService = TheOneApiHttpService(getTheOneApiHttpClient(mockEngineCharactersOkEmpty)),
            database = database
        )

        val pagingState = PagingState<Int, CharacterDO>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )

        runBlocking {
            when (val result = CharacterRemoteMediator(repository).load(loadType = LoadType.REFRESH, state = pagingState)) {
                is RemoteMediator.MediatorResult.Success -> {
                    assertEquals(true, result.endOfPaginationReached)
                }

                is RemoteMediator.MediatorResult.Error -> fail()
            }
        }
    }

    @Test
    fun loadPingException() {
        val repository = MiddleEarthRepository(
            pingService = PingHttpService(getPingHttpClient(mockEngineServiceUnavailable)),
            theOneApiService = TheOneApiHttpService(getTheOneApiHttpClient(mockEngineCharactersOkEmpty)),
            database = database
        )

        val pagingState = PagingState<Int, CharacterDO>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )

        runBlocking {
            when (val result = CharacterRemoteMediator(repository).load(loadType = LoadType.REFRESH, state = pagingState)) {
                is RemoteMediator.MediatorResult.Success -> {
                    fail()
                }

                is RemoteMediator.MediatorResult.Error -> {
                    assertEquals(true , result.throwable is RuntimeException)
                }
            }
        }
    }

    @Test
    fun loadTheOneApiException() {
        val repository = MiddleEarthRepository(
            pingService = PingHttpService(getPingHttpClient(mockEngineNoContent)),
            theOneApiService = TheOneApiHttpService(getTheOneApiHttpClient(mockEngineBadRequest)),
            database = database
        )

        val pagingState = PagingState<Int, CharacterDO>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )

        runBlocking {
            when (val result = CharacterRemoteMediator(repository).load(loadType = LoadType.REFRESH, state = pagingState)) {
                is RemoteMediator.MediatorResult.Success -> {
                    fail()
                }

                is RemoteMediator.MediatorResult.Error -> {
                    assertEquals(true , result.throwable is RuntimeException)
                }
            }
        }
    }
}