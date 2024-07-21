package com.example.fotleague.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

data class AppBarState(
    val title: String = "",
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val showNavigateBackIcon: Boolean = false,
    val titleFontWeight: FontWeight = FontWeight.Normal
)
