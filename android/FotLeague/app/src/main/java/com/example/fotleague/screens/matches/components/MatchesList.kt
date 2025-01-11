package com.example.fotleague.screens.matches.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fotleague.LocalNavController
import com.example.fotleague.Route
import com.example.fotleague.models.Match
import com.example.fotleague.models.MatchStatus
import com.example.fotleague.models.Prediction
import com.example.fotleague.models.Score

@Composable
fun MatchesList(
    index: Int,
    matches: List<Match>,
    predictions: List<Prediction>,
    scores: List<Score>,
    isLoggedIn: Boolean,
    onOpenPredictionDialog: () -> Unit,
    onSelectMatch: (match: Match) -> Unit,
) {
    val navController = LocalNavController.current

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Matches
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(matches.filter { match -> match.gameweek == index + 1 }) { match ->
                Match(
                    match = match,
                    prediction = predictions.find { it.matchId == match.id },
                    score = scores.find { it.matchId == match.id }?.score
                ) {
                    if (match.matchStatus != MatchStatus.Played.num && match.matchStatus != MatchStatus.InProgress.num) {
                        if (!isLoggedIn) {
                            navController.navigate(Route.Auth.route)
                        } else {
                            onOpenPredictionDialog()
                            onSelectMatch(match)
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}