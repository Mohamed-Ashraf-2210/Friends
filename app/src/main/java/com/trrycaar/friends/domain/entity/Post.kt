package com.trrycaar.friends.domain.entity


data class Post(
    val id: String,
    val title: String,
    val body: String,
    val isFavorite: Boolean
)