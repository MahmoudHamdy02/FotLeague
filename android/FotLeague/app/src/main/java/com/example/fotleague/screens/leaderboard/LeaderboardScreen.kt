package com.example.fotleague.screens.leaderboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fotleague.ui.navigation.TopBar

@Composable
fun LeaderboardScreen() {
    Scaffold(
        topBar = {
            TopBar(text = "Leaderboard")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "Leaderboard screen")
        }
    }
}