package com.example.fotleague.screens.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fotleague.LocalAuthUser
import com.example.fotleague.LocalTopBar
import com.example.fotleague.R
import com.example.fotleague.ui.navigation.AppBarState
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray

@Composable
fun MoreScreen(
    viewModel: MoreViewModel = hiltViewModel()
) {
    LocalTopBar.current(AppBarState(title = "More"))

    val authUser = LocalAuthUser.current

    MoreContent(
        isLoggedIn = authUser.isLoggedIn,
        onLogOutClick = { viewModel.onEvent(MoreEvent.Logout) }
    )
}

@Composable
private fun MoreContent(
    isLoggedIn: Boolean,
    onLogOutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RowButton(
            icon = Icons.Default.AccountCircle,
            text = "Account",
            onClick = {}
        )
        RowButton(
            icon = Icons.Default.Settings,
            text = "Settings",
            onClick = {}
        )
        RowButton(
            icon = ImageVector.vectorResource(id = R.drawable.bug_report_24),
            text = "Feedback and bug reports",
            onClick = {}
        )
        if (isLoggedIn) {
            RowButton(
                icon = ImageVector.vectorResource(id = R.drawable.logout_24),
                text = "Log out",
                onClick = onLogOutClick
            )
        }
    }
}

@Composable
private fun RowButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = LightGray)
        Text(text = text)
    }
}

@Preview
@Composable
private fun MoreContentPreview() {
    FotLeagueTheme {
        MoreContent(
            isLoggedIn = true,
            onLogOutClick = {}
        )
    }
}