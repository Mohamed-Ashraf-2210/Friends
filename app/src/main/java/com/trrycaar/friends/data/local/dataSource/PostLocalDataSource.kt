package com.trrycaar.friends.data.local.dataSource

import androidx.paging.PagingSource
import com.trrycaar.friends.data.local.entity.PostEntity

interface PostLocalDataSource {
    suspend fun savePosts(posts: List<PostEntity>)
    fun getPosts(): PagingSource<Int, PostEntity>
    fun getFavorites(): PagingSource<Int, PostEntity>
    suspend fun saveToFavorite(id: String, isFavorite: Boolean, isSync: Boolean)
    suspend fun getFavoritePostState(id: String): Boolean
    suspend fun updateSyncFavoritePosts()
}