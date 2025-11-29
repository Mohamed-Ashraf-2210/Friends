package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity

interface OfflineFavoritePostsLocalDataSource {
    suspend fun saveOfflinePostToFavorite(favoritePost: OfflineFavoritePostEntity)
    suspend fun getAll(): List<OfflineFavoritePostEntity>
    suspend fun deleteById(postId: String)
}