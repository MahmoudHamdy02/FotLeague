package com.example.fotleague.screens.matches

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.fotleague.LocalAuthUser
import com.example.fotleague.LocalNavController
import com.example.fotleague.LocalTopBar
import com.example.fotleague.MainState
import com.example.fotleague.models.Match
import com.example.fotleague.screens.matches.components.GameweeksRow
import com.example.fotleague.screens.matches.components.MatchesList
import com.example.fotleague.screens.matches.components.SubmitPredictionDialog
import com.example.fotleague.ui.navigation.AppBarState
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.FotLeagueTheme

@Composable
fun MatchesScreen(
    viewModel: MatchesViewModel = hiltViewModel()
) {
    LocalTopBar.current(AppBarState(title = "FotLeague", titleFontWeight = FontWeight.Bold))

    val state by viewModel.state.collectAsState()

    MatchesContent(
        state = state,
        onEvent = { viewModel.onEvent(it) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MatchesContent(
    state: MatchesState,
    onEvent: (event: MatchesEvent) -> Unit
) {
    val pagerState = rememberPagerState {
        38
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
        } else if (state.isLoading || LocalAuthUser.current.isLoading) {
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
                    onOpenPredictionDialog = { onEvent(MatchesEvent.OpenDialog) },
                    onSelectMatch = { onEvent(MatchesEvent.SelectMatch(it)) }
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
            onSubmit =
            {
                if (state.predictions.any { it.matchId == state.selectedMatch.id }) {
                    onEvent(MatchesEvent.UpdatePrediction)
                } else {
                    onEvent(MatchesEvent.SubmitPrediction)
                }
            },
            onDismiss = { onEvent(MatchesEvent.CloseDialog) },
            edit = state.predictions.any { it.matchId == state.selectedMatch.id }
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MatchesContentPreview() {
    CompositionLocalProvider(LocalAuthUser provides MainState(isLoading = false)) {
        CompositionLocalProvider(LocalNavController provides rememberNavController()) {
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
                    onEvent = {}
                )
            }
        }
    }
}
