package com.trrycaar.friends.presentation.screen.home.viewModle

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trrycaar.friends.core.network.NetworkMonitor
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _postsPaging =
        MutableStateFlow<PagingData<HomeUiState.PostUiState>>(PagingData.empty())
    val postsPaging: StateFlow<PagingData<HomeUiState.PostUiState>> = _postsPaging

    init {
        loadPosts()
        checkNetworkConnection()
    }

    private fun loadPosts() {
        tryToCollect(
            onStart = { updateState { copy(state = HomeUiState.State.LOADING) } },
            block = { postRepository.getPostsPaging().cachedIn(viewModelScope) },
            onCollect = { pagingData ->
                _postsPaging.value = pagingData.map { it.toUiState() }
                updateState { copy(state = HomeUiState.State.SUCCESS) }
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