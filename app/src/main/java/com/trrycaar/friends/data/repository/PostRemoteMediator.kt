package com.trrycaar.friends.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.trrycaar.friends.data.local.FriendsDatabase
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.local.entity.RemoteKeysEntity
import com.trrycaar.friends.data.mapper.toEntity
import com.trrycaar.friends.data.remote.dataSource.PostRemoteDataSource
import com.trrycaar.friends.data.util.mapToDomainException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val appDatabase: FriendsDatabase,
    private val postLocal: PostLocalDataSource,
    private val postRemote: PostRemoteDataSource
) : RemoteMediator<Int, PostEntity>() {
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PostEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { post ->
                appDatabase.remoteKeysDao().remoteKeysPostId(post.id)
            }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val posts = postRemote.getPosts(
                page = loadKey,
                pageSize = state.config.pageSize
            )

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteKeysDao().clearRemoteKeys()
                    appDatabase.postDao().clearPosts()
                }
                val nextKey = loadKey + 1

                val keys = posts.posts.map {
                    RemoteKeysEntity(postId = it.id.toString(), nextKey = nextKey)
                }
                appDatabase.remoteKeysDao().insertAll(keys)
                val postEntities = posts.posts.map { it.toEntity() }
                postLocal.savePosts(postEntities)
            }
            MediatorResult.Success(endOfPaginationReached = posts.posts.isEmpty())
        } catch (e: Throwable) {
            MediatorResult.Error(mapToDomainException(e))
        }
    }
}