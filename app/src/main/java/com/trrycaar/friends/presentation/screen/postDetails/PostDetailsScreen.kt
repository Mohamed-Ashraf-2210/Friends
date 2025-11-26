package com.trrycaar.friends.presentation.screen.postDetails

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
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
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            PostDetailsEffect.NavigateBack -> {
                navController.popBackStack()
            }
        }
    }
    PostDetailsContent(state = state, viewModel = viewModel)
}

@Composable
private fun PostDetailsContent(state: PostDetailsUiState, viewModel: PostDetailsViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Comments",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding( top = 16.dp, bottom = 8.dp).align(Alignment.CenterHorizontally)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(state.comment.size) { index ->
                val post = state.comment[index]
                CommentItem(
                    name = post.name,
                    body = post.body
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Add to Favorites",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_favorite_filled),
                contentDescription = null,
            )
        }

    }
}