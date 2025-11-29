package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel

import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.repository.FavoritePostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FavoritePostsViewModel(
    private val favoritePostRepository: FavoritePostRepository,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<FavoritePostsUiState, FavoritePostsEffects>(
    initialState = FavoritePostsUiState(),
    defaultDispatcher = defaultDispatcher
) {

    init {
        loadFavoritePosts()
    }

    private fun loadFavoritePosts() {
        tryToExecute(
            block = { favoritePostRepository.getFavoritePosts() },
            onSuccess = ::onLoadFavoritePostsSuccess,
            onError = ::onLoadFavoritePostsError
        )
    }

    private fun onLoadFavoritePostsSuccess(posts: List<Post>) {
        val postUiStates = posts.map { it.toUiState() }
        updateState { copy(favoritePosts = postUiStates) }
    }

    private fun onLoadFavoritePostsError(error: Throwable) {
        emitEffect(FavoritePostsEffects.ShowMessage("Failed to load favorite posts: ${error.message}"))
    }

    fun onPostClicked(postId: String) {
        emitEffect(FavoritePostsEffects.NavigateToPostDetails(postId))
    }

    fun refreshFavoritePosts() {
        updateState { copy(isRefreshing = true) }
        loadFavoritePosts()
        updateState { copy(isRefreshing = false) }
    }
}