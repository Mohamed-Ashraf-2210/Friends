package com.trrycaar.friends.data.local.dataSource

import androidx.paging.PagingSource
import com.trrycaar.friends.data.local.dao.PostDao
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.domain.exception.FriendDatabaseException

class PostLocalDataSourceImpl(
    private val postDao: PostDao
) : PostLocalDataSource {
    override suspend fun savePosts(posts: List<PostEntity>) {
        try {
            val newPosts = posts.map {
                postDao.getPostById(it.id)?.let { existingPost ->
                    it.copy(isFavorite = existingPost.isFavorite)
                } ?: it
            }
            postDao.savePosts(newPosts)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to save posts")
        }
    }

    override fun getPosts(): PagingSource<Int, PostEntity> {
        return try {
            postDao.getPostsPaginated()
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get posts")
        }
    }

    override fun getFavorites(): PagingSource<Int, PostEntity> {
        return try {
            postDao.getFavoritePosts()
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get favorite posts")
        }
    }

    override suspend fun saveToFavorite(id: String, isFavorite: Boolean) {
        try {
            postDao.saveToFavorite(id, isFavorite)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to save post to favorite")
        }
    }

    override suspend fun getFavoritePostState(id: String): Boolean {
        return try {
            postDao.getPostById(id)?.isFavorite ?: false
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get favorite post state")
        }
    }
}