package com.trrycaar.friends.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity
import com.trrycaar.friends.data.util.constants.Constants.OFFLINE_FAVORITE_POSTS_TABLE_NAME

@Dao
interface OfflineFavoritePostDao {
    @Upsert
    suspend fun insertPostToOfflineFavorite(favoritePosts: OfflineFavoritePostEntity)

    @Query("SELECT * FROM $OFFLINE_FAVORITE_POSTS_TABLE_NAME")
    suspend fun getAllFavoritePosts(): List<OfflineFavoritePostEntity>

    @Query("DELETE FROM $OFFLINE_FAVORITE_POSTS_TABLE_NAME WHERE postId = :postId")
    suspend fun deleteFavoritePostsById(postId: String)
}