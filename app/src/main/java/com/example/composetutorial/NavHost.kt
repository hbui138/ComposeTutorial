package com.example.composetutorial

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composetutorial.ui.Conversation
import com.example.composetutorial.ui.MenuScreen
import com.example.composetutorial.ui.ProfileScreen
import com.example.composetutorial.ui.TestScreen

// Define routes as a sealed class
sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object Test : Screen("test")
    object Profile : Screen("profile")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier) {
    NavHost(navController = navController,
        modifier = modifier,
        startDestination = Screen.Menu.route
    ) {
        composable(Screen.Menu.route) {
            MenuScreen(navController)
        }
        composable(Screen.Test.route) {
            TestScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
    }
}
