package com.trrycaar.friends.data.mapper

import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.remote.dto.posts.PostDto
import com.trrycaar.friends.domain.entity.Post


fun PostDto.toEntity() = PostEntity(
    id = id.toString(),
    title = title,
    body = body
)

fun PostEntity.toDomain() = Post(
    id = id,
    title = title,
    body = body,
    isFavorite = isFavorite
)