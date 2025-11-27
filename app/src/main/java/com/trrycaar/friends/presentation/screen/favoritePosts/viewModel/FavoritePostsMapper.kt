package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel

import com.trrycaar.friends.domain.entity.Post

fun Post.toUiState(): FavoritePostsUiState.PostUiState {
    return FavoritePostsUiState.PostUiState(
        id = this.id,
        title = this.title,
        body = this.body
    )
}