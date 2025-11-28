package com.trrycaar.friends.data.remote.dataSource

import com.trrycaar.friends.data.remote.dto.posts.PostsDto
import com.trrycaar.friends.data.util.constants.EndPoints.BASE_URL
import com.trrycaar.friends.data.util.constants.EndPoints.POSTS
import com.trrycaar.friends.data.util.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PostRemoteDataSourceImpl(
    private val client: HttpClient
) : PostRemoteDataSource {
    override suspend fun getPosts(page: Int, pageSize: Int): PostsDto {
        return safeApiCall {
            client.get(BASE_URL + POSTS) {
                parameter("page", page)
                parameter("limit", pageSize)
            }
        }
    }
}