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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fotleague.R
import com.example.fotleague.Screen
import com.example.fotleague.ui.navigation.TopBar
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray

@Composable
fun LeaguesScreen(navController: NavHostController) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopBar(text = "Leagues")
        }) { paddingValues ->
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
                Header()
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    League(navController = navController, name = "League 1 name", pos = 1)
                    League(navController = navController, name = "League 2 name", pos = 3)
                    League(navController = navController, name = "League 3 name", pos = 2)
                }
            }
        }
    }
}

// Components

@Composable
fun League(navController: NavHostController, name: String, pos: Int) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .clip(
            RoundedCornerShape(8.dp)
        )
        .background(DarkGray)
        .clickable { navController.navigate(Screen.LeagueDetails.route) }
        .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = name, fontSize = 16.sp, color = LightGray)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = pos.toString(), color = LightGray)
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
        League(navController = rememberNavController(), name = "League 1 name", pos = 1)
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