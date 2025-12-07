package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritePostsViewModel(
    postRepository: PostRepository,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<FavoritePostsUiState, FavoritePostsEffects>(
    initialState = FavoritePostsUiState(),
    defaultDispatcher = defaultDispatcher
) {
    val favoritePostsPaging: Flow<PagingData<FavoritePostsUiState.PostUiState>> =
        postRepository.getFavoritePostsPaging()
            .map { pagingData ->
                pagingData.map { it.toUiState() }
            }
            .cachedIn(viewModelScope)

    fun onPostClicked(postId: String) {
        emitEffect(FavoritePostsEffects.NavigateToPostDetails(postId))
    }
}