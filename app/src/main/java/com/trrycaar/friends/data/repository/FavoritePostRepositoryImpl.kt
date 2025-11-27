package com.trrycaar.friends.data.repository

import com.trrycaar.friends.data.local.dataSource.FavoritePostsLocalDataSource
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.local.entity.FavoritePostEntity
import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.exception.FriendDatabaseException
import com.trrycaar.friends.domain.repository.FavoritePostRepository

class FavoritePostRepositoryImpl(
    private val favoritePostsLocal: FavoritePostsLocalDataSource,
    private val postLocal: PostLocalDataSource
) : FavoritePostRepository {
    override suspend fun addPostToFavorite(postId: String) {
        try {
            favoritePostsLocal.savePostToFavorite(FavoritePostEntity(postId))
        } catch (e: FriendDatabaseException) {
            throw e
        }
    }

    override suspend fun getFavoritePosts(): List<Post> {
        return try {
            postLocal.getFavoritePosts().map { it.toDomain() }
        } catch (e: FriendDatabaseException) {
            throw e
        }
    }
}