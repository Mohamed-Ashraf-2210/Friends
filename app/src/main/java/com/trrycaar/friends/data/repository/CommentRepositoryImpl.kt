package com.trrycaar.friends.data.repository

import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.data.remote.dto.comments.CommentsDto
import com.trrycaar.friends.data.util.constants.EndPoints.BASE_URL
import com.trrycaar.friends.data.util.constants.EndPoints.COMMENTS
import com.trrycaar.friends.data.util.network.safeApiCall
import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.repository.CommentRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class CommentRepositoryImpl(
    private val client: HttpClient
) : CommentRepository {
    override suspend fun getCommentsPost(postId: String): List<Comment> {
        val response: CommentsDto = safeApiCall {
            client.get(BASE_URL + COMMENTS)
        }
        return response.commentDto.map { it.toDomain() }.filter { it.postId == postId }
    }
}