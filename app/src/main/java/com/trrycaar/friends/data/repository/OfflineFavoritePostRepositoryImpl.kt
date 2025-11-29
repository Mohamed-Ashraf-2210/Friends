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
    override suspend fun addPostToOfflineFavorite(postId: String) {
        try {
            offlineFavoritePostsLocal.saveOfflinePostToFavorite(
                OfflineFavoritePostEntity(postId)
            )
        } catch (e: FriendDatabaseException) {
            throw e
        }
    }


    override suspend fun syncOfflineFavorites() {
        try {
            val offlineFavorites = offlineFavoritePostsLocal.getAll()
            offlineFavorites.forEach { offlineFavorite ->
                postLocal.saveToFavorite(offlineFavorite.postId)
                offlineFavoritePostsLocal.deleteById(offlineFavorite.postId)
            }
        } catch (e: FriendDatabaseException) {
            throw e
        }
    }
}