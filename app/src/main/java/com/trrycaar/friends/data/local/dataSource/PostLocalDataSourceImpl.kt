package com.trrycaar.friends.data.local.dataSource

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
            postDao.insertPosts(newPosts)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to save posts")
        }
    }

    override suspend fun getPosts(page: Int, pageSize: Int): List<PostEntity> {
        return try {
            postDao.getPostsPaginated(page, pageSize)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get posts")
        }
    }

    override suspend fun getFavorites(page: Int, pageSize: Int): List<PostEntity> {
        return try {
            postDao.getFavoritePosts(page, pageSize)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get favorite posts")
        }
    }

    override suspend fun saveToFavorite(id: String) {
        try {
            postDao.saveToFavorite(id)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to add post to favorite")
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