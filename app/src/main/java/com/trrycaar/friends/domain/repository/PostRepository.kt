package com.trrycaar.friends.domain.repository

import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.util.Result

interface PostRepository {
    suspend fun getPosts(): Result<List<Post>>

    suspend fun addPostToFavorites(postId: String)

    suspend fun getFavoritePosts(): List<Post>
}