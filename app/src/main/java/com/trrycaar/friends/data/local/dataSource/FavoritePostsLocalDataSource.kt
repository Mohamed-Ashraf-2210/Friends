package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.dao.FavoritePostDao
import com.trrycaar.friends.data.local.entity.FavoritePostEntity
import com.trrycaar.friends.domain.exception.FriendDatabaseException

class FavoritePostsLocalDataSource(
    private val favoritePostsDao: FavoritePostDao
) {
    suspend fun savePostToFavorite(favoritePost: FavoritePostEntity) {
        try {
            favoritePostsDao.insertPostToFavorite(favoritePost)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to save post to favorite")
        }
    }
}