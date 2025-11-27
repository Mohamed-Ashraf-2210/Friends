package com.trrycaar.friends.domain.repository

import com.trrycaar.friends.domain.entity.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
}