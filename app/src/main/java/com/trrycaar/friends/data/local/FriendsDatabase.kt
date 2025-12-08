package com.trrycaar.friends.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trrycaar.friends.data.local.dao.FavoritePostsDao
import com.trrycaar.friends.data.local.dao.PostDao
import com.trrycaar.friends.data.local.dao.RemoteKeysDao
import com.trrycaar.friends.data.local.entity.FavoritePostsEntity
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.local.entity.RemoteKeysEntity

@Database(
    entities = [PostEntity::class, FavoritePostsEntity::class, RemoteKeysEntity::class],
    version = 1
)
abstract class FriendsDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun favoritePostsDao(): FavoritePostsDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}