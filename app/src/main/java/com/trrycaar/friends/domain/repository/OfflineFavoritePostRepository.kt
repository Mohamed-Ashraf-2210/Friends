package com.trrycaar.friends.domain.repository

interface OfflineFavoritePostRepository {
    suspend fun addPostToOfflineFavorite(postId: String)
    suspend fun syncOfflineFavorites()
}