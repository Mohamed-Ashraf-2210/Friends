package com.trrycaar.friends.presentation.screen.home.viewModle

import androidx.lifecycle.viewModelScope
import com.trrycaar.friends.core.network.NetworkMonitor
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val postRepository: PostRepository,
    private val networkMonitor: NetworkMonitor,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<HomeUiState, HomeEffects>(
    initialState = HomeUiState(),
    defaultDispatcher = defaultDispatcher
) {
    private var flowConnected: Boolean = networkMonitor.isConnected.value

    init {
        loadPosts()
        checkNetworkConnection()
    }

    private fun loadPosts() {
        tryToExecute(
            block = { postRepository.getPosts() },
            onSuccess = { posts ->
                val postUiStates = posts.map { it.toUiState() }
                updateState { copy(posts = postUiStates) }
            }
        )
    }

    private fun checkNetworkConnection() {
        viewModelScope.launch {
            networkMonitor.isConnected.collect {
                if (flowConnected != it) {
                    flowConnected = it
                    if (it) {
                        emitEffect(HomeEffects.ShowMessage("Online connection"))
                    } else {
                        emitEffect(HomeEffects.ShowMessage("Offline connection"))
                    }
                }
            }
        }
    }

    fun onPostClicked(postId: String) {
        emitEffect(HomeEffects.NavigateToPostDetails(postId))
    }
}