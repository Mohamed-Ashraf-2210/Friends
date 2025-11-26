package com.trrycaar.friends.data.repository

import com.trrycaar.friends.data.remote.dto.CommentDto
import com.trrycaar.friends.data.remote.dto.PostDto
import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.data.util.constants.EndPoints.BASE_URL
import com.trrycaar.friends.data.util.constants.EndPoints.COMMENTS
import com.trrycaar.friends.data.util.constants.EndPoints.POSTS
import com.trrycaar.friends.data.util.network.safeApiCall
import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.repository.PostRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PostRepositoryImpl(
    private val client: HttpClient
) : PostRepository {
    override suspend fun getPosts(): List<Post> {
        val response: List<PostDto> = safeApiCall {
            client.get(BASE_URL + POSTS)
        }
        return response.map { it.toDomain() }
    }

    override suspend fun getCommentsPost(postId: String): List<Comment> {
        val response: List<CommentDto> = safeApiCall {
            client.get(BASE_URL + COMMENTS) {
                parameter("postId", postId.toInt())
            }
        }
        return response.map { it.toDomain() }
    }

    override suspend fun addPostToFavorites(postId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoritePosts(): List<Post> {
        TODO("Not yet implemented")
    }
}