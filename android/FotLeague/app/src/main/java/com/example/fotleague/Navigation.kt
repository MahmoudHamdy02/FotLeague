package com.example.fotleague

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fotleague.home.HomeScreen
import com.example.fotleague.settings.SettingsScreen

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object SettingsScreen : Screen("settings_screen")
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route, modifier = Modifier.fillMaxSize()) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(Screen.SettingsScreen.route) {
            SettingsScreen()
        }
    }
}