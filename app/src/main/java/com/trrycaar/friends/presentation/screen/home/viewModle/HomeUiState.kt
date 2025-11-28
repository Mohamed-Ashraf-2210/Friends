package com.trrycaar.friends.presentation.screen.home.viewModle

data class HomeUiState(
    val posts: List<PostUiState> = emptyList(),
    val state: State = State.LOADING
) {
    data class PostUiState(
        val id: String = "",
        val title: String = "",
        val body: String = ""
    )
    enum class State {
        LOADING,
        SUCCESS
    }
}