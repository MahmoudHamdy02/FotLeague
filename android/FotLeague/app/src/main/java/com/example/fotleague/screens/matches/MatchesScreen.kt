package com.example.fotleague.screens.matches

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.fotleague.AuthState
import com.example.fotleague.Screen
import com.example.fotleague.models.Match
import com.example.fotleague.models.MatchStatus
import com.example.fotleague.screens.matches.components.GameweeksRow
import com.example.fotleague.screens.matches.components.MatchesList
import com.example.fotleague.screens.matches.components.SubmitPredictionDialog
import com.example.fotleague.ui.components.PrimaryButton
import com.example.fotleague.ui.navigation.BottomNavigation
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme

@Composable
fun MatchesScreen(
    viewModel: MatchesViewModel = hiltViewModel(),
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?
) {

    val state by viewModel.state.collectAsState()
    val authState by viewModel.authState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigation(navController, navBackStackEntry) },
        topBar = {
            TopBar(
                authState.isLoggedIn,
                authState.isLoading
            ) { navController.navigate(Screen.AuthGraph) }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            MatchesContent(
                state = state,
                authState = authState,
                onEvent = { viewModel.onEvent(it) },
                onNavigateToAuth = { navController.navigate(Screen.AuthGraph) }
            )
        }
    }
}

@Composable
private fun MatchesContent(
    state: MatchesState,
    authState: AuthState,
    onEvent: (event: MatchesEvent) -> Unit,
    onNavigateToAuth: () -> Unit
) {
    val pagerState = rememberPagerState {
        38
    }
    LaunchedEffect(state.isLoading) {
        if (!state.isLoading && state.error == null) {
            val gameweek =
                state.matches.filter { it.matchStatus == MatchStatus.Played.num || it.matchStatus == MatchStatus.InProgress.num }
                    .maxByOrNull { it.datetime }!!.gameweek
            pagerState.animateScrollToPage(gameweek - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        GameweeksRow(
            pagerState = pagerState
        )
        if (state.error != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = state.error)
            }
        } else if (state.isLoading || authState.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                )
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                MatchesList(
                    index = index,
                    matches = state.matches,
                    predictions = state.predictions,
                    scores = state.scores,
                    isLoggedIn = authState.isLoggedIn,
                    onOpenPredictionDialog = { onEvent(MatchesEvent.OpenDialog) },
                    onSelectMatch = { onEvent(MatchesEvent.SelectMatch(it)) },
                    onNavigateToAuth = onNavigateToAuth
                )
            }
        }
    }

    if (state.predictionDialogOpen) {
        SubmitPredictionDialog(
            homeTeam = state.selectedMatch.home,
            awayTeam = state.selectedMatch.away,
            homePickerState = state.homePickerState,
            awayPickerState = state.awayPickerState,
            isLoggedIn = authState.isLoggedIn,
            onSubmit = {
                if (state.predictions.any { it.matchId == state.selectedMatch.id }) {
                    onEvent(MatchesEvent.UpdatePrediction)
                } else {
                    onEvent(MatchesEvent.SubmitPrediction)
                }
            },
            onDismiss = { onEvent(MatchesEvent.CloseDialog) },
            onNavigateToAuth = onNavigateToAuth,
            edit = state.predictions.any { it.matchId == state.selectedMatch.id }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(isLoggedIn: Boolean, isLoading: Boolean, onNavigate: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGray),
        title = { Text(text = "FotLeague", fontWeight = FontWeight.Bold) },
        actions = {
            if (!isLoggedIn && !isLoading) {
                PrimaryButton(
                    onClick = onNavigate,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(72.dp)
                        .height(36.dp)
                ) {
                    Text("Log in", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.width(8.dp))
            }
        }
    )
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MatchesContentPreview() {
    FotLeagueTheme {
        MatchesContent(
            state = MatchesState(
                matches = listOf(
                    Match(
                        id = 1,
                        home = "Liverpool",
                        away = "",
                        homeScore = 0,
                        awayScore = 0,
                        matchStatus = 1,
                        datetime = "2024-08-16T19:00:00.000Z",
                        season = 0,
                        gameweek = 1
                    )
                )
            ),
            authState = AuthState(isLoading = false),
            onEvent = {},
            onNavigateToAuth = {}
        )
    }
}
