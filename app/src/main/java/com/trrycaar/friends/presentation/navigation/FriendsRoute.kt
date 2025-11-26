package com.trrycaar.friends.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface FriendsRoute {
    @Serializable
    object HomeScreenRoute : FriendsRoute

    @Serializable
    object FavoriteScreenRoute : FriendsRoute

    @Serializable
    data class PostDetailsScreenRoute(val postId: String) : FriendsRoute
}