package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel


sealed class FavoritePostsEffects {
    object NavigateBack : FavoritePostsEffects()
    data class NavigateToPostDetails(val postId: String) : FavoritePostsEffects()
    data class ShowMessage(val message: String) : FavoritePostsEffects()
}