package com.trrycaar.friends.data.network_monitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.trrycaar.friends.domain.repository.OfflineFavoritePostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NetworkMonitor(
    private val offlineFavoritePostRepository: OfflineFavoritePostRepository,
    context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback: ConnectivityManager.NetworkCallback
    private val scope = CoroutineScope(Dispatchers.IO)

    private var _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    init {
        val network = connectivityManager.activeNetwork
        _isConnected.value = network != null


        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (!_isConnected.value) {
                    _isConnected.value = true
                    scope.launch {
                        offlineFavoritePostRepository.syncOfflineFavorites()
                    }
                }
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
        scope.cancel()
    }
}