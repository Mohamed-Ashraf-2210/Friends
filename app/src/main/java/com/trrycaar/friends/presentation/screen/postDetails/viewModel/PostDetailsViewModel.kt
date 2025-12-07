package com.trrycaar.friends.presentation.screen.postDetails.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import com.trrycaar.friends.presentation.navigation.FriendsRoute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PostDetailsViewModel(
    private val postRepository: PostRepository,
    commentRepository: CommentRepository,
    savedStateHandle: SavedStateHandle,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<PostDetailsUiState, PostDetailsEffect>(
    initialState = PostDetailsUiState(),
    defaultDispatcher = defaultDispatcher
) {
    private val postId = savedStateHandle.toRoute<FriendsRoute.PostDetailsScreenRoute>().postId

    val commentsPaging: StateFlow<PagingData<PostDetailsUiState.CommentUiState>> =
        commentRepository.getCommentsPost(postId)
            .map { pagingData ->
                pagingData.map { it.toUiState() }
            }
            .cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = PagingData.empty()
            )

    init {
        loadPost()
    }

    fun onFavoritesClicked() {
        tryToExecute(
            block = {
                postRepository.saveToFavorite(postId, !state.value.isFavorite)
            },
            onSuccess = { updateState { copy(isFavorite = !isFavorite) } }
        )
    }

    fun loadPost() {
        tryToExecute(
            block = { postRepository.getFavoritePostState(postId) },
            onSuccess = {
                updateState { copy(isFavorite = it) }
            }
        )
    }
}