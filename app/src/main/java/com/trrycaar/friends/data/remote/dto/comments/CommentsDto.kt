package com.trrycaar.friends.data.remote.dto.comments


import com.trrycaar.friends.data.remote.dto.comments.Pagination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentsDto(
    @SerialName("data")
    val commentDto: List<CommentDto>,
    @SerialName("pagination")
    val pagination: Pagination
)