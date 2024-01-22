package org.middle.earth.lotr.data.remote

import android.util.Log
import arrow.core.Either
import arrow.core.raise.either
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

private const val TAG = "TheOneApiHttpService"

data class TheOneApiHttpService @Inject constructor(private val client: HttpClient) : TheOneApiHttpApi {

    override suspend fun character(): Either<Throwable, HttpResponse> = either {
        Either.catch { client.get(urlString = LotrHttpRoute.CHARACTER) }
            .onLeft { networkException ->
                Log.e(TAG, "get Characters failed", networkException)
                Firebase.crashlytics.recordException(networkException)
            }
            .onRight { }
            .bind()
    }


    override suspend fun quote(): Either<Throwable, HttpResponse> = either {
        Either.catch { client.get(urlString = LotrHttpRoute.QUOTE) }
            .onLeft { networkException ->
                Log.e(TAG, "get Quotes failed", networkException)
                Firebase.crashlytics.recordException(networkException)
            }
            .onRight { }
            .bind()
    }
}
