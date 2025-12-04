package com.trrycaar.friends.domain.repository

interface OfflineFavoritePostRepository {
    suspend fun savePostToOfflineFavorite(postId: String, isFavorite: Boolean)
    suspend fun syncOfflineFavorites()
}