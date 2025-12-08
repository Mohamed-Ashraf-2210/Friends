package com.trrycaar.friends.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.trrycaar.friends.data.local.FriendsDatabase
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.data.remote.dataSource.PostRemoteDataSource
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PostRepositoryImpl(
    private val postLocal: PostLocalDataSource,
    private val postRemote: PostRemoteDataSource,
    private val appDatabase: FriendsDatabase,
) : PostRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPostsPaging(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false, prefetchDistance = 5),
            remoteMediator = PostRemoteMediator(appDatabase, postLocal, postRemote),
            pagingSourceFactory = { postLocal.getPosts() }
        ).flow.map { it.map { post -> post.toDomain() } }
    }

    override fun getFavoritePostsPaging(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false, prefetchDistance = 5),
            pagingSourceFactory = { postLocal.getFavorites() }
        ).flow.map { it.map { postEntity -> postEntity.toDomain() } }
    }
}