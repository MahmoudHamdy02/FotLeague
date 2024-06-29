package com.example.fotleague.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.fotleague.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Hello world!")
        Button(onClick = {
            navController.navigate(Screen.SettingsScreen.route)
        }) {
            Text(text = "Go to settings")
        }
    }
}