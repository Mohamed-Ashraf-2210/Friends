package com.trrycaar.friends.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trrycaar.friends.data.util.constants.Constants.FAVORITE_POSTS_TABLE_NAME

@Entity(tableName = FAVORITE_POSTS_TABLE_NAME)
data class FavoritePostEntity(
    @PrimaryKey val postId: String
)