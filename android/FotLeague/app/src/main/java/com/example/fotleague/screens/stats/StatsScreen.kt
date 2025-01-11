package com.example.fotleague.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fotleague.LocalNavController
import com.example.fotleague.ui.navigation.BottomNavigation
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen() {
    val seasonList = mapOf(
        Pair("Current season score", "99"),
        Pair("Current global position", "52"),
    )
    val allTimeList = mapOf(
        Pair("Best season score", "102"),
        Pair("Best global position", "30"),
    )
    val leaguesList = mapOf(
        Pair("Leagues joined", "12"),
        Pair("Top 3 league finishes", "10")
    )
    val navController = LocalNavController.current

    Scaffold(
        bottomBar = {
            BottomNavigation(navController)
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGray),
                title = { Text(text = "Stats", fontWeight = FontWeight.Medium) }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            StatsContent(seasonList, allTimeList, leaguesList)
        }
    }
}

@Composable
private fun StatsContent(
    seasonList: Map<String, String>,
    allTimeList: Map<String, String>,
    leaguesList: Map<String, String>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "Season", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
        }
        items(seasonList.toList()) {
            StatsItem(text = it.first, value = it.second)
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DarkGray)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "All Time", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
        }
        items(allTimeList.toList()) {
            StatsItem(text = it.first, value = it.second)
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DarkGray)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Leagues", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
        }
        items(leaguesList.toList()) {
            StatsItem(text = it.first, value = it.second)
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DarkGray)
        }
    }
}


@Composable
private fun StatsItem(text: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text)
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Preview
@Composable
private fun StatsContentPreview() {
    FotLeagueTheme {
        StatsContent(
            seasonList = mapOf(
                Pair("Current season score", "99"),
                Pair("Current global position", "52"),
            ),
            allTimeList = mapOf(
                Pair("Best season score", "102"),
                Pair("Best global position", "30"),
            ),
            leaguesList = mapOf(
                Pair("Leagues joined", "12"),
                Pair("Top 3 league finishes", "10")
            )
        )
    }
}