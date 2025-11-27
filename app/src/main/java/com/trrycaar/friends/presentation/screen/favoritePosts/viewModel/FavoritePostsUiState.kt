package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel

data class FavoritePostsUiState(
    val favoritePosts: List<PostUiState> = emptyList()
) {
    data class PostUiState(
        val id: String,
        val title: String,
        val body: String
    )
}