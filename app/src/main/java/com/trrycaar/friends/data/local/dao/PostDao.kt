package com.trrycaar.friends.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.util.constants.Constants.FAVORITE_POSTS_TABLE_NAME
import com.trrycaar.friends.data.util.constants.Constants.POSTS_TABLE_NAME

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("SELECT * FROM $POSTS_TABLE_NAME LIMIT :pageSize OFFSET (:page - 1) * :pageSize")
    suspend fun getPostsPaginated(page: Int, pageSize: Int): List<PostEntity>

    @Query(
        """
        SELECT p.*
        FROM $POSTS_TABLE_NAME p
        INNER JOIN $FAVORITE_POSTS_TABLE_NAME f
        ON p.id = f.postId
    """
    )
    suspend fun getFavoritePosts(): List<PostEntity>

    @Query("DELETE FROM $POSTS_TABLE_NAME")
    suspend fun clearPosts()
}