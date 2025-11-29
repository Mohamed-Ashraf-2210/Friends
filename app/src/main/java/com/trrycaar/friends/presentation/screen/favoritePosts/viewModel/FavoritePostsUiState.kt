package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel

data class FavoritePostsUiState(
    val favoritePosts: List<PostUiState> = emptyList(),
    val isRefreshing: Boolean = false
) {
    data class PostUiState(
        val id: String = "",
        val title: String = "",
        val body: String = ""
    )
}