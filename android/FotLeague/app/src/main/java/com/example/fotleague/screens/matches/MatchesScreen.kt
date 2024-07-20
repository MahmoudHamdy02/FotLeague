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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.fotleague.models.Match
import com.example.fotleague.models.MatchStatus
import com.example.fotleague.models.Prediction
import com.example.fotleague.models.Score
import com.example.fotleague.screens.matches.components.SubmitPredictionDialog
import com.example.fotleague.ui.Logos
import com.example.fotleague.ui.components.picker.PickerState
import com.example.fotleague.ui.components.picker.rememberPickerState
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray
import com.example.fotleague.ui.theme.LightGreen
import com.example.fotleague.ui.theme.LightRed
import com.example.fotleague.ui.theme.LightYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
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

    MatchesContent(
        error = state.error,
        isLoading = state.isLoading,
        selectedTabIndex = selectedTabIndex,
        setSelectedTabIndex = { selectedTabIndex = it },
        gameweeks = gameweeks,
        scope = scope,
        pagerState = pagerState,
        predictionDialogOpen = state.predictionDialogOpen,
        selectedMatch = state.selectedMatch,
        homePickerState = state.homePickerState,
        awayPickerState = state.awayPickerState,
        predictions = state.predictions,
        onSubmitPrediction = { viewModel.onEvent(MatchesEvent.SubmitPrediction) },
        onUpdatePrediction = { viewModel.onEvent(MatchesEvent.UpdatePrediction) },
        onClosePredictionDialog = { viewModel.onEvent(MatchesEvent.CloseDialog) },
        matches = state.matches,
        scores = state.scores,
        onOpenPredictionDialog = { viewModel.onEvent(MatchesEvent.OpenDialog) },
        onSelectMatch = { viewModel.onEvent(MatchesEvent.SelectMatch(it)) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MatchesContent(
    error: String?,
    isLoading: Boolean,
    selectedTabIndex: Int,
    setSelectedTabIndex: (Int) -> Unit,
    gameweeks: List<Int>,
    scope: CoroutineScope,
    pagerState: PagerState,
    predictionDialogOpen: Boolean,
    selectedMatch: Match,
    homePickerState: PickerState,
    awayPickerState: PickerState,
    matches: List<Match>,
    scores: List<Score>,
    predictions: List<Prediction>,
    onSubmitPrediction: () -> Unit,
    onUpdatePrediction: () -> Unit,
    onClosePredictionDialog: () -> Unit,
    onOpenPredictionDialog: () -> Unit,
    onSelectMatch: (match: Match) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = { TopBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            if (error != null) {
                Text(text = error, modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    GameweeksRow(
                        selectedTabIndex = selectedTabIndex,
                        setSelectedTabIndex = { setSelectedTabIndex(it) },
                        gameweeks = gameweeks,
                        scope = scope,
                        pagerState = pagerState
                    )
                    if (isLoading || LocalAuthUser.current.isLoading) {
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
                            MatchesList(
                                index = index,
                                matches = matches,
                                predictions = predictions,
                                scores = scores,
                                onOpenPredictionDialog = onOpenPredictionDialog,
                                onSelectMatch = onSelectMatch
                            )
                        }
                    }
                }
            }
        }
        if (predictionDialogOpen) {
            SubmitPredictionDialog(
                homeTeam = selectedMatch.home,
                awayTeam = selectedMatch.away,
                homePickerState = homePickerState,
                awayPickerState = awayPickerState,
                onSubmit =
                if (predictions.any { it.matchId == selectedMatch.id }) {
                    onUpdatePrediction
                } else {
                    onSubmitPrediction
                },
                onDismiss = onClosePredictionDialog,
                edit = predictions.any { it.matchId == selectedMatch.id }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = { Text(text = "FotLeague", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
        windowInsets = WindowInsets(0.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GameweeksRow(
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
private fun MatchesList(
    index: Int,
    matches: List<Match>,
    predictions: List<Prediction>,
    scores: List<Score>,
    onOpenPredictionDialog: () -> Unit,
    onSelectMatch: (match: Match) -> Unit,
) {
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
            }
            items(matches.filter { match -> match.gameweek == index + 1 }) { match ->
                val date = ZonedDateTime.parse(match.datetime)
                    .withZoneSameInstant(ZoneId.systemDefault())
                Match(
                    match.home,
                    match.away,
                    date.format(DateTimeFormatter.ofPattern("d MMM")),
                    date.format(DateTimeFormatter.ofPattern("h:mm a")),
                    datetime = date,
                    prediction = predictions.find { it.matchId == match.id },
                    status = match.matchStatus,
                    homeScore = match.homeScore,
                    awayScore = match.awayScore,
                    score = scores.find { it.matchId == match.id }?.score
                ) {
                    if (!authUser.isLoggedIn) {
                        navController.navigate(Route.Auth.route)
                    } else {
                        onOpenPredictionDialog()
                        onSelectMatch(match)
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
private fun Match(
    homeTeam: String,
    awayTeam: String,
    date: String,
    time: String,
    datetime: ZonedDateTime,
    prediction: Prediction?,
    status: Int,
    homeScore: Int,
    awayScore: Int,
    score: Int?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((88 - 16).dp)
            .clickable { onClick() }
    ) {
        val bgColor =
            if (score == 3) LightGreen else if (score == 1) LightYellow else if (score == 0) LightRed else LightGray
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .align(Alignment.BottomCenter)
                .padding(vertical = 0.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            val text =
                if (prediction == null)
                    "Tap to submit prediction"
                else
                    "Prediction: ${prediction.home} - ${prediction.away}"
            Text(
                text = text,
                color = DarkGray,
                fontSize = 13.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (homeRef, awayRef, centerRef, homeIconRef, awayIconRef, matchTimeRef) = createRefs()
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
                            end.linkTo(centerRef.start, margin = 8.dp)
                            centerVerticallyTo(parent)
                        }
                        .size(24.dp)
                )
                if (status == MatchStatus.Upcoming.num) {
                    Column(
                        modifier = Modifier
                            .widthIn(min = 60.dp)
                            .fillMaxHeight()
                            .constrainAs(centerRef) {
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
                } else {
                    Text(
                        text = "$homeScore  -  $awayScore",
                        lineHeight = 8.sp,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .widthIn(60.dp)
                            .constrainAs(centerRef) {
                                centerTo(parent)
                            })

                    val currentTime = ZonedDateTime.now()
                    val duration = Duration.between(datetime, currentTime)
                    Text(
                        text = if (status == MatchStatus.InProgress.num) "${duration.toMinutes()}'" else "FT",
                        fontSize = 11.sp,
                        lineHeight = 8.sp,
                        modifier = Modifier.constrainAs(matchTimeRef) {
                            top.linkTo(centerRef.bottom)
                            centerHorizontallyTo(parent)
                        })
                }
                Icon(
                    painter = painterResource(
                        id = Logos.getResourceId(awayTeam)
                    ),
                    contentDescription = "Team Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .constrainAs(awayIconRef) {
                            start.linkTo(centerRef.end, margin = 8.dp)
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
        Match(
            "Liverpool",
            "Everton",
            "27 Aug",
            "9:00 PM",
            ZonedDateTime.now(),
            Prediction(0, 0, 0, 0),
            1,
            0,
            0,
            null,
        ) {}
    }
}

@Preview
@Composable
private fun InProgressMatchPreview() {
    FotLeagueTheme {
        Match(
            "Liverpool",
            "Everton",
            "27 Aug",
            "9:00 PM",
            ZonedDateTime.now().minusMinutes(30),
            Prediction(0, 0, 0, 0),
            2,
            1,
            0,
            1,
        ) {}
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun MatchesContentPreview() {
    FotLeagueTheme {
        MatchesContent(
            error = null,
            isLoading = false,
            selectedTabIndex = 0,
            setSelectedTabIndex = {},
            gameweeks = (1..38).toList(),
            scope = rememberCoroutineScope(),
            pagerState = rememberPagerState {
                38
            },
            predictionDialogOpen = false,
            selectedMatch = Match(0, "", "", 0, 0, 0, "", 0, 0),
            homePickerState = rememberPickerState(),
            awayPickerState = rememberPickerState(),
            matches = listOf(Match(0, "", "", 0, 0, 0, "", 0, 0)),
            scores = emptyList(),
            predictions = emptyList(),
            onSubmitPrediction = {},
            onUpdatePrediction = {},
            onClosePredictionDialog = {},
            onOpenPredictionDialog = {},
            onSelectMatch = {})
    }
}