package com.trrycaar.friends.data.remote.dataSource

import com.trrycaar.friends.data.remote.dto.comments.CommentsDto
import com.trrycaar.friends.data.util.constants.EndPoints.BASE_URL
import com.trrycaar.friends.data.util.constants.EndPoints.COMMENTS
import com.trrycaar.friends.data.util.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class CommentRemoteDataSourceImpl(
    private val client: HttpClient
) : CommentRemoteDataSource {
    override suspend fun getComments(
        page: Int,
        pageSize: Int
    ): CommentsDto {
        return safeApiCall {
            client.get(BASE_URL + COMMENTS)
            {
                parameter("page", page)
                parameter("limit", pageSize)
            }
        }
    }
}