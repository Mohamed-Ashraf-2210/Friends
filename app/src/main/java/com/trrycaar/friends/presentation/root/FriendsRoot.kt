package com.trrycaar.friends.presentation.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.trrycaar.friends.presentation.composable.BottomNavigationBar
import com.trrycaar.friends.presentation.composable.TopAppBar
import com.trrycaar.friends.presentation.composable.bottomTabs
import com.trrycaar.friends.presentation.navigation.FriendsNavGraph

@Composable
fun FriendsRoot() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar()
        },
        bottomBar = {
            BottomNavigationBar(navController, bottomTabs)
        }
    ) { padding ->
        FriendsNavGraph(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}