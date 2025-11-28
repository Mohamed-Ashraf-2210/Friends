package com.trrycaar.friends.presentation.screen.favoritePosts

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
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
    val state by viewModel.state.collectAsStateWithLifecycle()
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
    FavoritePostsContent(state = state, viewModel = viewModel)

}

@Composable
fun FavoritePostsContent(state: FavoritePostsUiState, viewModel: FavoritePostsViewModel) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::refreshFavoritePosts,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(state.favoritePosts.size) { index ->
                val post = state.favoritePosts[index]
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