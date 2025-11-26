package com.trrycaar.friends.data.mapper

import com.trrycaar.friends.data.remote.dto.PostDto
import com.trrycaar.friends.domain.entity.Post

fun PostDto.toDomain() = Post(
    id = id.toString(),
    userId = userId.toString(),
    title = title,
    body = body
)