package com.trrycaar.friends.presentation.screen.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.trrycaar.friends.presentation.composable.LoadingBar
import com.trrycaar.friends.presentation.composable.PostItem
import com.trrycaar.friends.presentation.navigation.FriendsRoute
import com.trrycaar.friends.presentation.screen.home.viewModle.HomeEffects
import com.trrycaar.friends.presentation.screen.home.viewModle.HomeUiState
import com.trrycaar.friends.presentation.screen.home.viewModle.HomeViewModel
import com.trrycaar.friends.presentation.screen.util.ObserveAsEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val postsPaging = viewModel.postsPaging.collectAsLazyPagingItems()
    val context = LocalContext.current
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            HomeEffects.NavigateBack -> {
                navController.popBackStack()
            }

            is HomeEffects.NavigateToPostDetails -> {
                navController.navigate(FriendsRoute.PostDetailsScreenRoute(postId = it.postId))
            }

            is HomeEffects.ShowMessage -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    HomeContent(viewModel = viewModel, postsPaging = postsPaging)
}

@Composable
private fun HomeContent(
    viewModel: HomeViewModel,
    postsPaging: LazyPagingItems<HomeUiState.PostUiState>
) {

    val refreshState = postsPaging.loadState.refresh
    val appendState = postsPaging.loadState.append
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = refreshState is LoadState.Loading,
        state = pullRefreshState,
        onRefresh = { postsPaging.refresh() },
    ) {
        when {
            refreshState is LoadState.Loading -> {
                LoadingBar(Modifier.fillMaxSize())
            }

            refreshState is LoadState.Error && viewModel.flowConnected -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Error!"
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        count = postsPaging.itemCount,
                        key = { index ->
                            postsPaging[index]?.id ?: index
                        }) { index ->
                        postsPaging[index]?.let { post ->
                            PostItem(
                                id = post.id,
                                title = post.title,
                                body = post.body,
                                onClick = viewModel::onPostClicked
                            )
                        }
                    }

                    if (appendState is LoadState.Loading) {
                        item {
                            LoadingBar(Modifier.fillMaxWidth())
                        }
                    }

                    if (appendState is LoadState.Error) {
                        item {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Error!"
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}