package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.dao.PostDao
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.domain.exception.FriendDatabaseException

class PostLocalDataSourceImpl(
    private val postDao: PostDao
): PostLocalDataSource {
    override suspend fun savePosts(posts: List<PostEntity>) {
        try {
            postDao.insertPosts(posts)
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

    override suspend fun getFavoritePosts(): List<PostEntity> {
        return try {
            postDao.getFavoritePosts()
        } catch (_: Exception) {
            throw FriendDatabaseException("Failed to get favorite posts")
        }
    }
}