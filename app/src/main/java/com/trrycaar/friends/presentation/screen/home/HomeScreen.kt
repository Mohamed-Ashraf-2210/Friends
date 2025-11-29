package com.trrycaar.friends.presentation.screen.home

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
    val isRefreshing = postsPaging.loadState.refresh is LoadState.Loading
    val isError = postsPaging.loadState.refresh is LoadState.Error
    AnimatedContent(isRefreshing || isError) {
        when (it) {
            true -> {
                if (isRefreshing) {
                    LoadingBar(
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    val errorState = postsPaging.loadState.refresh as? LoadState.Error
                    val errorMessage = errorState?.error?.localizedMessage ?: "Unknown Error"
                    viewModel.showMessage("Error: $errorMessage")
                }

            }

            false -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(postsPaging.itemCount) { index ->
                        postsPaging[index]?.let { post ->
                            PostItem(
                                id = post.id,
                                title = post.title,
                                body = post.body,
                                onClick = viewModel::onPostClicked
                            )
                        }
                    }
                    if (postsPaging.loadState.append is LoadState.Loading) {
                        item { LoadingBar(modifier = Modifier.fillMaxSize()) }
                    }
                }
            }
        }
    }
}