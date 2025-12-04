package com.trrycaar.friends.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trrycaar.friends.data.util.constants.Constants.OFFLINE_FAVORITE_POSTS_TABLE_NAME

@Entity(tableName = OFFLINE_FAVORITE_POSTS_TABLE_NAME)
data class OfflineFavoritePostEntity(
    @PrimaryKey val postId: String = "",
    val isFavorite: Boolean = false
)