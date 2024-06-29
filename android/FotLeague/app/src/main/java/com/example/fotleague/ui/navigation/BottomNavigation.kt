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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fotleague.Screen

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun BottomNavigation(navController: NavHostController) {
    println("Debugging here")
    val items = listOf(
        BottomNavigationItem(
            title = "Matches",
            route = Screen.MatchesScreen.route,
            selectedIcon = Icons.Default.Add,
            unselectedIcon = Icons.Default.Add
        ),
        BottomNavigationItem(
            title = "Leagues",
            route = Screen.LeaguesScreen.route,
            selectedIcon = Icons.Default.Add,
            unselectedIcon = Icons.Default.Add
        ),
        BottomNavigationItem(
            title = "Leaderboard",
            route = Screen.LeaderboardScreen.route,
            selectedIcon = Icons.Default.Add,
            unselectedIcon = Icons.Default.Add
        ),
        BottomNavigationItem(
            title = "Stats",
            route = Screen.StatsScreen.route,
            selectedIcon = Icons.Default.Add,
            unselectedIcon = Icons.Default.Add
        ),
        BottomNavigationItem(
            title = "More",
            route = Screen.MoreScreen.route,
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
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(imageVector = item.selectedIcon, contentDescription = item.title)
                })
        }
    }
}