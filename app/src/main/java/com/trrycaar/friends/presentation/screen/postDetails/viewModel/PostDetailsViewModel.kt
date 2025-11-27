package com.trrycaar.friends.presentation.screen.postDetails.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.trrycaar.friends.NetworkMonitor
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.domain.repository.FavoritePostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import com.trrycaar.friends.presentation.navigation.FriendsRoute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PostDetailsViewModel(
    private val commentRepository: CommentRepository,
    private val favoritePostRepository: FavoritePostRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<PostDetailsUiState, PostDetailsEffect>(
    initialState = PostDetailsUiState(),
    defaultDispatcher = defaultDispatcher
) {
    private val postId = savedStateHandle.toRoute<FriendsRoute.PostDetailsScreenRoute>().postId

    init {
        loadComments()
    }

    private fun loadComments() {
        tryToExecute(
            block = {
                commentRepository.getCommentsPost(postId)
            },
            onSuccess = {
                val commentUiStates = it.map { comment -> comment.toUiState() }
                updateState { copy(comment = commentUiStates) }
            }
        )
    }

    fun addPostToFavorites() {
        emitEffect(PostDetailsEffect.ShowMessage("Post added to favorites"))
        tryToExecute(
            block = {
                if (networkMonitor.isOnline())
                    favoritePostRepository.addPostToFavorite(postId)
                else
                    favoritePostRepository.addPostToOfflineFavorite(postId)
            }
        )
    }

}