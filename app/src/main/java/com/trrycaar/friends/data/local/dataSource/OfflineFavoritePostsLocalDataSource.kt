package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.dao.OfflineFavoritePostDao
import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity
import com.trrycaar.friends.domain.exception.FriendDatabaseException

class OfflineFavoritePostsLocalDataSource(
    private val offlineFavoritePostDao: OfflineFavoritePostDao
) {
    suspend fun saveOfflinePostToFavorite(favoritePost: OfflineFavoritePostEntity) {
        try {
            offlineFavoritePostDao.insertPostToOfflineFavorite(favoritePost)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to save post to offline favorite")
        }
    }

    suspend fun getAllPosts(): List<OfflineFavoritePostEntity> {
        try {
            return offlineFavoritePostDao.getAllFavoritePosts()
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get offline favorite posts")
        }
    }

    suspend fun deleteOfflineFavoritePostById(postId: String) {
        try {
            offlineFavoritePostDao.deleteFavoritePostsById(postId)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to delete offline favorite post by id")
        }
    }
}