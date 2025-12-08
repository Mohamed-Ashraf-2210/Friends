package com.trrycaar.friends.data.local.dataSource

import androidx.paging.PagingSource
import com.trrycaar.friends.data.local.dao.PostDao
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.util.safeDbCall

class PostLocalDataSourceImpl(
    private val postDao: PostDao
) : PostLocalDataSource {
    override suspend fun savePosts(posts: List<PostEntity>) {
        safeDbCall {
            postDao.savePosts(posts)
        }
    }

    override fun getPosts(): PagingSource<Int, PostEntity> {
        return try {
            postDao.getPostsPaginated()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getFavorites(): PagingSource<Int, PostEntity> {
        return try {
            postDao.getFavoritePosts()
        } catch (e: Exception) {
            throw e
        }
    }
}