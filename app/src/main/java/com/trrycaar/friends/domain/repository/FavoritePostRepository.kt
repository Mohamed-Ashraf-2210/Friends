package com.trrycaar.friends.domain.repository

import com.trrycaar.friends.domain.entity.Post

interface FavoritePostRepository {
    suspend fun addPostToFavorite(postId: String)
    suspend fun addPostToOfflineFavorite(postId: String)
    suspend fun getFavoritePosts(): List<Post>
    suspend fun syncOfflineFavorites()
}