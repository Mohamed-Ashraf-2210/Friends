package com.trrycaar.friends.presentation.screen.postDetails.viewModel

import com.trrycaar.friends.domain.entity.Comment

fun Comment.toUiState(): PostDetailsUiState.CommentUiState {
    return PostDetailsUiState.CommentUiState(
        id = id,
        name = name,
        email = email,
        body = body
    )
}