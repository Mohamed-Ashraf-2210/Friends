package com.trrycaar.friends.presentation.navigation

fun FriendsRoute.asRoute(): Route {
    return object : Route{
        override val value: String = when (this@asRoute) {
            is FriendsRoute.HomeScreenRoute -> "home"
            is FriendsRoute.FavoriteScreenRoute -> "favorite"
            is FriendsRoute.PostDetailsScreenRoute -> "post/${this@asRoute.postId}"
        }
    }
}