package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel

data class FavoritePostsUiState(
    val state: State = State.LOADING
) {
    data class PostUiState(
        val id: String = "",
        val title: String = "",
        val body: String = ""
    )

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }
}