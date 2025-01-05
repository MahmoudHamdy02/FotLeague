package com.example.fotleague.screens.matches.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.fotleague.ui.theme.Background
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameweeksRow(pagerState: PagerState) {
    val gameweeks = (1..38).toList()

    val scope = rememberCoroutineScope()

    var selectedTabIndex by remember {
        mutableIntStateOf(pagerState.currentPage)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
        gameweeks.forEachIndexed { index, i ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                    selectedTabIndex = index
                },
                text = { Text(text = "Game Week $i") },
                modifier = Modifier.background(Background)
            )
        }
    }
}