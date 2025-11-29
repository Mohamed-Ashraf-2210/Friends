package com.trrycaar.friends.presentation.screen.postDetails

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.trrycaar.friends.R
import com.trrycaar.friends.presentation.composable.LoadingBar
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
    PostDetailsContent(viewModel = viewModel, commentsPaging = commentsPaging)
}

@Composable
private fun PostDetailsContent(
    viewModel: PostDetailsViewModel,
    commentsPaging: LazyPagingItems<PostDetailsUiState.CommentUiState>
) {
    val isRefreshing = commentsPaging.loadState.refresh is LoadState.Loading
    val isError = commentsPaging.loadState.refresh is LoadState.Error
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
                painter = painterResource(id = R.drawable.ic_favorite),
                modifier = Modifier
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        viewModel.addPostToFavorites()
                    },
                contentDescription = null
            )
        }
        AnimatedContent(isRefreshing || isError) {
            when (it) {
                true -> {
                    if (isRefreshing) {
                        LoadingBar(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                false -> {
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
                        if (commentsPaging.loadState.append is LoadState.Loading) {
                            item { LoadingBar(modifier = Modifier.fillMaxSize()) }
                        }
                    }
                }
            }
        }
    }
}