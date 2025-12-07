package com.trrycaar.friends.data.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkMonitor(
    context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback: ConnectivityManager.NetworkCallback

    private var _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    init {
        val network = connectivityManager.activeNetwork
        _isConnected.value = network != null


        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _isConnected.value = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _isConnected.value = false
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}