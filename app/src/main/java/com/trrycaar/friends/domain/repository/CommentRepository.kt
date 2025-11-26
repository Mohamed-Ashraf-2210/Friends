package com.trrycaar.friends.domain.repository

import com.trrycaar.friends.domain.entity.Comment

interface CommentRepository {
    suspend fun getCommentsPost(postId: String): List<Comment>
}