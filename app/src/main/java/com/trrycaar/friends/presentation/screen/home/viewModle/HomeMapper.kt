package com.trrycaar.friends.presentation.screen.home.viewModle

import com.trrycaar.friends.domain.entity.Post

fun Post.toUiState(): HomeUiState.PostUiState {
    return HomeUiState.PostUiState(
        id = this.id,
        title = this.title,
        body = this.body
    )
}