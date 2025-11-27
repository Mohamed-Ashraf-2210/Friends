package com.trrycaar.friends.presentation.screen.home.viewModle

data class HomeUiState(
    val posts: List<PostUiState> = emptyList()
) {
    data class PostUiState(
        val id: String,
        val title: String,
        val body: String
    )
}