package com.trrycaar.friends.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.trrycaar.friends.data.util.constants.Constants.FAVORITE_POSTS_TABLE_NAME

@Dao
interface FavoritePostsDao {
    @Query(
        """
        INSERT OR REPLACE INTO $FAVORITE_POSTS_TABLE_NAME (id, isFavorite, isSync)
        VALUES (:id, 1, :isSync)
    """
    )
    suspend fun addFavorite(id: String, isSync: Boolean)

    @Query(
        """
        UPDATE $FAVORITE_POSTS_TABLE_NAME 
        SET isFavorite = 0, isSync = :isSync 
        WHERE id = :id
    """
    )
    suspend fun removeFavorite(id: String, isSync: Boolean)

    @Query(
        """
        UPDATE $FAVORITE_POSTS_TABLE_NAME 
        SET isSync = 1 
        WHERE isFavorite = 1
    """
    )
    suspend fun updateSyncFavoritePosts()

    @Query(
        """
        DELETE FROM $FAVORITE_POSTS_TABLE_NAME 
        WHERE isFavorite = 0 AND isSync = 0
    """
    )
    suspend fun deleteUnsyncedUnfavoritePosts()

    @Query("SELECT isFavorite FROM $FAVORITE_POSTS_TABLE_NAME WHERE id = :id")
    suspend fun getFavoritePostState(id: String): Int?
}