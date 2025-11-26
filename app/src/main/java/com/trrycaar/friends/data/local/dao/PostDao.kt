package com.trrycaar.friends.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.util.constants.Constants.POSTS_TABLE_NAME

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("SELECT * FROM $POSTS_TABLE_NAME")
    suspend fun getPosts(): List<PostEntity>

    @Query("DELETE FROM $POSTS_TABLE_NAME")
    suspend fun clearPosts()
}