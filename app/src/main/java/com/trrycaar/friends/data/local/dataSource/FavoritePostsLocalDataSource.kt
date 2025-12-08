package com.trrycaar.friends.data.local.dataSource

interface FavoritePostsLocalDataSource {
    suspend fun addFavorite(id: String, isSync: Boolean)
    suspend fun removeFavorite(id: String, isSync: Boolean)
    suspend fun updateSyncFavoritePosts()
    suspend fun deletePendingRemovedFavorites()
    suspend fun getFavoritePostState(id: String): Boolean
}