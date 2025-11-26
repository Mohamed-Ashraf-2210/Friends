package com.trrycaar.friends.domain.repository

import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.entity.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>

    suspend fun getCommentsPost(postId: String): List<Comment>

    suspend fun addPostToFavorites(postId: String)

    suspend fun getFavoritePosts(): List<Post>
}