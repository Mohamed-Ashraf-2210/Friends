package com.trrycaar.friends.domain.repository

import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<List<Post>>

    fun getCommentsPost(postId: String): Flow<List<Comment>>

    suspend fun addPostToFavorites(postId: String)

    suspend fun removePostFromFavorites(postId: String)

    fun getFavoritePosts(userId: String): Flow<List<Post>>
}