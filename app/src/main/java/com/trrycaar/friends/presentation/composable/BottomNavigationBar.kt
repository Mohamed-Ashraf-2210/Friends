package com.trrycaar.friends.presentation.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val currentDestination = navController
        .currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        bottomTabs.forEach { tab ->
            NavigationBarItem(
                selected = currentDestination == tab.route.value,
                onClick = {
                    navController.navigate(tab.route.value) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    val isSelected = currentDestination == tab.route.value
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