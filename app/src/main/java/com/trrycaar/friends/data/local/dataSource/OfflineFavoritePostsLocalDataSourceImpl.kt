package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.dao.OfflineFavoritePostDao
import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity
import com.trrycaar.friends.domain.exception.FriendDatabaseException

class OfflineFavoritePostsLocalDataSourceImpl(
    private val offlineFavoritePostDao: OfflineFavoritePostDao
) : OfflineFavoritePostsLocalDataSource {
    override suspend fun saveOfflinePostToFavorite(favoritePost: OfflineFavoritePostEntity) {
        try {
            offlineFavoritePostDao.insertPostToOfflineFavorite(favoritePost)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to save post to offline favorite")
        }
    }

    override suspend fun getAll(): List<OfflineFavoritePostEntity> {
        try {
            return offlineFavoritePostDao.getAllFavoritePosts()
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get offline favorite posts")
        }
    }

    override suspend fun deleteById(postId: String) {
        try {
            offlineFavoritePostDao.deleteFavoritePostsById(postId)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to delete offline favorite post by id")
        }
    }
}