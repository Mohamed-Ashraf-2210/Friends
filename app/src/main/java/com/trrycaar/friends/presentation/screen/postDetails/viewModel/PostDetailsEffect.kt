package com.trrycaar.friends.presentation.screen.postDetails.viewModel

sealed class PostDetailsEffect {
    object NavigateBack : PostDetailsEffect()
    data class ShowMessage(val message: String) : PostDetailsEffect()
}