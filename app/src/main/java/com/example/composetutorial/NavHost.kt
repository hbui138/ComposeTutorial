package com.example.composetutorial

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composetutorial.ui.Conversation
import com.example.composetutorial.ui.MenuScreen
import com.example.composetutorial.ui.TestScreen

// Define routes as a sealed class
sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object Test : Screen("test")
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Menu.route) {
        composable(Screen.Menu.route) {
            MenuScreen(navController)
        }
        composable(Screen.Test.route) {
            TestScreen(navController)
        }
    }
}
