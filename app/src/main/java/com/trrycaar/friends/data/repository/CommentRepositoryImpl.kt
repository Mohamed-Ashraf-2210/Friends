package com.trrycaar.friends.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.data.remote.dataSource.CommentRemoteDataSource
import com.trrycaar.friends.data.remote.paging.ApiPagingSource
import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow

class CommentRepositoryImpl(
    private val commentRemote: CommentRemoteDataSource
) : CommentRepository {
    override fun getCommentsPost(postId: String): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                ApiPagingSource(
                    getDataFromApi = { page, pageSize ->
                        commentRemote.getComments(page, pageSize).commentDto.map { it.toDomain() }
                            .filter { it.postId == postId }
                    }
                )
            }
        ).flow
    }
}