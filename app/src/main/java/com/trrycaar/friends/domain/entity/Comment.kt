package com.trrycaar.friends.domain.entity


data class Comment(
    val id: String,
    val postId: String,
    val name: String,
    val email: String,
    val body: String
)
