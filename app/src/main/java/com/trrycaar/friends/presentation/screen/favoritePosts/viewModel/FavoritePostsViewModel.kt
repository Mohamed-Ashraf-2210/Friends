package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritePostsViewModel(
    private val postRepository: PostRepository,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<FavoritePostsUiState, FavoritePostsEffects>(
    initialState = FavoritePostsUiState(),
    defaultDispatcher = defaultDispatcher
) {
    private val _favoritePostsPaging =
        MutableStateFlow<PagingData<FavoritePostsUiState.PostUiState>>(PagingData.empty())
    val favoritePostsPaging: StateFlow<PagingData<FavoritePostsUiState.PostUiState>> = _favoritePostsPaging

    init {
        loadFavoritePosts()
    }

    private fun loadFavoritePosts() {
        tryToCollect(
            onStart = ::onLoadFavoritePostsStart,
            block = { postRepository.getFavoritePostsPaging().cachedIn(viewModelScope) },
            onCollect = ::onLoadFavoritePostsSuccess,
            onError = ::onLoadFavoritePostsError
        )
    }

    private fun onLoadFavoritePostsStart() {
        updateState { copy(state = FavoritePostsUiState.State.LOADING) }
    }

    private fun onLoadFavoritePostsSuccess(pagingData: PagingData<Post>) {
        _favoritePostsPaging.value = pagingData.map { it.toUiState() }
        updateState { copy(state = FavoritePostsUiState.State.SUCCESS) }
    }


    private fun onLoadFavoritePostsError(error: Throwable) {
        emitEffect(FavoritePostsEffects.ShowMessage("Failed to load favorite posts: ${error.message}"))
        updateState { copy(state = FavoritePostsUiState.State.ERROR) }
    }

    fun onPostClicked(postId: String) {
        emitEffect(FavoritePostsEffects.NavigateToPostDetails(postId))
    }
}