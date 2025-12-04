package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.entity.PostEntity

interface PostLocalDataSource {
    suspend fun savePosts(posts: List<PostEntity>)
    suspend fun getPosts(page: Int, pageSize: Int): List<PostEntity>
    suspend fun getFavorites(page: Int, pageSize: Int): List<PostEntity>
    suspend fun saveToFavorite(id: String)
    suspend fun getFavoritePostState(id: String): Boolean
}