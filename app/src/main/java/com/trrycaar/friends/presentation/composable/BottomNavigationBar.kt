package com.trrycaar.friends.presentation.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.trrycaar.friends.presentation.navigation.FriendsRoute


@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    bottomTabs: List<BottomTab>,
    modifier: Modifier = Modifier
) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar(modifier) {
        bottomTabs.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    navController.navigate(
                        when (index) {
                            0 -> {
                                selectedItemIndex = 0
                                FriendsRoute.HomeScreenRoute
                            }

                            1 -> {
                                selectedItemIndex = 1
                                FriendsRoute.FavoriteScreenRoute
                            }

                            else -> {
                                selectedItemIndex = 0
                                FriendsRoute.HomeScreenRoute
                            }
                        }
                    ) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    val isSelected = selectedItemIndex == index
                    Icon(
                        painter = painterResource(
                            if (isSelected) tab.iconRes.first()
                            else tab.iconRes.last()
                        ),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = tab.labelRes))
                }
            )
        }
    }
}