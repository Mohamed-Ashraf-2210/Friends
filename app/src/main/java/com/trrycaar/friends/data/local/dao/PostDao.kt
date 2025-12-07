package com.trrycaar.friends.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.util.constants.Constants.POSTS_TABLE_NAME

@Dao
interface PostDao {
    @Upsert
    suspend fun savePosts(posts: List<PostEntity>)

    @Query("SELECT * FROM $POSTS_TABLE_NAME")
    fun getPostsPaginated(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM $POSTS_TABLE_NAME WHERE isFavorite = 1 AND isSync = 1")
    fun getFavoritePosts(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM $POSTS_TABLE_NAME WHERE id = :id")
    suspend fun getPostById(id: String): PostEntity?

    @Query("UPDATE $POSTS_TABLE_NAME SET isFavorite = :isFavorite, isSync = :isSync WHERE id = :id")
    suspend fun saveToFavorite(id: String, isFavorite: Boolean, isSync: Boolean)

    @Query("UPDATE $POSTS_TABLE_NAME SET isSync = 1 WHERE isFavorite = 1")
    suspend fun syncFavoritePosts()

    @Query("DELETE FROM $POSTS_TABLE_NAME")
    suspend fun clearAllPosts()
}