package com.trrycaar.friends.data.repository

import com.trrycaar.friends.data.local.dataSource.OfflineFavoritePostsLocalDataSource
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity
import com.trrycaar.friends.domain.exception.FriendDatabaseException
import com.trrycaar.friends.domain.repository.OfflineFavoritePostRepository

class OfflineFavoritePostRepositoryImpl(
    private val offlineFavoritePostsLocal: OfflineFavoritePostsLocalDataSource,
    private val postLocal: PostLocalDataSource
) : OfflineFavoritePostRepository {
    override suspend fun savePostToOfflineFavorite(postId: String, isFavorite: Boolean) {
        try {
            offlineFavoritePostsLocal.saveOfflinePostToFavorite(
                OfflineFavoritePostEntity(postId = postId, isFavorite = isFavorite)
            )
        } catch (e: FriendDatabaseException) {
            throw e
        }
    }


    override suspend fun syncOfflineFavorites() {
        try {
            val offlineFavorites = offlineFavoritePostsLocal.getAll()
            offlineFavorites.forEach { offlineFavorite ->
                postLocal.saveToFavorite(offlineFavorite.postId, offlineFavorite.isFavorite)
                offlineFavoritePostsLocal.deleteById(offlineFavorite.postId)
            }
        } catch (e: FriendDatabaseException) {
            throw e
        }
    }
}