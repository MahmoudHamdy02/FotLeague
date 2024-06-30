package com.example.fotleague.ui.navigation

import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fotleague.R
import com.example.fotleague.Screen

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
            selectedIcon = ImageVector.vectorResource(id = R.drawable.sports_32),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.sports_24)
        ),
        BottomNavigationItem(
            title = "Leagues",
            screen = Screen.LeaguesScreen,
            selectedIcon = ImageVector.vectorResource(id = R.drawable.groups_filled_32),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.groups_24)
        ),
        BottomNavigationItem(
            title = "Leaderboard",
            screen = Screen.LeaderboardScreen,
            selectedIcon = ImageVector.vectorResource(id = R.drawable.trophy_filled_32),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.trophy_24)
        ),
        BottomNavigationItem(
            title = "Stats",
            screen = Screen.StatsScreen,
            selectedIcon = ImageVector.vectorResource(id = R.drawable.leaderboard_filled_32),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.leaderboard_24)
        ),
        BottomNavigationItem(
            title = "More",
            screen = Screen.MoreScreen,
            selectedIcon = ImageVector.vectorResource(id = R.drawable.menu_32),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.menu_24)
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
                    Icon(
                        imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                })
        }
    }
}