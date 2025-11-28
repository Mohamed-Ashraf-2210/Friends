package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.entity.FavoritePostEntity

interface FavoritePostsLocalDataSource {
    suspend fun savePostToFavorite(favoritePost: FavoritePostEntity)
}