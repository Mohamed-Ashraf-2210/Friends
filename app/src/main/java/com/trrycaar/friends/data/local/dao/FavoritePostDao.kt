package com.trrycaar.friends.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.trrycaar.friends.data.local.entity.FavoritePostEntity

@Dao
interface FavoritePostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostToFavorite(favoritePosts: FavoritePostEntity)
}