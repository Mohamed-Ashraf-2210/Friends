package com.trrycaar.friends.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
        composable<FriendsRoute.HomeScreenRoute> { }
        composable<FriendsRoute.FavoriteScreenRoute> { }
        composable<FriendsRoute.PostDetailsScreenRoute> { }
    }
}