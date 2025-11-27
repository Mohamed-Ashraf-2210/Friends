package com.trrycaar.friends

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.trrycaar.friends.domain.repository.FavoritePostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NetworkMonitor(
    private val favoritePostRepository: FavoritePostRepository,
    context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback: ConnectivityManager.NetworkCallback
    private val scope = CoroutineScope(Dispatchers.IO)

    @Volatile
    private var isConnected: Boolean = false

    init {
        val network = connectivityManager.activeNetwork
        isConnected = network != null


        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (!isConnected) {
                    isConnected = true
                    scope.launch {
                        favoritePostRepository.syncOfflineFavorites()
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected = false
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
        scope.cancel()
    }
    fun isOnline(): Boolean {
        return isConnected
    }
}