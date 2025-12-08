package com.trrycaar.friends.data.repository

import com.trrycaar.friends.data.local.dataSource.FavoritePostsLocalDataSource
import com.trrycaar.friends.data.util.network.NetworkMonitor
import com.trrycaar.friends.data.util.safeCall
import com.trrycaar.friends.domain.repository.FavoritePostsRepository

class FavoritePostsRepositoryImpl(
    private val favoritePostsLocal: FavoritePostsLocalDataSource,
    private val networkMonitor: NetworkMonitor
) : FavoritePostsRepository {
    override suspend fun getFavoritePostState(postId: String): Boolean {
        return safeCall {
            favoritePostsLocal.getFavoritePostState(postId)
        }
    }

    override suspend fun addFavorite(postId: String) {
        safeCall {
            val isNetworkConnection = networkMonitor.isConnected.value
            favoritePostsLocal.addFavorite(id = postId, isSync = isNetworkConnection)
        }
    }

    override suspend fun removeFavorite(postId: String) {
        safeCall {
            val isNetworkConnection = networkMonitor.isConnected.value
            favoritePostsLocal.removeFavorite(id = postId, isSync = isNetworkConnection)
        }
    }
}