package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.dao.OfflineFavoritePostDao
import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity
import com.trrycaar.friends.data.util.safeDataCall

class OfflineFavoritePostsLocalDataSourceImpl(
    private val offlineFavoritePostDao: OfflineFavoritePostDao
) : OfflineFavoritePostsLocalDataSource {
    override suspend fun saveOfflinePostToFavorite(favoritePost: OfflineFavoritePostEntity) {
        safeDataCall {
            offlineFavoritePostDao.insertPostToOfflineFavorite(favoritePost)
        }
    }

    override suspend fun getAll(): List<OfflineFavoritePostEntity> {
        return safeDataCall {
            offlineFavoritePostDao.getAllFavoritePosts()
        }
    }

    override suspend fun deleteById(postId: String) {
        safeDataCall {
            offlineFavoritePostDao.deleteFavoritePostsById(postId)
        }
    }
}