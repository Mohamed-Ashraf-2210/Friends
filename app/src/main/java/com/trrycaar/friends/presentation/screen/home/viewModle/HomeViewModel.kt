package com.trrycaar.friends.presentation.screen.home.viewModle

import androidx.lifecycle.viewModelScope
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.domain.util.Result
import com.trrycaar.friends.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val postRepository: PostRepository,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<HomeUiState, HomeEffects>(
    initialState = HomeUiState(),
    defaultDispatcher = defaultDispatcher
) {
    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch(defaultDispatcher) {
            val result = postRepository.getPosts()
            withContext(Dispatchers.Main) {

                when (result) {
                    is Result.Success -> {
                        val postUiStates = result.data.map { it.toUiState() }
                        updateState { copy(posts = postUiStates, errorMessage = null) }
                    }

                    is Result.Error -> {
                        val postUiStates = result.cachedData?.map { it.toUiState() } ?: emptyList()
                        updateState { copy(posts = postUiStates, errorMessage = result.message) }
                        emitEffect(HomeEffects.ShowMessage(result.message))
                    }

                    else -> {}
                }
            }
        }
    }

    fun onPostClicked(postId: String) {

    }
}