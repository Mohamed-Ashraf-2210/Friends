package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity

interface OfflineFavoritePostsLocalDataSource {
    suspend fun saveOfflinePostToFavorite(favoritePost: OfflineFavoritePostEntity)
    suspend fun getAllPosts(): List<OfflineFavoritePostEntity>
    suspend fun deleteOfflineFavoritePostById(postId: String)
}