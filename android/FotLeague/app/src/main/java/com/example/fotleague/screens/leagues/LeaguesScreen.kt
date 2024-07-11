package com.example.fotleague.screens.leagues

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fotleague.LocalAuthUser
import com.example.fotleague.LocalNavController
import com.example.fotleague.R
import com.example.fotleague.Screen
import com.example.fotleague.models.League
import com.example.fotleague.screens.leagues.components.CreateLeagueDialog
import com.example.fotleague.screens.leagues.components.JoinLeagueDialog
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray

@Composable
fun LeaguesScreen(
    viewModel: LeaguesViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    val state by viewModel.state.collectAsState()

    LeaguesContent(
        leagues = state.leagues,
        onLeagueClick = { navController.navigate(Screen.LeagueDetails.route) },
        isJoinLeagueDialogOpen = state.isJoinLeagueDialogOpen,
        onOpenJoinLeagueDialog = { viewModel.onEvent((LeaguesEvent.OpenJoinLeagueDialog)) },
        onOpenCreateLeagueDialog = {viewModel.onEvent(LeaguesEvent.OpenCreateLeagueDialog)},
        onDismissJoinLeagueDialog = { viewModel.onEvent(LeaguesEvent.CloseJoinLeagueDialog) },
        code = state.joinLeagueDialogCode,
        setCode = { viewModel.onEvent(LeaguesEvent.SetJoinLeagueDialogCode(it)) },
        onJoinClick = { viewModel.onEvent(LeaguesEvent.JoinLeague) },
        isCreateLeagueDialogOpen = state.isCreateLeagueDialogOpen,
        onDismissCreateLeagueDialog = { viewModel.onEvent(LeaguesEvent.CloseCreateLeagueDialog) },
        name = state.createLeagueDialogName,
        setName = { viewModel.onEvent(LeaguesEvent.SetCreateLeagueDialogName(it)) },
        onCreateClick = { viewModel.onEvent(LeaguesEvent.CreateLeague) }
    )
}

@Composable
fun LeaguesContent(
    leagues: List<League>,
    onLeagueClick: () -> Unit,
    isJoinLeagueDialogOpen: Boolean,
    onOpenJoinLeagueDialog: () -> Unit,
    onOpenCreateLeagueDialog: () -> Unit,
    onDismissJoinLeagueDialog: () -> Unit,
    code: String,
    setCode: (code: String) -> Unit,
    onJoinClick: () -> Unit,
    isCreateLeagueDialogOpen: Boolean,
    onDismissCreateLeagueDialog: () -> Unit,
    name: String,
    setName: (code: String) -> Unit,
    onCreateClick: () -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopBar(
                onOpenJoinLeagueDialog = onOpenJoinLeagueDialog,
                onOpenCreateLeagueDialog = onOpenCreateLeagueDialog
            )
        })
    { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
            ) {
                if (!LocalAuthUser.current.isLoggedIn) {
                    Text(text = "Log in to see your leagues")
                } else {
                    Header()
                    LeaguesList(leagues = leagues, onLeagueClick = onLeagueClick)
                }
            }
        }
        if (isJoinLeagueDialogOpen) {
            JoinLeagueDialog(
                code = code,
                setCode = setCode,
                onJoinClick = onJoinClick,
                onDismiss = onDismissJoinLeagueDialog
            )
        }
        if (isCreateLeagueDialogOpen) {
            CreateLeagueDialog(
                name = name,
                setName = setName,
                onCreateClick = onCreateClick,
                onDismiss = onDismissCreateLeagueDialog
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onOpenJoinLeagueDialog: () -> Unit,
    onOpenCreateLeagueDialog: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Leagues") },
        windowInsets = WindowInsets(0.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
        actions = {
            IconButton(onClick = onOpenCreateLeagueDialog) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = LightGray
                )
            }
            IconButton(onClick = onOpenJoinLeagueDialog) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.person_add_24),
                    contentDescription = null,
                    tint = LightGray
                )
            }
        }
    )
}

@Composable
fun LeaguesList(leagues: List<League>, onLeagueClick: () -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            League(name = "Global", pos = 1)
        }
        items(leagues.drop(1)) { league ->
            League(name = league.name, pos = 1, modifier = Modifier.clickable { onLeagueClick() })
        }
    }
}


@Composable
fun League(name: String, pos: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(DarkGray)
            .then(modifier)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(text = name, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = pos.toString())
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.double_arrow_right_24),
            contentDescription = "Right arrow",
            tint = LightGray
        )
    }
}

@Preview
@Composable
fun LeaguePreview(modifier: Modifier = Modifier) {
    FotLeagueTheme {
        League(name = "League 1 name", pos = 1)
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "League")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Pos.")
        Spacer(modifier = Modifier.width(20.dp))
    }
}