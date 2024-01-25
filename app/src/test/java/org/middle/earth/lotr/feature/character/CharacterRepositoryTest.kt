package org.middle.earth.lotr.feature.character

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.middle.earth.lotr.data.local.CharacterDO
import org.middle.earth.lotr.data.local.CharacterRemoteMediator
import org.middle.earth.lotr.data.local.MiddleEarthRepository
import org.middle.earth.lotr.data.local.TheLordOfTheRingsDatabase
import org.middle.earth.lotr.data.remote.PingHttpService
import org.middle.earth.lotr.data.remote.TheOneApiHttpService
import org.middle.earth.lotr.data.remote.getPingHttpClient
import org.middle.earth.lotr.data.remote.getTheOneApiHttpClient
import org.middle.earth.lotr.data.remote.mockEngineCharactersOk
import org.middle.earth.lotr.data.remote.mockEngineNoContent
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(minSdk = 26, maxSdk = 34)
class CharacterRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()


    private lateinit var database: TheLordOfTheRingsDatabase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        val context: Context = ApplicationProvider.getApplicationContext()

        FirebaseApp.initializeApp(context)

        database = Room
            .inMemoryDatabaseBuilder(context, TheLordOfTheRingsDatabase::class.java)
            .build()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun characters() {

        val middleEarthRepository = MiddleEarthRepository(
            pingService = PingHttpService(getPingHttpClient(mockEngineNoContent)),
            theOneApiService = TheOneApiHttpService(getTheOneApiHttpClient(mockEngineCharactersOk)),
            database = database
        )

        val repository = CharacterRepository(repository = middleEarthRepository)

        val pagingState = PagingState<Int, CharacterDO>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )

        runTest {
            when (val result = CharacterRemoteMediator(middleEarthRepository).load(loadType = LoadType.REFRESH, state = pagingState)) {
                is RemoteMediator.MediatorResult.Success -> {
                    assertEquals(false, result.endOfPaginationReached)
                }

                is RemoteMediator.MediatorResult.Error -> fail()
            }

            repository.characters().testPages {

                awaitPages(this@runTest).first()

            }
        }

    }
}