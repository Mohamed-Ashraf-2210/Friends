package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel

data class FavoritePostsUiState(
    val errorMessage: String = ""
) {
    data class PostUiState(
        val id: String = "",
        val title: String = "",
        val body: String = ""
    )
}