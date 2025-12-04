package com.trrycaar.friends.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trrycaar.friends.data.util.constants.Constants.REMOTE_KEYS_TABLE_NAME

@Entity(tableName = REMOTE_KEYS_TABLE_NAME)
data class RemoteKeysEntity(
    @PrimaryKey val postId: String,
    val nextKey: Int?
)