package com.trrycaar.friends.domain.repository

import androidx.paging.PagingData
import com.trrycaar.friends.domain.entity.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun getCommentsPost(postId: String): Flow<PagingData<Comment>>
}