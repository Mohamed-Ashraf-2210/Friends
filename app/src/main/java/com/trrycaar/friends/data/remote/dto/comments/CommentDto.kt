package com.trrycaar.friends.data.remote.dto.comments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    @SerialName("body")
    val body: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("postId")
    val postId: Int,
    @SerialName("updatedAt")
    val updatedAt: String
)