package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.dao.PostDao
import com.trrycaar.friends.data.local.entity.PostEntity

class PostLocalDataSource(
    private val postDao: PostDao
) {
    suspend fun savePosts(posts: List<PostEntity>) = postDao.insertPosts(posts)

    suspend fun getPosts() = postDao.getPosts()

    suspend fun clearPosts() = postDao.clearPosts()
}