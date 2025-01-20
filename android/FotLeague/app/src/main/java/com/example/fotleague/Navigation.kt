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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fotleague.screens.auth.login.LoginScreen
import com.example.fotleague.screens.auth.signup.SignupScreen
import com.example.fotleague.screens.leaderboard.LeaderboardScreen
import com.example.fotleague.screens.leagues.LeaguesScreen
import com.example.fotleague.screens.leagues.leaguedetails.LeagueDetailsScreen
import com.example.fotleague.screens.leagues.leaguesettings.LeagueSettingsScreen
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
    data object LeagueSettings : Screen("league_settings")

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
fun Navigation() {
    val navController = rememberNavController()
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
        // Matches
        composable(Screen.MatchesScreen.route) {
            MatchesScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }

        // Leagues
        composable(Screen.LeaguesScreen.route,
            exitTransition = {
                if (this.targetState.destination.route == Screen.LeagueDetails.route + "/{leagueId}") {
                    fadeOutAnimation() + partialSlideOutAnimation()
                } else {
                    ExitTransition.None
                }
            }) {
            LeaguesScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }
        // League details
        composable(
            route = Screen.LeagueDetails.route + "/{leagueId}",
            arguments = listOf(
                navArgument("leagueId") {
                    type = NavType.IntType
                }
            ),
            enterTransition = {
                if (this.initialState.destination.route == Screen.LeagueSettings.route + "/{leagueId},{isLeagueOwner}") {
                    EnterTransition.None
                } else {
                    slideInAnimation(this)
                }
            },
            exitTransition = {
                if (this.targetState.destination.route == Screen.LeagueSettings.route + "/{leagueId},{isLeagueOwner}") {
                    fadeOutAnimation() + partialSlideOutAnimation()
                } else {
                    slideOutAnimation(this)
                }
            }
        ) {
            LeagueDetailsScreen(navController = navController)
        }
        // League settings
        composable(
            route = Screen.LeagueSettings.route + "/{leagueId},{isLeagueOwner}",
            arguments = listOf(
                navArgument("leagueId") {
                    type = NavType.IntType
                },
                navArgument("isLeagueOwner") {
                    type = NavType.BoolType
                }
            ),
            enterTransition = {
                slideInAnimation(this)
            },
            exitTransition = {
                slideOutAnimation(this)
            }
        ) {
            LeagueSettingsScreen(navController = navController)
        }

        // Leaderboard
        composable(Screen.LeaderboardScreen.route) {
            LeaderboardScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }

        // Stats
        composable(Screen.StatsScreen.route) {
            StatsScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }

        // More
        composable(Screen.MoreScreen.route) {
            MoreScreen(navController = navController, navBackStackEntry = navBackStackEntry)
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

private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInAnimation(it: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return it.slideIntoContainer(
        animationSpec = tween(250, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
    )
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutAnimation(it: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return it.slideOutOfContainer(
        animationSpec = tween(250, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.End
    )
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.partialSlideOutAnimation(): ExitTransition {
    return slideOutOfContainer(
        animationSpec = tween(400, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
    ) {
        it / 5
    }
}

private fun fadeOutAnimation(): ExitTransition {
    return fadeOut(
        animationSpec = tween(250, easing = EaseOut),
        0.5f
    )
}