package com.example.fotleague.screens.matches

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fotleague.LocalAuthUser
import com.example.fotleague.LocalNavController
import com.example.fotleague.Route
import com.example.fotleague.models.Prediction
import com.example.fotleague.screens.matches.components.SubmitPredictionDialog
import com.example.fotleague.ui.Logos
import com.example.fotleague.ui.navigation.TopBar
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import kotlinx.coroutines.CoroutineScope
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
            if (state.error != null) {
                Text(text = state.error!!, modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    GameweeksRow(
                        selectedTabIndex = selectedTabIndex,
                        setSelectedTabIndex = { i -> selectedTabIndex = i },
                        gameweeks = gameweeks,
                        scope = scope,
                        pagerState = pagerState
                    )
                    if (state.isLoading || LocalAuthUser.current.isLoading) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(48.dp)
                            )
                        }
                    } else {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) { index ->
                            MatchesList(state = state, index = index, viewModel = viewModel)
                        }
                    }
                }
            }
        }
        if (state.predictionDialogOpen)
            SubmitPredictionDialog(
                homeTeam = state.selectedMatch.home,
                awayTeam = state.selectedMatch.away,
                homePickerState = state.homePickerState,
                awayPickerState = state.awayPickerState,
                onSubmit = {
                    if (state.predictions.any { it.matchId == state.selectedMatch.id }) {
                        viewModel.onEvent(MatchesEvent.UpdatePrediction)
                    } else {
                        viewModel.onEvent(MatchesEvent.SubmitPrediction)
                    }
                },
                onDismiss = { viewModel.onEvent(MatchesEvent.CloseDialog) },
                edit = state.predictions.any { it.matchId == state.selectedMatch.id }
            )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameweeksRow(
    selectedTabIndex: Int,
    setSelectedTabIndex: (Int) -> Unit,
    gameweeks: List<Int>,
    scope: CoroutineScope,
    pagerState: PagerState
) {
    ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
        gameweeks.forEachIndexed { index, i ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                    setSelectedTabIndex(index)
                },
                text = { Text(text = "Game Week $i") },
                modifier = Modifier.background(Background)
            )
        }
    }
}

@Composable
fun MatchesList(state: MatchesState, index: Int, viewModel: MatchesViewModel) {
    val navController = LocalNavController.current
    val authUser = LocalAuthUser.current

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Matches
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = LocalAuthUser.current.isLoggedIn.toString())
            }
            items(state.matches.filter { match -> match.gameweek == index + 1 }) { match ->
                val date = ZonedDateTime.parse(match.datetime)
                    .withZoneSameInstant(ZoneId.systemDefault())
                Match(
                    match.home,
                    match.away,
                    date.format(DateTimeFormatter.ofPattern("d MMM")),
                    date.format(DateTimeFormatter.ofPattern("h:mm a")),
                    prediction = state.predictions.find { it.matchId == match.id }
                ) {
                    if (!authUser.isLoggedIn) {
                        navController.navigate(Route.Auth.route)
                    } else {
                        viewModel.onEvent(MatchesEvent.OpenDialog)
                        viewModel.onEvent(MatchesEvent.SelectMatch(match))
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun Match(
    homeTeam: String,
    awayTeam: String,
    date: String,
    time: String,
    prediction: Prediction?,
    onClick: () -> Unit
) {
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
            if (prediction == null) {
                Text(text = "Tap to submit prediction", color = DarkGray, fontSize = 13.sp)
            } else {
                Text(
                    text = "Prediction: ${prediction.home} - ${prediction.away}",
                    color = DarkGray,
                    fontSize = 13.sp
                )
            }
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
                val (homeRef, awayRef, datetimeRef, homeIconRef, awayIconRef) = createRefs()
                Text(
                    text = homeTeam,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.constrainAs(homeRef) {
                        end.linkTo(homeIconRef.start, margin = 8.dp)
                        centerVerticallyTo(parent)
                    })
                Icon(
                    painter = painterResource(
                        id = Logos.getResourceId(homeTeam)
                    ),
                    contentDescription = "Team Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .constrainAs(homeIconRef) {
                            end.linkTo(datetimeRef.start, margin = 8.dp)
                            centerVerticallyTo(parent)
                        }
                        .size(24.dp)
                )
                Column(
                    modifier = Modifier
                        .widthIn(min = 60.dp)
                        .fillMaxHeight()
                        .constrainAs(datetimeRef) {
                            centerTo(parent)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        text = date,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 12.sp
                    )
                    Text(
                        text = time,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center

                    )
                }
                Icon(
                    painter = painterResource(
                        id = Logos.getResourceId(awayTeam)
                    ),
                    contentDescription = "Team Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .constrainAs(awayIconRef) {
                            start.linkTo(datetimeRef.end, margin = 8.dp)
                            centerVerticallyTo(parent)
                        }
                        .size(24.dp)
                )
                Text(
                    text = awayTeam,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.constrainAs(awayRef) {
                        start.linkTo(awayIconRef.end, margin = 8.dp)
                        centerVerticallyTo(parent)
                    })
            }
        }
    }
}

@Preview
@Composable
private fun MatchPreview() {
    FotLeagueTheme {
        Match("Liverpool", "Everton", "27 Aug", "9:00 PM", Prediction(0, 0, 0, 0)) {}
    }
}
