package com.example.fotleague.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fotleague.Screen
import com.example.fotleague.ui.theme.FotLeagueTheme

data class BottomNavigationItem(
    val title: String,
    val screen: Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun BottomNavigation(navController: NavHostController) {
    println("Debugging here")
    val items = listOf(
        BottomNavigationItem(
            title = "Matches",
            screen = Screen.MatchesScreen,
            selectedIcon = Icons.Default.Add,
            unselectedIcon = Icons.Default.Add
        ),
        BottomNavigationItem(
            title = "Leagues",
            screen = Screen.LeaguesScreen,
            selectedIcon = Icons.Default.Add,
            unselectedIcon = Icons.Default.Add
        ),
        BottomNavigationItem(
            title = "Leaderboard",
            screen = Screen.LeaderboardScreen,
            selectedIcon = Icons.Default.Add,
            unselectedIcon = Icons.Default.Add
        ),
        BottomNavigationItem(
            title = "Stats",
            screen = Screen.StatsScreen,
            selectedIcon = Icons.Default.Add,
            unselectedIcon = Icons.Default.Add
        ),
        BottomNavigationItem(
            title = "More",
            screen = Screen.MoreScreen,
            selectedIcon = Icons.Default.Add,
            unselectedIcon = Icons.Default.Add
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier = Modifier.height(120.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedItemIndex,
                label = { Text(text = item.title, fontSize = 10.sp) },
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.screen.route)
                },
                icon = {
                    Icon(imageVector = item.selectedIcon, contentDescription = item.title)
                })
        }
    }
}