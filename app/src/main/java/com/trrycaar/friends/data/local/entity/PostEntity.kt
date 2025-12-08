package com.trrycaar.friends.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trrycaar.friends.data.util.constants.Constants.POSTS_TABLE_NAME


@Entity(tableName = POSTS_TABLE_NAME)
data class PostEntity(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val body: String = ""
)