package com.example.fotleague.screens.leagues.leaguedetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fotleague.R
import com.example.fotleague.Screen
import com.example.fotleague.models.UserScore
import com.example.fotleague.ui.components.ScoresTable
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray
import kotlinx.coroutines.delay

@Composable
fun LeagueDetailsScreen(
    viewModel: LeagueDetailsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()
    val authState by viewModel.authState.collectAsState()

    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(NavigationBarDefaults.windowInsets),
        topBar = {
            TopBar(
                onBackArrowClick = navController::popBackStack,
                onNavigate = { navController.navigate("${Screen.LeagueSettings.route}/${state.league.id},${state.league.ownerId == authState.user!!.id}") }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LeagueDetailsContent(
                clipboardManager = clipboardManager,
                leagueName = state.league.name,
                ownerName = state.userScores.find { it.id == state.league.ownerId }?.name ?: "",
                code = state.league.code,
                userScores = state.userScores
            )
        }
    }
}

@Composable
private fun LeagueDetailsContent(
    clipboardManager: ClipboardManager,
    leagueName: String,
    ownerName: String,
    code: String,
    userScores: List<UserScore>,
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

                CopyLeagueCodeButton(clipboardManager = clipboardManager, code = code)
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

@Composable
private fun CopyLeagueCodeButton(clipboardManager: ClipboardManager, code: String) {
    var clicked by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(clicked) {
        if (clicked) {
            delay(1000)
            clicked = false
        }
    }
    Row(
        modifier = Modifier
            .border(1.dp, DarkGray, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                clipboardManager.setText(AnnotatedString(code))
                clicked = true
            }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedContent(
            targetState = clicked,
            label = "Copy animation",
            transitionSpec = {
                fadeIn(animationSpec = tween(250)) togetherWith fadeOut(
                    animationSpec = tween(
                        250
                    )
                )
            }
        ) { clicked ->
            when (clicked) {
                false -> Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.copy_24),
                    contentDescription = "Copy",
                    tint = LightGray
                )

                true -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Copied",
                        tint = LightGray
                    )
                }
            }
        }
        Text(
            text = code,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onBackArrowClick: () -> Unit, onNavigate: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Leagues") },
        actions = {
            IconButton(onClick = onNavigate) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGray),
        navigationIcon = {
            IconButton(onClick = onBackArrowClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
        val clipboardManager = LocalClipboardManager.current
        LeagueDetailsContent(
            clipboardManager = clipboardManager,
            leagueName = "League name",
            ownerName = "owner",
            code = "fn28gD",
            userScores = emptyList()
        )
    }
}
