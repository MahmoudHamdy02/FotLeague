package com.example.fotleague.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme

@Composable
fun StatsScreen() {

    val list = remember {
        mapOf(
            Pair("Current season score", "99"),
            Pair("Best season score", "102"),
            Pair("Current global position", "52"),
            Pair("Best global position", "30"),
            Pair("Leagues joined", "12"),
            Pair("Top 3 league finishes", "10")
        )
    }

    StatsContent(list = list)
}

@Composable
private fun StatsContent(list: Map<String, String>) {
    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(list.toList()) {
                    StatsItem(text = it.first, value = it.second)
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = DarkGray)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = { Text(text = "Stats") },
        windowInsets = WindowInsets(0.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
    )
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
            list = mapOf(
                Pair("Current season score", "99"),
                Pair("Best season score", "102"),
                Pair("Current global position", "52"),
                Pair("Best global position", "30"),
                Pair("Leagues joined", "12"),
                Pair("Top 3 league finishes", "10")
            )
        )
    }
}