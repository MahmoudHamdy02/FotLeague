package com.example.fotleague

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
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
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray
import dagger.hilt.android.AndroidEntryPoint

val LocalAuthUser = compositionLocalOf<MainState> {
    error("No LocalAuthUser provided")
}

val LocalTopBar = compositionLocalOf<(appBarState: AppBarState) -> Unit> {
    error("No function provided")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: MainViewModel by viewModels()

        LifecycleUtil.restartAppEvent.observe(this) {
            if (it) {
                restart()
            }
        }

        setContent {
            FotLeagueTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val state by viewModel.state.collectAsState()

                var appBarState by remember {
                    mutableStateOf(AppBarState())
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = appBarState.title) },
                            actions = { appBarState.actions?.invoke(this) },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
                            navigationIcon = {
                                if (appBarState.showNavigateBackIcon) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = null,
                                            tint = LightGray
                                        )
                                    }
                                }
                            }
                        )
                    },
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
                            CompositionLocalProvider(LocalAuthUser provides state) {
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

    }

    private fun restart() {
        Log.d("LIFECYCLE", "restart")
        LifecycleUtil.onSetRestartFalse()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
        startActivity(intent)
    }
}
