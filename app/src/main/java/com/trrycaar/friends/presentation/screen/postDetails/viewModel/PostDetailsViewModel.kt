package com.trrycaar.friends.presentation.screen.postDetails.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trrycaar.friends.core.network.NetworkMonitor
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.domain.repository.FavoritePostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import com.trrycaar.friends.presentation.navigation.FriendsRoute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
    private val _commentsPaging =
        MutableStateFlow<PagingData<PostDetailsUiState.CommentUiState>>(PagingData.empty())
    val commentsPaging: StateFlow<PagingData<PostDetailsUiState.CommentUiState>> = _commentsPaging

    init {
        loadComments()
    }

    private fun loadComments() {
        tryToCollect(
            onStart = { updateState { copy(state = PostDetailsUiState.State.LOADING) } },
            block = { commentRepository.getCommentsPost(postId).cachedIn(viewModelScope) },
            onCollect = { pagingData ->
                _commentsPaging.value = pagingData.map { it.toUiState() }
                updateState { copy(state = PostDetailsUiState.State.SUCCESS) }
            },
            onError = {
                updateState { copy(state = PostDetailsUiState.State.ERROR) }
                emitEffect(PostDetailsEffect.ShowMessage("Failed to load comments"))
            }
        )
    }

    fun addPostToFavorites() {
        tryToExecute(
            block = {
                if (networkMonitor.isConnected.value)
                    favoritePostRepository.addPostToFavorite(postId)
                else
                    favoritePostRepository.addPostToOfflineFavorite(postId)
            }
        )
        emitEffect(PostDetailsEffect.ShowMessage("Post added to favorites"))
    }

}