package com.trrycaar.friends.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.trrycaar.friends.data.local.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao {
    @Upsert
    suspend fun insertAll(remoteKey: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_keys WHERE postId = :postId")
    suspend fun remoteKeysPostId(postId: String): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}