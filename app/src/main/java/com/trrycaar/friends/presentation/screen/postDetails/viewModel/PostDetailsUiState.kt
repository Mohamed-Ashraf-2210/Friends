package com.trrycaar.friends.presentation.screen.postDetails.viewModel

data class PostDetailsUiState(
    val comment: List<CommentUiState> = emptyList(),
    val isFavorite: Boolean = false
) {
    data class CommentUiState(
        val id: String,
        val name: String,
        val email: String,
        val body: String
    )
}
