package com.trrycaar.friends.presentation.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.trrycaar.friends.R


data class BottomTab(
    @get:StringRes
    val labelRes: Int,
    @get:DrawableRes
    val iconRes: List<Int>,
)

val bottomTabs = listOf(
    BottomTab(
        labelRes = R.string.home,
        iconRes = listOf(
            R.drawable.ic_home_filled,
            R.drawable.ic_home
        )
    ),
    BottomTab(
        labelRes = R.string.favorite,
        iconRes = listOf(
            R.drawable.ic_favorite_filled,
            R.drawable.ic_favorite
        )
    )
)