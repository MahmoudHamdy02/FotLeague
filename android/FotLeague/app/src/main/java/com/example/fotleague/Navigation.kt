package com.example.fotleague

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.fotleague.screens.auth.login.LoginScreen
import com.example.fotleague.screens.auth.signup.SignupScreen
import com.example.fotleague.screens.leaderboard.LeaderboardScreen
import com.example.fotleague.screens.leagues.LeaguesScreen
import com.example.fotleague.screens.leagues.leaguedetails.LeagueDetailsScreen
import com.example.fotleague.screens.matches.MatchesScreen
import com.example.fotleague.screens.more.MoreScreen
import com.example.fotleague.screens.stats.StatsScreen

sealed class Screen(val route: String) {
    data object MatchesScreen : Screen("matches_screen")
    data object LeaguesScreen : Screen("leagues_screen")
    data object LeaderboardScreen : Screen("leaderboard_screen")
    data object StatsScreen : Screen("stats_screen")
    data object MoreScreen : Screen("more_screen")
    data object LeagueDetails : Screen("league_details")

    sealed class Auth(val route: String) {
        data object LoginScreen : Auth("login_screen")
        data object SignupScreen : Auth("signup_screen")
        data object ForgotPasswordScreen : Auth("forgot_password_screen")
    }
}

sealed class Route(val route: String) {
    data object Auth : Route("auth")
}

@Composable
fun Navigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.MatchesScreen.route,
        modifier = Modifier.fillMaxSize(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        // Bottom navigation
        composable(Screen.MatchesScreen.route) {
            MatchesScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }
        composable(Screen.LeaguesScreen.route,
            exitTransition = {
                if (this.targetState.destination.route == Screen.LeagueDetails.route + "/{leagueId}") {
                    fadeOut(animationSpec = tween(250, easing = EaseOut), 0.5f) + slideOutOfContainer(
                        animationSpec = tween(400, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    ) {
                        it/5
                    }
                } else {
                    ExitTransition.None
                }
            }) {
            LeaguesScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }
        composable(Screen.LeaderboardScreen.route) {
            LeaderboardScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }
        composable(Screen.StatsScreen.route) {
            StatsScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }
        composable(Screen.MoreScreen.route) {
            MoreScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }

        // Nested navigation
        composable(
            route = Screen.LeagueDetails.route + "/{leagueId}",
            arguments = listOf(
                navArgument("leagueId") {
                    type = NavType.IntType
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(250, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(250, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            LeagueDetailsScreen(navController = navController)
        }

        // Auth
        navigation(
            startDestination = Screen.Auth.LoginScreen.route,
            route = Route.Auth.route
        ) {
            composable(Screen.Auth.LoginScreen.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.Auth.SignupScreen.route) {
                SignupScreen(navController = navController)
            }
            composable(Screen.Auth.ForgotPasswordScreen.route) {

            }
        }
    }
}