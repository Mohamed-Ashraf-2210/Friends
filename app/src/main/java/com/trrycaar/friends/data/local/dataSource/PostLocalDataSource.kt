package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.dao.PostDao
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.domain.exception.FriendDatabaseException

class PostLocalDataSource(
    private val postDao: PostDao
) {
    suspend fun savePosts(posts: List<PostEntity>) {
        try {
            postDao.insertPosts(posts)
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to save posts")
        }
    }

    suspend fun getPosts(): List<PostEntity> {
        return try {
            postDao.getPosts()
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get posts")
        }
    }

    suspend fun getFavoritePosts(): List<PostEntity> {
        return try {
            postDao.getFavoritePosts()
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get favorite posts")
        }
    }

    suspend fun clearPosts() {
        try {
            postDao.clearPosts()
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to clear posts")
        }
    }
}