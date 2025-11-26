package com.trrycaar.friends.presentation.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.trrycaar.friends.R
import com.trrycaar.friends.presentation.navigation.FriendsRoute
import com.trrycaar.friends.presentation.navigation.Route
import com.trrycaar.friends.presentation.navigation.asRoute


data class BottomTab(
    val route: Route,
    @get:StringRes
    val labelRes: Int,
    @get:DrawableRes
    val iconRes: List<Int>,
)

val bottomTabs = listOf(
    BottomTab(
        route = FriendsRoute.HomeScreenRoute.asRoute(),
        labelRes = R.string.home,
        iconRes = listOf(
            R.drawable.ic_home_filled,
            R.drawable.ic_home
        )
    ),
    BottomTab(
        route = FriendsRoute.FavoriteScreenRoute.asRoute(),
        labelRes = R.string.favorite,
        iconRes = listOf(
            R.drawable.ic_favorite_filled,
            R.drawable.ic_favorite
        )
    )
)