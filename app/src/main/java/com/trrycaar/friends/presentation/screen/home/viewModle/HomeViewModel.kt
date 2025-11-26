package com.trrycaar.friends.presentation.screen.home.viewModle

import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

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
        tryToExecute(
            block = {
                postRepository.getPosts()
            },
            onSuccess = { posts ->
                val postUiStates = posts.map { it.toUiState() }
                updateState {
                    copy(posts = postUiStates)
                }
            },
            onError = { exception ->

            }
        )
    }

    fun onPostClicked(postId: String) {

    }
}