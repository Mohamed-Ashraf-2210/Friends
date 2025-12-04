package com.trrycaar.friends.presentation.screen.favoritePosts

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
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
    val isRefreshing = favoritePostsPaging.loadState.refresh is LoadState.Loading
    val pullRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        state = pullRefreshState,
        onRefresh = { favoritePostsPaging.refresh() },
    ) {
        when {
            isRefreshing -> {
                LoadingBar(Modifier.fillMaxSize())
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
                    if (favoritePostsPaging.loadState.append is LoadState.Loading) {
                        item { LoadingBar(modifier = Modifier.fillMaxSize()) }
                    }
                }
            }
        }
    }
}