package com.trrycaar.friends.presentation.screen.postDetails.viewModel

data class PostDetailsUiState(
    val errorMessage: String = ""
) {
    data class CommentUiState(
        val id: String = "",
        val name: String = "",
        val email: String = "",
        val body: String = ""
    )
}
