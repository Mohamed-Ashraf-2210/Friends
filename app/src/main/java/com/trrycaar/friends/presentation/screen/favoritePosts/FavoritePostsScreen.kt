package com.trrycaar.friends.presentation.screen.favoritePosts

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
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
import com.trrycaar.friends.presentation.navigation.FriendsRoute.PostDetailsScreenRoute
import com.trrycaar.friends.presentation.screen.favoritePosts.viewModel.FavoritePostsEffects
import com.trrycaar.friends.presentation.screen.favoritePosts.viewModel.FavoritePostsUiState
import com.trrycaar.friends.presentation.screen.favoritePosts.viewModel.FavoritePostsViewModel
import com.trrycaar.friends.presentation.screen.util.ObserveAsEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritePostsScreen(
    navController: NavHostController,
    viewModel: FavoritePostsViewModel = koinViewModel()
) {
    val favoritePostsPaging = viewModel.favoritePostsPaging.collectAsLazyPagingItems()

    val context = LocalContext.current
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            FavoritePostsEffects.NavigateBack -> {
                navController.popBackStack()
            }

            is FavoritePostsEffects.NavigateToPostDetails -> {
                navController.navigate(PostDetailsScreenRoute(postId = it.postId))
            }

            is FavoritePostsEffects.ShowMessage -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    FavoritePostsContent(viewModel = viewModel, favoritePostsPaging = favoritePostsPaging)

}

@Composable
fun FavoritePostsContent(
    viewModel: FavoritePostsViewModel,
    favoritePostsPaging: LazyPagingItems<FavoritePostsUiState.PostUiState>
) {
    val refreshState = favoritePostsPaging.loadState.refresh
    val appendState = favoritePostsPaging.loadState.append
    val pullRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = refreshState is LoadState.Loading,
        state = pullRefreshState,
        onRefresh = { favoritePostsPaging.refresh() },
    ) {
        when {
            refreshState is LoadState.Error -> {
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
                        count = favoritePostsPaging.itemCount,
                        key = { index ->
                            favoritePostsPaging[index]?.id ?: index
                        }) { index ->
                        favoritePostsPaging[index]?.let { post ->
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
                    if (appendState is LoadState.NotLoading && refreshState is LoadState.NotLoading && favoritePostsPaging.itemCount == 0) {
                        item {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "No Posts in favorite Yet!",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}