package com.trrycaar.friends.data.repository

import com.trrycaar.friends.data.local.dataSource.OfflineFavoritePostsLocalDataSource
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity
import com.trrycaar.friends.data.util.safeCall
import com.trrycaar.friends.domain.repository.OfflineFavoritePostRepository

class OfflineFavoritePostRepositoryImpl(
    private val offlineFavoritePostsLocal: OfflineFavoritePostsLocalDataSource,
    private val postLocal: PostLocalDataSource
) : OfflineFavoritePostRepository {
    override suspend fun savePostToOfflineFavorite(postId: String, isFavorite: Boolean) {
        safeCall {
            offlineFavoritePostsLocal.saveOfflinePostToFavorite(
                OfflineFavoritePostEntity(postId = postId, isFavorite = isFavorite)
            )
        }
    }


    override suspend fun syncOfflineFavorites() {
        safeCall {
            val offlineFavorites = offlineFavoritePostsLocal.getAll()
            offlineFavorites.forEach { offlineFavorite ->
                postLocal.saveToFavorite(offlineFavorite.postId, offlineFavorite.isFavorite)
                offlineFavoritePostsLocal.deleteById(offlineFavorite.postId)
            }
        }
    }
}