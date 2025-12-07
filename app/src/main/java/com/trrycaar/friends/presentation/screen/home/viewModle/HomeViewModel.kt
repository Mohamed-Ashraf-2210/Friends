package com.trrycaar.friends.presentation.screen.home.viewModle

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trrycaar.friends.data.util.network.NetworkMonitor
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(
    postRepository: PostRepository,
    private val networkMonitor: NetworkMonitor,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<HomeUiState, HomeEffects>(
    initialState = HomeUiState(),
    defaultDispatcher = defaultDispatcher
) {
    private var flowConnected: Boolean = networkMonitor.isConnected.value

    val postsPaging: Flow<PagingData<HomeUiState.PostUiState>> =
        postRepository.getPostsPaging()
            .map { pagingData ->
                pagingData.map { it.toUiState() }
            }
            .cachedIn(viewModelScope)

    init {
        checkNetworkConnection()
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