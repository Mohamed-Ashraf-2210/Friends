package com.trrycaar.friends.presentation.screen.postDetails

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.trrycaar.friends.R
import com.trrycaar.friends.presentation.screen.postDetails.composable.CommentItem
import com.trrycaar.friends.presentation.screen.postDetails.viewModel.PostDetailsEffect
import com.trrycaar.friends.presentation.screen.postDetails.viewModel.PostDetailsUiState
import com.trrycaar.friends.presentation.screen.postDetails.viewModel.PostDetailsViewModel
import com.trrycaar.friends.presentation.screen.util.ObserveAsEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun PostDetailsScreen(
    navController: NavHostController,
    viewModel: PostDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val commentsPaging = viewModel.commentsPaging.collectAsLazyPagingItems()
    val context = LocalContext.current
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            PostDetailsEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is PostDetailsEffect.ShowMessage -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    PostDetailsContent(state = state, viewModel = viewModel, commentsPaging = commentsPaging)
}

@Composable
private fun PostDetailsContent(
    state: PostDetailsUiState,
    viewModel: PostDetailsViewModel,
    commentsPaging: LazyPagingItems<PostDetailsUiState.CommentUiState>
) {
    val pullRefreshState = rememberPullToRefreshState()
    val refreshState = commentsPaging.loadState.refresh
    val appendState = commentsPaging.loadState.append
    var lastError by remember { mutableStateOf<String?>(null) }
    val favoriteIconColor by animateColorAsState(
        targetValue = if (state.isFavorite) {
            Color.Red
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Comments",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_favorite_filled),
                modifier = Modifier
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        viewModel.onFavoritesClicked()
                    },
                tint = favoriteIconColor,
                contentDescription = null
            )
        }
        PullToRefreshBox(
            isRefreshing = refreshState is LoadState.Loading,
            state = pullRefreshState,
            onRefresh = { commentsPaging.refresh() },
        ) {
            if (refreshState is LoadState.Error) {
                val errorMessage = refreshState.error.message ?: "Error"
                if (lastError != errorMessage) {
                    lastError = errorMessage
                    viewModel.showToastErrorMessage(refreshState.error)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(commentsPaging.itemCount) { index ->
                    commentsPaging[index]?.let { comment ->
                        CommentItem(
                            name = comment.name,
                            body = comment.body
                        )
                    }
                }
                if (appendState is LoadState.NotLoading && refreshState is LoadState.NotLoading && commentsPaging.itemCount == 0) {
                    item {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "No Comments in this post Yet!",
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