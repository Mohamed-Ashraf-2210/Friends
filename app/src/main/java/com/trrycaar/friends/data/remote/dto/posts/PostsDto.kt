package com.trrycaar.friends.data.remote.dto.posts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostsDto(
    @SerialName("data")
    val posts: List<PostDto>
)