package com.trrycaar.friends.domain.repository

interface FavoritePostsRepository {
    suspend fun getFavoritePostState(postId: String): Boolean
    suspend fun addFavorite(postId: String)
    suspend fun removeFavorite(postId: String)
}