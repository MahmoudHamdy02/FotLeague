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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.fotleague.screens.auth.login.LoginScreen
import com.example.fotleague.screens.auth.signup.SignupScreen
import com.example.fotleague.screens.leaderboard.LeaderboardScreen
import com.example.fotleague.screens.leagues.LeaguesScreen
import com.example.fotleague.screens.leagues.leaguedetails.LeagueDetailsScreen
import com.example.fotleague.screens.leagues.leaguesettings.LeagueSettingsScreen
import com.example.fotleague.screens.matches.MatchesScreen
import com.example.fotleague.screens.more.MoreScreen
import com.example.fotleague.screens.stats.StatsScreen
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Matches: Screen()

    @Serializable
    object Leagues: Screen()

    @Serializable
    data class LeagueDetails(val leagueId: Int): Screen()

    @Serializable
    data class LeagueSettings(val leagueId: Int, val isLeagueOwner: Boolean): Screen()

    @Serializable
    object Leaderboard: Screen()

    @Serializable
    object Stats: Screen()

    @Serializable
    object More: Screen()

    @Serializable
    object AuthGraph

    @Serializable
    sealed class Auth {

        @Serializable
        object Login: Auth()

        @Serializable
        object Signup: Auth()

        @Serializable
        object ForgotPassword: Auth()
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Matches,
        modifier = Modifier.fillMaxSize(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        // Matches
        composable<Screen.Matches> {
            MatchesScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }

        // Leagues
        composable<Screen.Leagues>(
            exitTransition = {
                if (this.targetState.destination.hasRoute(Screen.LeagueDetails::class)) {
                    fadeOutAnimation() + partialSlideOutAnimation()
                } else {
                    ExitTransition.None
                }
            }) {
            LeaguesScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }
        // League details
        composable<Screen.LeagueDetails>(
            enterTransition = {
                if (this.initialState.destination.hasRoute(Screen.LeagueSettings::class)) {
                    EnterTransition.None
                } else {
                    slideInAnimation()
                }
            },
            exitTransition = {
                if (this.targetState.destination.hasRoute(Screen.LeagueSettings::class)) {
                    fadeOutAnimation() + partialSlideOutAnimation()
                } else {
                    slideOutAnimation()
                }
            }
        ) {
            LeagueDetailsScreen(navController = navController)
        }
        // League settings
        composable<Screen.LeagueSettings>(
            enterTransition = { slideInAnimation() },
            exitTransition = { slideOutAnimation() }
        ) {
            LeagueSettingsScreen(navController = navController)
        }

        // Leaderboard
        composable<Screen.Leaderboard> {
            LeaderboardScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }

        // Stats
        composable<Screen.Stats> {
            StatsScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }

        // More
        composable<Screen.More> {
            MoreScreen(navController = navController, navBackStackEntry = navBackStackEntry)
        }

        // Auth
        navigation<Screen.AuthGraph>(
            startDestination = Screen.Auth.Login
        ) {
            composable<Screen.Auth.Login>(
                enterTransition = {
                    if (this.initialState.destination.hasRoute(Screen.Auth.Signup::class)) {
                        slideIntoContainer(
                            animationSpec = tween(500),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        )
                    } else {
                        EnterTransition.None
                    }
                },
                exitTransition = {
                    if (this.targetState.destination.hasRoute(Screen.Auth.Signup::class)) {
                        slideOutOfContainer(
                            animationSpec = tween(500),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    } else {
                        ExitTransition.None
                    }
                }
            ) {
                LoginScreen(navController = navController)
            }
            composable<Screen.Auth.Signup>(
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(500),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        animationSpec = tween(500),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                SignupScreen(navController = navController)
            }
            composable<Screen.Auth.ForgotPassword> {

            }
        }
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInAnimation(): EnterTransition {
    return slideIntoContainer(
        animationSpec = tween(250, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
    )
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutAnimation(): ExitTransition {
    return slideOutOfContainer(
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