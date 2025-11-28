package com.trrycaar.friends.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.data.remote.dto.comments.CommentsDto
import com.trrycaar.friends.data.util.constants.EndPoints.BASE_URL
import com.trrycaar.friends.data.util.constants.EndPoints.COMMENTS
import com.trrycaar.friends.data.util.network.safeApiCall
import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.presentation.base.BasePagingSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow

class CommentRepositoryImpl(
    private val client: HttpClient
) : CommentRepository {
    override suspend fun getCommentsPost(postId: String): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BasePagingSource(
                    pageSize = 10,
                    getDataFromApi = { page, pageSize ->
                        val response: CommentsDto = safeApiCall {
                            client.get(BASE_URL + COMMENTS)
                            {
                                parameter("page", page)
                                parameter("limit", pageSize)
                            }
                        }
                        response.commentDto.map { it.toDomain() }.filter { it.postId == postId }
                    }
                )
            }
        ).flow
    }
}