package org.middle.earth.lotr.data.local

import android.util.Log
import arrow.core.Either
import arrow.core.raise.either
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import io.ktor.client.statement.HttpResponse
import org.middle.earth.lotr.data.remote.PingHttpApi
import org.middle.earth.lotr.data.remote.TheOneApiHttpApi
import javax.inject.Inject

private const val TAG = "MiddleEarthRepository"

class MiddleEarthRepository @Inject constructor(
    private val pingService: PingHttpApi,
    private val theOneApiService: TheOneApiHttpApi,
    val database: TheLordOfTheRingsDatabase
) : TheOneApiHttpApi {
    override suspend fun character(page: Int): HttpResponse? = characterInternal(page).fold(ifLeft = { null }, ifRight = { it })

    private suspend fun characterInternal(page: Int) = either {
        Either.catch { pingService.ping() }
            .onLeft { networkException ->
                Log.e(TAG, "no internet access", networkException)
                Firebase.crashlytics.recordException(networkException)
            }
            .onRight { }
            .bind()

        Either.catch { theOneApiService.character(page) }
            .onLeft { networkException ->
                Log.e(TAG, "get Characters failed", networkException)
                Firebase.crashlytics.recordException(networkException)
            }
            .onRight { }
            .bind()
    }

    override suspend fun quote(): HttpResponse {
        TODO("Not yet implemented")
    }
}
