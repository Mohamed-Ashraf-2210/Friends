package com.trrycaar.friends.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.data.remote.dataSource.CommentRemoteDataSource
import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.data.util.base.BasePagingSource
import kotlinx.coroutines.flow.Flow

class CommentRepositoryImpl(
    private val commentRemote: CommentRemoteDataSource
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
                        commentRemote.getComments(page, pageSize).commentDto.map { it.toDomain() }
                            .filter { it.postId == postId }
                    }
                )
            }
        ).flow
    }
}