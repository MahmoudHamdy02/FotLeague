package com.example.fotleague.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.fotleague.ui.components.ScoresTable
import com.example.fotleague.ui.navigation.BottomNavigation
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.Gray
import com.example.fotleague.ui.theme.LightGray
import com.example.fotleague.ui.theme.Primary
import com.example.fotleague.ui.theme.PrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel = hiltViewModel(),
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?
) {
    val gameweekScoresState by viewModel.gameweekScoresState.collectAsState()
    val scoresTableState by viewModel.scoresTableState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigation(navController, navBackStackEntry) },
        topBar = { TopBar() }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            val pullState = rememberPullToRefreshState()
            PullToRefreshBox(
                state = pullState,
                isRefreshing = scoresTableState.isRefreshing,
                onRefresh = { viewModel.onEvent(LeaderboardEvent.Refresh) },
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        state = pullState,
                        isRefreshing = scoresTableState.isRefreshing,
                        color = Primary
                    )
                }
            ) {
                LeaderboardContent(
                    scoresTableState = scoresTableState,
                    gameweekScoresState = gameweekScoresState,
                    onNextGameweek = { viewModel.onEvent(LeaderboardEvent.SelectNextGameweek) },
                    onPreviousGameweek = { viewModel.onEvent(LeaderboardEvent.SelectPreviousGameweek) },
                    onDismissNumOfScoresDropdown = { viewModel.onEvent(LeaderboardEvent.DismissNumOfScoresDropdown) },
                    onExpandNumOfScoresDropdown = { viewModel.onEvent(LeaderboardEvent.ExpandNumOfScoresDropdown) },
                    onSelectNumOfScores = { viewModel.onEvent(LeaderboardEvent.SelectNumOfScores(it)) }
                )
            }
        }
    }
}

@Composable
private fun LeaderboardContent(
    gameweekScoresState: GameweekScoresState,
    scoresTableState: ScoresTableState,
    onNextGameweek: () -> Unit,
    onPreviousGameweek: () -> Unit,
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
        GameweekScores(
            gameweekScoresState,
            onNextGameweek,
            onPreviousGameweek
        )
        Spacer(Modifier.height(24.dp))
        LeaderboardTable(
            scoresTableState,
            onDismissNumOfScoresDropdown,
            onExpandNumOfScoresDropdown,
            onSelectNumOfScores
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGray),
        title = { Text(text = "Leaderboard", fontWeight = FontWeight.Medium) }
    )
}

@Composable
private fun GameweekScores(
    state: GameweekScoresState,
    onNextGameweek: () -> Unit,
    onPreviousGameweek: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkGray)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Gameweek selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousGameweek, enabled = state.isPrevGameweekButtonEnabled) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    tint = if (state.isPrevGameweekButtonEnabled) Primary else Gray,
                    contentDescription = "Previous gameweek"
                )
            }
            Text(text = "Gameweek ${state.selectedGameweek}", fontWeight = FontWeight.Medium)
            IconButton(onClick = onNextGameweek, enabled = state.isNextGameweekButtonEnabled) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    tint = if (state.isNextGameweekButtonEnabled) Primary else Gray,
                    contentDescription = "Previous gameweek"
                )
            }

        }
        // Scores
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                24.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreCard("Average", state.averageScore, fontSize = 30)
            VerticalDivider(modifier = Modifier.height(50.dp), color = Color(0xFF555555))
            ScoreCard(
                topText = "Your Score",
                score = state.userScore,
                borderWidth = 2,
                brush = Brush.verticalGradient(listOf(Primary, PrimaryDark))
            )
            VerticalDivider(modifier = Modifier.height(50.dp), color = Color(0xFF555555))
            ScoreCard("Highest", state.highestScore, fontSize = 30)
        }
    }
}

@Composable
private fun ScoreCard(
    topText: String,
    score: Int?,
    borderWidth: Int = 1,
    brush: Brush = Brush.verticalGradient(listOf(Gray, DarkGray)),
    fontSize: Int = 36
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = topText, fontSize = 13.sp)
        Box(
            Modifier
                .border(borderWidth.dp, brush, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(text = "${score ?: "-"}", fontWeight = FontWeight.Medium, fontSize = fontSize.sp)
        }
    }
}

@Composable
private fun LeaderboardTable(
    state: ScoresTableState,
    onDismissNumOfScoresDropdown: () -> Unit,
    onExpandNumOfScoresDropdown: () -> Unit,
    onSelectNumOfScores: (num: Int) -> Unit
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
                text = "${state.numOfScores}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_down_24),
                contentDescription = "Copy",
                tint = LightGray
            )
            DropdownMenu(
                expanded = state.isNumOfScoresDropdownExpanded,
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
    if (state.error != null) {
        Text(text = state.error)
    } else if (state.isLoading) {
        Text(text = "Loading...")
    } else {
        ScoresTable(userScores = state.scores)
    }
}

@Preview
@Composable
private fun LeaderboardContentPreview() {
    FotLeagueTheme {
        LeaderboardContent(
            scoresTableState = ScoresTableState(),
            gameweekScoresState = GameweekScoresState(),
            onDismissNumOfScoresDropdown = {},
            onExpandNumOfScoresDropdown = {},
            onSelectNumOfScores = {},
            onNextGameweek = {},
            onPreviousGameweek = {}
        )
    }
}