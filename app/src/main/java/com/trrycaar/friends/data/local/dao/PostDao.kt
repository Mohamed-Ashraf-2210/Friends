package com.trrycaar.friends.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.util.constants.Constants.POSTS_TABLE_NAME

@Dao
interface PostDao {
    @Upsert
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("SELECT * FROM $POSTS_TABLE_NAME LIMIT :pageSize OFFSET (:page - 1) * :pageSize")
    suspend fun getPostsPaginated(page: Int, pageSize: Int): List<PostEntity>

    @Query("SELECT * FROM $POSTS_TABLE_NAME WHERE isFavorite = 1 LIMIT :pageSize OFFSET (:page - 1) * :pageSize")
    suspend fun getFavoritePosts(page: Int, pageSize: Int): List<PostEntity>

    @Query("SELECT * FROM $POSTS_TABLE_NAME WHERE id = :id")
    suspend fun getPostById(id: String): PostEntity?

    @Query("UPDATE $POSTS_TABLE_NAME SET isFavorite = 1 WHERE id = :id")
    suspend fun saveToFavorite(id: String)

    @Query("DELETE FROM $POSTS_TABLE_NAME")
    suspend fun clearAllPosts()
}