package org.middle.earth.lotr.data.remote

import android.util.Log
import arrow.core.Either
import arrow.core.raise.either
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import org.middle.earth.lotr.data.remote.PingHttpRoute.GOOGLE_CONNECTIVITY_CHECK_URL
import javax.inject.Inject

private const val TAG = "PingHttpService"

data class PingHttpService @Inject constructor(private val client: HttpClient) : PingHttpApi {

    override suspend fun ping(): Either<Throwable, HttpResponse> = either {
        Either.catch { client.get(urlString = GOOGLE_CONNECTIVITY_CHECK_URL) }
            .onLeft { networkException ->
                Log.e(TAG, "ping failed", networkException)
                Firebase.crashlytics.recordException(networkException)
            }
            .onRight { }
            .bind()
    }
}
