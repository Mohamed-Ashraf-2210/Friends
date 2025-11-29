package com.trrycaar.friends.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trrycaar.friends.data.local.dao.OfflineFavoritePostDao
import com.trrycaar.friends.data.local.dao.PostDao
import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity
import com.trrycaar.friends.data.local.entity.PostEntity

@Database(
    entities = [PostEntity::class, OfflineFavoritePostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FriendsDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun offlineFavoritePostDao(): OfflineFavoritePostDao
}