package com.example.fotleague.screens.more

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fotleague.LocalAuthUser

@Composable
fun MoreScreen(
    viewModel: MoreViewModel = hiltViewModel()
) {
    Text(text = "More screen")

    val authUser = LocalAuthUser.current

    if (authUser.isLoggedIn) {
        Button(onClick = { viewModel.onEvent(MoreEvent.Logout) }) {
            Text(text = "Logout")
        }
    }
}