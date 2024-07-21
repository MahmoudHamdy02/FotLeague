package com.example.fotleague.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(appBarState: AppBarState, onBackArrowClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = appBarState.title, fontWeight = appBarState.titleFontWeight) },
        actions = { appBarState.actions?.invoke(this) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
        navigationIcon = {
            if (appBarState.showNavigateBackIcon) {
                IconButton(onClick = onBackArrowClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = LightGray
                    )
                }
            }
        }
    )
}