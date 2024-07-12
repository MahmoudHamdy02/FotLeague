package com.example.fotleague.screens.leagues.leaguedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fotleague.LocalNavController
import com.example.fotleague.R
import com.example.fotleague.models.UserScore
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.Gray
import com.example.fotleague.ui.theme.LightGray

@Composable
fun LeagueDetailsScreen(
    viewModel: LeagueDetailsViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    val state by viewModel.state.collectAsState()

    LeagueDetailsContent(
        leagueName = state.league.name,
        ownerName = state.userScores.find { it.id == state.league.ownerId }?.name ?: "",
        code = state.league.code,
        userScores = state.userScores,
        onBackArrowClick = { navController.popBackStack() }
    )
}

@Composable
private fun LeagueDetailsContent(
    leagueName: String,
    ownerName: String,
    code: String,
    userScores: List<UserScore>,
    onBackArrowClick: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopBar(
                onLeaveLeagueClick = {},
                onBackArrowClick = onBackArrowClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // League info
                Column {
                    // League name and code
                    Row {
                        Text(
                            text = leagueName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .border(1.dp, DarkGray, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.copy_24),
                                contentDescription = "Copy",
                                tint = LightGray
                            )
                            Text(
                                text = code,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    // League owner
                    Row {
                        Text(text = "Owner: $ownerName", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = DarkGray)
                }

                // Members table
                Text(text = "Members", fontSize = 20.sp)
                ScoresTable(userScores = userScores)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onLeaveLeagueClick: () -> Unit,
    onBackArrowClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Leagues") },
        windowInsets = WindowInsets(0.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
        navigationIcon = {
            IconButton(onClick = onBackArrowClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        },
        actions = {
            IconButton(onClick = onLeaveLeagueClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.logout_24),
                    contentDescription = null,
                    tint = LightGray
                )
            }
        }
    )
}

@Preview
@Composable
fun LeagueDetailsPreview() {
    FotLeagueTheme {
        LeagueDetailsContent(
            leagueName = "League name",
            ownerName = "owner",
            code = "fn28gD",
            userScores = emptyList(),
            onBackArrowClick = {}
        )
    }
}

@Composable
fun ScoresTable(userScores: List<UserScore>) {

    // Each cell of a column must have the same weight.
    val column1Weight = .15f // 30%
    val column2Weight = .65f // 70%
    val column3Weight = .2f // 70%

    LazyColumn(
        Modifier
            .fillMaxSize()
    ) {
        // Header
        item {
            Row {
                TableCell(text = "Pos.", weight = column1Weight, color = Gray)
                TableCell(text = "Name", weight = column2Weight, color = Gray)
                TableCell(text = "Score", weight = column3Weight, color = Gray)
            }
        }
        // Rows
        itemsIndexed(userScores) { index, userScore ->
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = (index + 1).toString(), weight = column1Weight)
                TableCell(text = userScore.name, weight = column2Weight)
                TableCell(text = userScore.score.toString(), weight = column3Weight)
            }
            HorizontalDivider(color = DarkGray)
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    color: Color = Color.Unspecified
) {
    Text(
        text = text,
        color = color,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
    )
}