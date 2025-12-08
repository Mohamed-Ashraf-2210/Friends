package com.trrycaar.friends.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.util.constants.Constants.FAVORITE_POSTS_TABLE_NAME
import com.trrycaar.friends.data.util.constants.Constants.POSTS_TABLE_NAME

@Dao
interface PostDao {
    @Upsert
    suspend fun savePosts(posts: List<PostEntity>)

    @Query("SELECT * FROM $POSTS_TABLE_NAME")
    fun getPostsPaginated(): PagingSource<Int, PostEntity>

    @Query(
        """
    SELECT p.* FROM $POSTS_TABLE_NAME p
    INNER JOIN $FAVORITE_POSTS_TABLE_NAME f
    ON p.id = f.id
    WHERE f.isFavorite = 1 AND f.isSync = 1
    """
    )
    fun getFavoritePosts(): PagingSource<Int, PostEntity>

    @Query("DELETE FROM $POSTS_TABLE_NAME")
    suspend fun clearPosts()
}