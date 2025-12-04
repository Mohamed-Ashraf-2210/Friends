package com.trrycaar.friends.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.data.mapper.toEntity
import com.trrycaar.friends.data.remote.dataSource.PostRemoteDataSource
import com.trrycaar.friends.data.util.base.BasePagingSource
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.exception.FriendDatabaseException
import com.trrycaar.friends.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class PostRepositoryImpl(
    private val postLocal: PostLocalDataSource,
    private val postRemote: PostRemoteDataSource
) : PostRepository {
    override fun getPostsPaging(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BasePagingSource(
                    pageSize = 10,
                    onError = { throw it },
                    getDataFromApi = { page, pageSize ->
                        postRemote.getPosts(page, pageSize).posts.map { it.toDomain() }
                    },
                    getDataFromDataBase = { page, pageSize ->
                        postLocal.getPosts(page, pageSize).map { it.toDomain() }
                    },
                    saveDataToDataBase = { posts ->
                        postLocal.savePosts(posts.map { it.toEntity() })
                    }
                )
            }
        ).flow
    }

    override fun getFavoritePostsPaging(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BasePagingSource(
                    pageSize = 10,
                    onError = { throw it },
                    getDataFromDataBase = { page, pageSize ->
                        postLocal.getFavorites(page, pageSize).map { it.toDomain() }
                    }
                )
            }
        ).flow
    }

    override suspend fun getFavoritePostState(postId: String): Boolean {
        return try {
            postLocal.getFavoritePostState(postId)
        } catch (e: FriendDatabaseException) {
            throw e
        }
    }

    override suspend fun addToFavorite(postId: String) {
        try {
            postLocal.saveToFavorite(postId)
        } catch (e: FriendDatabaseException) {
            throw e
        }
    }
}