package com.example.fotleague.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.fotleague.R
import com.example.fotleague.models.UserScore
import com.example.fotleague.ui.components.ScoresTable
import com.example.fotleague.ui.navigation.BottomNavigation
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel = hiltViewModel(),
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigation(navController, navBackStackEntry)
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGray),
                title = { Text(text = "Leaderboard", fontWeight = FontWeight.Medium) }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LeaderboardContent(
                error = state.error,
                isLoading = state.isLoading,
                numOfScores = state.numOfScores,
                userScores = state.scores,
                isNumOfScoresDropdownExpanded = state.isNumOfScoresDropdownExpanded,
                onDismissNumOfScoresDropdown = { viewModel.onEvent(LeaderboardEvent.DismissNumOfScoresDropdown) },
                onExpandNumOfScoresDropdown = { viewModel.onEvent(LeaderboardEvent.ExpandNumOfScoresDropdown) },
                onSelectNumOfScores = { viewModel.onEvent(LeaderboardEvent.SelectNumOfScores(it)) }
            )
        }
    }
}

@Composable
private fun LeaderboardContent(
    error: String?,
    isLoading: Boolean,
    numOfScores: Int,
    userScores: List<UserScore>,
    isNumOfScoresDropdownExpanded: Boolean,
    onDismissNumOfScoresDropdown: () -> Unit,
    onExpandNumOfScoresDropdown: () -> Unit,
    onSelectNumOfScores: (num: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(horizontal = 16.dp, vertical = 24.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Global Scores", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Top")
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier
                    .border(1.dp, DarkGray, RoundedCornerShape(4.dp))
                    .clickable { onExpandNumOfScoresDropdown() }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = numOfScores.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrow_down_24),
                    contentDescription = "Copy",
                    tint = LightGray
                )
                DropdownMenu(
                    expanded = isNumOfScoresDropdownExpanded,
                    onDismissRequest = onDismissNumOfScoresDropdown,
                    modifier = Modifier.background(Background)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "10") },
                        onClick = { onSelectNumOfScores(10) })
                    DropdownMenuItem(
                        text = { Text(text = "20") },
                        onClick = { onSelectNumOfScores(20) })
                    DropdownMenuItem(
                        text = { Text(text = "50") },
                        onClick = { onSelectNumOfScores(50) })
                }
            }
        }
        if (error != null) {
            Text(text = error)
        } else if (isLoading) {
            Text(text = "Loading...")
        } else {
            ScoresTable(userScores = userScores)
        }
    }
}

@Preview
@Composable
private fun LeaderboardContentPreview() {
    FotLeagueTheme {
        LeaderboardContent(
            error = null,
            isLoading = false,
            numOfScores = 10,
            userScores = emptyList(),
            isNumOfScoresDropdownExpanded = false,
            onDismissNumOfScoresDropdown = {},
            onExpandNumOfScoresDropdown = {},
            onSelectNumOfScores = {}
        )
    }
}