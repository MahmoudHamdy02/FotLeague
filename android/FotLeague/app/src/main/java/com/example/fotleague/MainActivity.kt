package com.example.fotleague

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fotleague.ui.navigation.AppBarState
import com.example.fotleague.ui.navigation.BottomNavigation
import com.example.fotleague.ui.navigation.TopBar
import com.example.fotleague.ui.theme.FotLeagueTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalTopBar = compositionLocalOf<(appBarState: AppBarState) -> Unit> {
    error("No appBarState provided")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: MainViewModel by viewModels()
        viewModel.init()

//        LifecycleUtil.restartAppEvent.observe(this) {
//            if (it) {
//                restart()
//            }
//        }

        setContent {
            FotLeagueTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                var appBarState by remember {
                    mutableStateOf(AppBarState())
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBar(appBarState, navController::navigateUp) },
                    bottomBar = {
                        if (currentRoute in bottomBarRoutes) {
                            BottomNavigation(navController)
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        CompositionLocalProvider(LocalNavController provides navController) {
                            CompositionLocalProvider(LocalTopBar provides {
                                appBarState = it
                            }) {
                                Navigation()
                            }
                        }
                    }
                }
            }
        }

    }

//    private fun restart() {
//        Log.d("LIFECYCLE", "restart")
//        LifecycleUtil.onSetRestartFalse()
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        finish()
//        startActivity(intent)
//    }
}
