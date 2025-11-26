package com.trrycaar.friends.presentation.screen.home.viewModle

sealed class HomeEffects {
    object NavigateBack : HomeEffects()
    data class NavigateToPostDetails(val postId: String) : HomeEffects()

    data class ShowMessage(val message: String) : HomeEffects()
}