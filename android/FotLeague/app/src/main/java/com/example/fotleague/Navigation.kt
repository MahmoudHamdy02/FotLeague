package com.example.fotleague

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fotleague.screens.leaderboard.LeaderboardScreen
import com.example.fotleague.screens.leagues.LeaguesScreen
import com.example.fotleague.screens.matches.MatchesScreen
import com.example.fotleague.screens.more.MoreScreen
import com.example.fotleague.screens.stats.StatsScreen

sealed class Screen(val route: String) {
    data object MatchesScreen : Screen("matches_screen")
    data object LeaguesScreen : Screen("leagues_screen")
    data object LeaderboardScreen : Screen("leaderboard_screen")
    data object StatsScreen : Screen("stats_screen")
    data object MoreScreen : Screen("more_screen")
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.MatchesScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.MatchesScreen.route) {
            MatchesScreen()
        }
        composable(Screen.LeaguesScreen.route) {
            LeaguesScreen()
        }
        composable(Screen.LeaderboardScreen.route) {
            LeaderboardScreen()
        }
        composable(Screen.StatsScreen.route) {
            StatsScreen()
        }
        composable(Screen.MoreScreen.route) {
            MoreScreen()
        }
    }
}