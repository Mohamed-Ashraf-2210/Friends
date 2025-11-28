package com.trrycaar.friends.data.remote.dataSource

import com.trrycaar.friends.data.remote.dto.comments.CommentsDto

interface CommentRemoteDataSource {
    suspend fun getComments(page: Int, pageSize: Int): CommentsDto
}