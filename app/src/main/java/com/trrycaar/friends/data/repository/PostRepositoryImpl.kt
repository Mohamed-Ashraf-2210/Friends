package com.trrycaar.friends.data.repository

import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.data.mapper.toEntity
import com.trrycaar.friends.data.remote.dto.posts.PostsDto
import com.trrycaar.friends.data.util.constants.EndPoints.BASE_URL
import com.trrycaar.friends.data.util.constants.EndPoints.POSTS
import com.trrycaar.friends.data.util.network.safeApiCall
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.repository.PostRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class PostRepositoryImpl(
    private val client: HttpClient,
    private val postLocalDataSource: PostLocalDataSource
) : PostRepository {
    override suspend fun getPosts(): List<Post> {
        return try {
            val response: PostsDto = safeApiCall {
                client.get(BASE_URL + POSTS)
            }
            postLocalDataSource.clearPosts()
            postLocalDataSource.savePosts(response.posts.map { it.toEntity() })
            response.posts.map { it.toDomain() }
        } catch (_: Exception) {
            postLocalDataSource.getPosts().map { it.toDomain() }
        }
    }
}