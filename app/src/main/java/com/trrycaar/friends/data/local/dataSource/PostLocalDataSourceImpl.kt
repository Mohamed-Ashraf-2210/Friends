package com.trrycaar.friends.data.local.dataSource

import androidx.paging.PagingSource
import com.trrycaar.friends.data.local.dao.PostDao
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.util.safeDbCall

class PostLocalDataSourceImpl(
    private val postDao: PostDao,
) : PostLocalDataSource {
    override suspend fun savePosts(posts: List<PostEntity>) {
        safeDbCall {
            val newPosts = posts.map {
                postDao.getPostById(it.id)?.let { existingPost ->
                    it.copy(isFavorite = existingPost.isFavorite, isSync = existingPost.isSync)
                } ?: it
            }
            postDao.savePosts(newPosts)
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

    override suspend fun saveToFavorite(id: String, isFavorite: Boolean, isSync: Boolean) {
        safeDbCall {
            postDao.saveToFavorite(id, isFavorite, isSync = isSync)
        }
    }

    override suspend fun getFavoritePostState(id: String): Boolean {
        return safeDbCall {
            postDao.getPostById(id)?.isFavorite ?: false
        }
    }

    override suspend fun updateSyncFavoritePosts() {
        safeDbCall {
            postDao.updateSyncFavoritePosts()
        }
    }
}