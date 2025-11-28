package com.trrycaar.friends.presentation.screen.postDetails.viewModel

data class PostDetailsUiState(
    val state: State = State.LOADING
) {
    data class CommentUiState(
        val id: String,
        val name: String,
        val email: String,
        val body: String
    )

    enum class State {
        LOADING,
        SUCCESS,
        ERROR,
    }
}
