package com.trrycaar.friends.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    @SerialName("id")
    val id: Int,
    @SerialName("postId")
    val postId: Int,
    @SerialName("body")
    val body: String,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String
)