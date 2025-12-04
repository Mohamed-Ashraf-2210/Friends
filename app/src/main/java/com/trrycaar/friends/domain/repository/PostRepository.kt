package com.trrycaar.friends.domain.repository

import androidx.paging.PagingData
import com.trrycaar.friends.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostsPaging(): Flow<PagingData<Post>>
    fun getFavoritePostsPaging(): Flow<PagingData<Post>>

    suspend fun getFavoritePostState(postId: String): Boolean
    suspend fun addToFavorite(postId: String)
}