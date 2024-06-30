package com.example.fotleague.screens.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fotleague.ui.theme.FotLeagueTheme

@Composable
fun MatchesScreen() {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .padding(horizontal = 8.dp)
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "FotLeague", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "Home screen")
        }
    }
}

@Composable
fun Match() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .align(Alignment.BottomCenter)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Prediction status")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Match data", color = Color.LightGray)
        }

    }
}

@Preview
@Composable
fun MatchPreview(modifier: Modifier = Modifier) {
    FotLeagueTheme {
        Match()
    }
}