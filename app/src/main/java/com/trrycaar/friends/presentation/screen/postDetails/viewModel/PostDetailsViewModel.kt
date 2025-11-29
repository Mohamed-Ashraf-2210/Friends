package com.trrycaar.friends.presentation.screen.postDetails.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trrycaar.friends.core.network.NetworkMonitor
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.domain.repository.OfflineFavoritePostRepository
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
    private val favoritePostRepository: OfflineFavoritePostRepository,
    private val networkMonitor: NetworkMonitor,
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
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { it.toUiState() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = PagingData.empty()
            )

    fun addPostToFavorites() {
        tryToExecute(
            block = {
                if (networkMonitor.isConnected.value)
                    postRepository.addToFavorite(postId)
                else
                    favoritePostRepository.addPostToOfflineFavorite(postId)
            }
        )
        emitEffect(PostDetailsEffect.ShowMessage("Post added to favorites"))
    }
}