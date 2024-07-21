package com.example.fotleague.screens.matches

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.navigation.compose.rememberNavController
import com.example.fotleague.LocalAuthUser
import com.example.fotleague.LocalNavController
import com.example.fotleague.LocalTopBar
import com.example.fotleague.MainState
import com.example.fotleague.Route
import com.example.fotleague.models.Match
import com.example.fotleague.models.MatchStatus
import com.example.fotleague.models.Prediction
import com.example.fotleague.models.Score
import com.example.fotleague.screens.matches.components.SubmitPredictionDialog
import com.example.fotleague.ui.Logos
import com.example.fotleague.ui.navigation.AppBarState
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray
import com.example.fotleague.ui.theme.LightGreen
import com.example.fotleague.ui.theme.LightRed
import com.example.fotleague.ui.theme.LightYellow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MatchesScreen(
    viewModel: MatchesViewModel = hiltViewModel()
) {
    LocalTopBar.current(AppBarState(title = "FotLeague"))

    val state by viewModel.state.collectAsState()

    MatchesContent(
        state = state,
        onEvent = { viewModel.onEvent(it) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MatchesContent(
    state: MatchesState,
    onEvent: (event: MatchesEvent) -> Unit
) {
    val pagerState = rememberPagerState {
        38
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)) {
        GameweeksRow(
            pagerState = pagerState
        )
        if (state.error != null) {
            Text(text = state.error)
        } else if (state.isLoading || LocalAuthUser.current.isLoading) {
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
                    matches = state.matches,
                    predictions = state.predictions,
                    scores = state.scores,
                    onOpenPredictionDialog = { onEvent(MatchesEvent.OpenDialog) },
                    onSelectMatch = { onEvent(MatchesEvent.SelectMatch(it)) }
                )
            }
        }
    }

    if (state.predictionDialogOpen) {
        SubmitPredictionDialog(
            homeTeam = state.selectedMatch.home,
            awayTeam = state.selectedMatch.away,
            homePickerState = state.homePickerState,
            awayPickerState = state.awayPickerState,
            onSubmit =
            {
                if (state.predictions.any { it.matchId == state.selectedMatch.id }) {
                    onEvent(MatchesEvent.UpdatePrediction)
                } else {
                    onEvent(MatchesEvent.SubmitPrediction)
                }
            },
            onDismiss = { onEvent(MatchesEvent.CloseDialog) },
            edit = state.predictions.any { it.matchId == state.selectedMatch.id }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GameweeksRow(pagerState: PagerState) {
    val gameweeks = (1..38).toList()

    val scope = rememberCoroutineScope()

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
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
                Match(
                    match = match,
                    prediction = predictions.find { it.matchId == match.id },
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
    match: Match,
    prediction: Prediction?,
    score: Int?,
    onClick: () -> Unit
) {
    val datetime = ZonedDateTime.parse(match.datetime).withZoneSameInstant(ZoneId.systemDefault())

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
                    text = match.home,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.constrainAs(homeRef) {
                        end.linkTo(homeIconRef.start, margin = 8.dp)
                        centerVerticallyTo(parent)
                    })
                Icon(
                    painter = painterResource(
                        id = Logos.getResourceId(match.home)
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
                if (match.matchStatus == MatchStatus.Upcoming.num) {
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
                            text = datetime.format(DateTimeFormatter.ofPattern("d MMM")),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            lineHeight = 12.sp
                        )
                        Text(
                            text = datetime.format(DateTimeFormatter.ofPattern("h:mm a")),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center

                        )
                    }
                } else {
                    Text(
                        text = "${match.homeScore}  -  ${match.awayScore}",
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
                        text = if (match.matchStatus == MatchStatus.InProgress.num) "${duration.toMinutes()}'" else "FT",
                        fontSize = 11.sp,
                        lineHeight = 8.sp,
                        modifier = Modifier.constrainAs(matchTimeRef) {
                            top.linkTo(centerRef.bottom)
                            centerHorizontallyTo(parent)
                        })
                }
                Icon(
                    painter = painterResource(
                        id = Logos.getResourceId(match.away)
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
                    text = match.away,
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
            match = Match(
                id = 1,
                home = "Liverpool",
                away = "",
                homeScore = 0,
                awayScore = 0,
                matchStatus = 1,
                datetime = "2024-08-16 09:00:00",
                season = 0,
                gameweek = 1
            ),
            prediction = Prediction(0, 0, 0, 0),
            score = null,
        ) {}
    }
}

@Preview
@Composable
private fun InProgressMatchPreview() {
    FotLeagueTheme {
        Match(
            match = Match(
                id = 1,
                home = "Liverpool",
                away = "",
                homeScore = 0,
                awayScore = 0,
                matchStatus = 2,
                datetime = "2024-08-16 09:00:00",
                season = 0,
                gameweek = 1
            ),
            prediction = Prediction(0, 0, 0, 0),
            score = null,
        ) {}
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MatchesContentPreview() {
    CompositionLocalProvider(LocalAuthUser provides MainState(isLoading = false)) {
        CompositionLocalProvider(LocalNavController provides rememberNavController()) {
            FotLeagueTheme {
                MatchesContent(
                    state = MatchesState(
                        matches = listOf(
                            Match(
                                id = 1,
                                home = "Liverpool",
                                away = "",
                                homeScore = 0,
                                awayScore = 0,
                                matchStatus = 1,
                                datetime = "2024-08-16 09:00:00",
                                season = 0,
                                gameweek = 1
                            )
                        )
                    ),
                    onEvent = {}
                )
            }
        }
    }
}
