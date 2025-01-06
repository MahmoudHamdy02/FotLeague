package com.example.fotleague.screens.leagues.leaguedetails

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.fotleague.LocalNavController
import com.example.fotleague.LocalTopBar
import com.example.fotleague.R
import com.example.fotleague.Screen
import com.example.fotleague.models.UserScore
import com.example.fotleague.ui.components.ScoresTable
import com.example.fotleague.ui.navigation.AppBarState
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray

@Composable
fun LeagueDetailsScreen(
    viewModel: LeagueDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val authState by viewModel.authState.collectAsState()

    // TODO: Prevent icon flash by introducing loading state
    LocalTopBar.current(
        AppBarState(
            title = "Leagues",
            showNavigateBackIcon = true,
            actions = {
                if (state.league.ownerId == authState.user!!.id) {
                    IconButton(onClick = { viewModel.onEvent(LeagueDetailsEvent.OpenDeleteLeagueDialog) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = LightGray
                        )
                    }
                } else {
                    IconButton(onClick = { viewModel.onEvent(LeagueDetailsEvent.OpenLeaveLeagueDialog) }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.logout_24),
                            contentDescription = null,
                            tint = LightGray
                        )
                    }

                }
            })
    )

    val navController = LocalNavController.current
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(state.leagueLeft) {
        if (state.leagueLeft) {
            navController.navigate(Screen.LeaguesScreen.route) {
                popUpTo(Screen.LeaguesScreen.route)
            }
        }
    }

    LeagueDetailsContent(
        clipboardManager = clipboardManager,
        leagueName = state.league.name,
        ownerName = state.userScores.find { it.id == state.league.ownerId }?.name ?: "",
        code = state.league.code,
        userScores = state.userScores,
        isLeaveLeagueDialogOpen = state.isLeaveLeagueDialogOpen,
        onLeaveLeague = { viewModel.onEvent(LeagueDetailsEvent.LeaveLeague) },
        isDeleteLeagueDialogOpen = state.isDeleteLeagueDialogOpen,
        onDeleteLeague = { viewModel.onEvent(LeagueDetailsEvent.DeleteLeague) },
        onDismissLeaveLeagueDialog = { viewModel.onEvent(LeagueDetailsEvent.CloseLeaveLeagueDialog) },
        onDismissDeleteLeagueDialog = { viewModel.onEvent(LeagueDetailsEvent.CloseDeleteLeagueDialog) }
    )
}

@Composable
private fun LeagueDetailsContent(
    clipboardManager: ClipboardManager,
    leagueName: String,
    ownerName: String,
    code: String,
    userScores: List<UserScore>,
    isLeaveLeagueDialogOpen: Boolean,
    onLeaveLeague: () -> Unit,
    isDeleteLeagueDialogOpen: Boolean,
    onDeleteLeague: () -> Unit,
    onDismissLeaveLeagueDialog: () -> Unit,
    onDismissDeleteLeagueDialog: () -> Unit
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
                // Copy code button
                Row(
                    modifier = Modifier
                        .border(1.dp, DarkGray, RoundedCornerShape(4.dp))
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { clipboardManager.setText(AnnotatedString(code)) }
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

    if (isLeaveLeagueDialogOpen) {
        ConfirmLeaveLeagueDialog(
            onLeaveLeague = onLeaveLeague,
            onDismiss = onDismissLeaveLeagueDialog
        )
    }
    if (isDeleteLeagueDialogOpen) {
        ConfirmDeleteLeagueDialog(
            onDeleteLeague = onDeleteLeague,
            onDismiss = onDismissDeleteLeagueDialog
        )
    }
}

@Composable
private fun ConfirmLeaveLeagueDialog(
    onDismiss: () -> Unit,
    onLeaveLeague: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Leave league") },
        text = { Text(text = "Are you sure you want to leave this league?") },
        confirmButton = {
            TextButton(onClick = onLeaveLeague) {
                Text(text = "Leave")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
private fun ConfirmDeleteLeagueDialog(
    onDismiss: () -> Unit,
    onDeleteLeague: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete league") },
        text = { Text(text = "Are you sure you want to delete this league?") },
        confirmButton = {
            TextButton(onClick = onDeleteLeague) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ConfirmLeaveLeagueDialogPreview() {
    FotLeagueTheme {
        ConfirmLeaveLeagueDialog(
            onLeaveLeague = {},
            onDismiss = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ConfirmDeleteLeagueDialogPreview() {
    FotLeagueTheme {
        ConfirmDeleteLeagueDialog(
            onDeleteLeague = {},
            onDismiss = {}
        )
    }
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
            userScores = emptyList(),
            isLeaveLeagueDialogOpen = false,
            onLeaveLeague = {},
            onDeleteLeague = {},
            isDeleteLeagueDialogOpen = false,
            onDismissLeaveLeagueDialog = {},
            onDismissDeleteLeagueDialog = {}
        )
    }
}
