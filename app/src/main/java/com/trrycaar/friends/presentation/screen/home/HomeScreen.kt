package com.trrycaar.friends.presentation.screen.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
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
    val state by viewModel.state.collectAsStateWithLifecycle()
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
    HomeContent(state = state, viewModel = viewModel, postsPaging = postsPaging)
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    viewModel: HomeViewModel,
    postsPaging: LazyPagingItems<HomeUiState.PostUiState>
) {
    AnimatedVisibility(state.state == HomeUiState.State.LOADING) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
            )
        }
    }
    AnimatedVisibility(state.state == HomeUiState.State.SUCCESS) {
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
        }
    }
}