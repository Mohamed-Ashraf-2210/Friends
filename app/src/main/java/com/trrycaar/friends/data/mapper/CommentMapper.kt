package com.trrycaar.friends.data.mapper

import com.trrycaar.friends.data.remote.dto.comments.CommentDto
import com.trrycaar.friends.domain.entity.Comment

fun CommentDto.toDomain() = Comment(
    id = id.toString(),
    postId = postId.toString(),
    name = name,
    email = email,
    body = body
)