package com.trrycaar.friends.data.remote.dataSource

import com.trrycaar.friends.data.remote.dto.posts.PostsDto

interface PostRemoteDataSource {
    suspend fun getPosts(page: Int, pageSize: Int): PostsDto
}