package org.middle.earth.lotr.di

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.middle.earth.lotr.BuildConfig.THE_ONE_API_ACCESS_TOKEN
import org.middle.earth.lotr.data.remote.PingHttpApi
import org.middle.earth.lotr.data.remote.PingHttpService
import org.middle.earth.lotr.data.remote.TheOneApiHttpApi
import org.middle.earth.lotr.data.remote.TheOneApiHttpService
import javax.inject.Qualifier
import javax.inject.Singleton

private const val TAG = "NetworkModule"
const val NETWORK_TIME_OUT = 6_000L


data class UserAgent(val value: String)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PingHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TheOneApiHttpClient

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @PingHttpClient
    @Provides
    @Singleton
    fun providesPingHttpClient(): HttpClient = HttpClient(Android) {

        expectSuccess = true

        followRedirects = false

        install(HttpTimeout) {
            requestTimeoutMillis = NETWORK_TIME_OUT
            connectTimeoutMillis = NETWORK_TIME_OUT
            socketTimeoutMillis = NETWORK_TIME_OUT
        }

        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, "no-cache")
        }
    }

    @TheOneApiHttpClient
    @Provides
    @Singleton
    fun providesHttpClient(userAgent: UserAgent): HttpClient = HttpClient(Android) {

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
                    Log.d(TAG, "log() called with: message = $message")
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d(TAG, "providesHttpClient() called with: response = $response\n${response.status.value}")
            }
        }

        defaultRequest {
            header(HttpHeaders.Authorization, "Bearer $THE_ONE_API_ACCESS_TOKEN")
            header(HttpHeaders.UserAgent, userAgent.value)
        }
    }

    @Provides
    @Singleton
    fun providesPingApiService(@PingHttpClient httpClient: HttpClient): PingHttpApi = PingHttpService(httpClient)

    @Provides
    @Singleton
    fun providesTheOneApiHttpService(@TheOneApiHttpClient httpClient: HttpClient): TheOneApiHttpApi = TheOneApiHttpService(httpClient)

    @Provides
    @Singleton
    fun providesUserAgent(@ApplicationContext appContext: Context): UserAgent =
        UserAgent(
            "${getApplicationName(appContext)}/" +
                    "${getVersion(appContext).first} " +
                    "(${appContext.packageName}; " +
                    "build:${getVersion(appContext).second} " +
                    "Android SDK ${Build.VERSION.SDK_INT}) " +
                    getDeviceName()
        )

    /******************************************************************************************************************************
     *
     * U S E R  A G E N T
     *
     ******************************************************************************************************************************/
    private fun getVersion(context: Context): Pair<String, Int> {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            Pair(info.versionName, info.longVersionCode.toInt())
        } else {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            Pair(info.versionName, info.versionCode)
        }
    }

    private fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
    }

    private fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            model
        } else "$manufacturer $model"
    }

}