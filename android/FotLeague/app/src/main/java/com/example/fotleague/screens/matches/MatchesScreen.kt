package com.example.fotleague.screens.matches

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fotleague.R
import com.example.fotleague.ui.components.picker.Picker
import com.example.fotleague.ui.components.picker.rememberPickerState
import com.example.fotleague.ui.navigation.TopBar
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.Gray
import com.example.fotleague.ui.theme.LightGray
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MatchesScreen(
    viewModel: MatchesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val gameweeks = (1..38).toList()

    val scope = rememberCoroutineScope()

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        38
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopBar(text = "FotLeague", fontSize = 24, weight = FontWeight.Bold)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
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
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { index ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        // Matches
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 32.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(state.matches.filter { match -> match.gameweek == index + 1 }) { match ->
                                val date = ZonedDateTime.parse(match.datetime)
                                    .withZoneSameInstant(ZoneId.systemDefault())
                                Match(
                                    match.home,
                                    match.away,
                                    date.format(DateTimeFormatter.ofPattern("h:mm a"))
                                ) { viewModel.onEvent(MatchesEvent.OpenDialog) }
                            }
                        }
                    }
                }
            }
        }
        if (state.predictionDialogOpen)
            SubmitPredictionModal(
                homeTeam = "Liverpool",
                awayTeam = "Everton",
                onDismiss = { viewModel.onEvent(MatchesEvent.CloseDialog) })
    }
}

@Composable
fun Match(homeTeam: String, awayTeam: String, time: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((80 - 16).dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .align(Alignment.BottomCenter)
                .padding(vertical = 0.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            Text(text = "No prediction submitted", color = DarkGray, fontSize = 13.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (homeRef, awayRef, timeRef, homeIconRef, awayIconRef) = createRefs()
                Text(
                    text = homeTeam,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.constrainAs(homeRef) {
                        end.linkTo(homeIconRef.start, margin = 8.dp)
                    })
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.shield),
                    contentDescription = "Team Icon",
                    tint = LightGray,
                    modifier = Modifier.constrainAs(homeIconRef) {
                        end.linkTo(timeRef.start, margin = 8.dp)
                    }
                )
                Text(
                    text = time,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .widthIn(min = 60.dp)
                        .constrainAs(timeRef) { centerTo(parent) })
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.shield),
                    contentDescription = "Team Icon",
                    tint = LightGray,
                    modifier = Modifier.constrainAs(awayIconRef) {
                        start.linkTo(timeRef.end, margin = 8.dp)
                    }
                )
                Text(
                    text = awayTeam,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.constrainAs(awayRef) {
                        start.linkTo(awayIconRef.end, margin = 8.dp)
                    })
            }
        }
    }
}

@Composable
private fun SubmitPredictionModal(homeTeam: String, awayTeam: String, onDismiss: () -> Unit) {
    val homeGoals = remember { (0..15).map { it.toString() } }
    val homePickerState = rememberPickerState()
    val awayGoals = remember { (0..15).map { it.toString() } }
    val awayPickerState = rememberPickerState()

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Submit prediction",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LightGray
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (homeRef, awayRef, dashRef, homeIconRef, awayIconRef, homeScore, awayScore) = createRefs()
                        Text(
                            text = homeTeam,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.constrainAs(homeRef) {
                                end.linkTo(homeIconRef.start, margin = 8.dp)
                                centerVerticallyTo(parent)
                            })
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.shield),
                            contentDescription = "Team Icon",
                            tint = LightGray,
                            modifier = Modifier.constrainAs(homeIconRef) {
                                end.linkTo(homeScore.start, margin = 8.dp)
                                centerVerticallyTo(parent)
                            }
                        )
                        Picker(
                            items = homeGoals,
                            state = homePickerState,
                            visibleItemsCount = 3,
                            modifier = Modifier
                                .width(32.dp)
                                .constrainAs(homeScore) {
                                    end.linkTo(dashRef.start, margin = 8.dp)
                                }
                        )
                        Text(
                            text = "-",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .widthIn(min = 16.dp)
                                .constrainAs(dashRef) { centerTo(parent) })
                        Picker(
                            items = awayGoals,
                            state = awayPickerState,
                            visibleItemsCount = 3,
                            modifier = Modifier
                                .width(32.dp)
                                .constrainAs(awayScore) {
                                    start.linkTo(dashRef.end, margin = 8.dp)
                                }
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.shield),
                            contentDescription = "Team Icon",
                            tint = LightGray,
                            modifier = Modifier.constrainAs(awayIconRef) {
                                start.linkTo(awayScore.end, margin = 8.dp)
                                centerVerticallyTo(parent)
                            }
                        )
                        Text(
                            text = awayTeam,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.constrainAs(awayRef) {
                                start.linkTo(awayIconRef.end, margin = 8.dp)
                                centerVerticallyTo(parent)
                            })
                    }
                }
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Gray),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Submit", color = DarkGray)
                }
            }
        }
    }

}

@Preview
@Composable
private fun MatchPreview() {
    FotLeagueTheme {
        Match("Liverpool", "Everton", "9:00 PM") {}
    }
}

@Preview
@Composable
private fun SubmitPredictionModalPreview() {
    FotLeagueTheme {
        SubmitPredictionModal("Liverpool", "Everton") {}
    }
}