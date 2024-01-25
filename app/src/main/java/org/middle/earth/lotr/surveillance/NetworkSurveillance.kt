package org.middle.earth.lotr.surveillance

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

private val request = NetworkRequest.Builder()
    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
    .build()

class NetworkSurveillance @Inject constructor(private val connectivityManager: ConnectivityManager) {

    val connectionFlow: Flow<NetworkState> = callbackFlow {

        val callback: ConnectivityManager.NetworkCallback = manufactureCallback(this@callbackFlow)
        val networkCallback: ConnectivityManager.NetworkCallback = manufactureCallback(this@callbackFlow)

        connectivityManager.registerNetworkCallback(request, callback)
        connectivityManager.requestNetwork(request, networkCallback, 1000)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    private fun manufactureCallback(producerScope: ProducerScope<NetworkState>) = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            producerScope.trySend(NetworkState.Connected)
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            producerScope.trySend(NetworkState.Disconnected)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            producerScope.trySend(NetworkState.Disconnected)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            producerScope.trySend(NetworkState.Disconnected)
        }
    }
}

sealed class NetworkState() {
    data object Connected : NetworkState()
    data object Disconnected : NetworkState()
}
