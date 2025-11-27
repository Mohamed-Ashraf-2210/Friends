package com.trrycaar.friends.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.trrycaar.friends.presentation.screen.favoritePosts.FavoritePostsScreen
import com.trrycaar.friends.presentation.screen.home.HomeScreen
import com.trrycaar.friends.presentation.screen.postDetails.PostDetailsScreen

@Composable
fun FriendsNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FriendsRoute.HomeScreenRoute::class,
        modifier = modifier
    ) {
        composable<FriendsRoute.HomeScreenRoute> { HomeScreen(navController) }
        composable<FriendsRoute.FavoriteScreenRoute> { FavoritePostsScreen(navController) }
        composable<FriendsRoute.PostDetailsScreenRoute> { PostDetailsScreen(navController) }
    }
}