package com.trrycaar.friends.presentation.screen.home

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.trrycaar.friends.presentation.navigation.FriendsRoute
import com.trrycaar.friends.presentation.composable.PostItem
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
    HomeContent(state = state, viewModel = viewModel)
}

@Composable
private fun HomeContent(state: HomeUiState, viewModel: HomeViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(state.posts.size) { index ->
            val post = state.posts[index]
            PostItem(
                id = post.id,
                title = post.title,
                body = post.body,
                onClick = viewModel::onPostClicked
            )
        }
    }
}