package com.trrycaar.friends.data.local.dataSource

import androidx.paging.PagingSource
import com.trrycaar.friends.data.local.dao.PostDao
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.util.safeDataCall

class PostLocalDataSourceImpl(
    private val postDao: PostDao,
) : PostLocalDataSource {
    override suspend fun savePosts(posts: List<PostEntity>) {
        safeDataCall {
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
        safeDataCall {
            postDao.saveToFavorite(id, isFavorite, isSync = isSync)
        }
    }

    override suspend fun getFavoritePostState(id: String): Boolean {
        return safeDataCall {
            postDao.getPostById(id)?.isFavorite ?: false
        }
    }

    override suspend fun syncFavoritePosts() {
        safeDataCall {
            postDao.syncFavoritePosts()
        }
    }
}