package com.example.fotleague.screens.matches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.fotleague.models.Match
import com.example.fotleague.models.MatchStatus
import com.example.fotleague.models.Prediction
import com.example.fotleague.ui.Logos
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray
import com.example.fotleague.ui.theme.LightGreen
import com.example.fotleague.ui.theme.LightRed
import com.example.fotleague.ui.theme.LightYellow
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Match(
    match: Match, prediction: Prediction?, score: Int?, onClick: () -> Unit
) {
    val datetime = ZonedDateTime.parse(match.datetime).withZoneSameInstant(ZoneId.systemDefault())
    val currentTime = ZonedDateTime.now()
    val currentDate = currentTime.toLocalDate()
    val matchDate = when (datetime.toLocalDate()) {
        currentDate -> "Today"
        currentDate.plusDays(1) -> "Tomorrow"
        currentDate.minusDays(1) -> "Yesterday"
        else -> datetime.format(DateTimeFormatter.ofPattern("d MMM"))
    }
    val bgColor =
        if (score == 3) LightGreen else if (score == 1) LightYellow else if (score == 0 || (score != null && prediction == null && match.matchStatus == MatchStatus.Played.num)) LightRed else LightGray

    Box(modifier = Modifier
        .fillMaxWidth()
        .height((88 - 16).dp)
        .clickable { onClick() }) {
        // Bottom Rectangle
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
                if (prediction == null) {
                    if (match.matchStatus == MatchStatus.Played.num || match.matchStatus == MatchStatus.InProgress.num)
                        "No prediction submitted"
                    else "Tap to submit prediction"
                } else "Prediction: ${prediction.home} - ${prediction.away}"
            Text(
                text = text, color = DarkGray, fontSize = 13.sp
            )
        }
        // Top Rectangle
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
                Text(text = match.home,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.constrainAs(homeRef) {
                        end.linkTo(homeIconRef.start, margin = 8.dp)
                        centerVerticallyTo(parent)
                    })
                Icon(painter = painterResource(
                    id = Logos.getResourceId(match.home)
                ),
                    contentDescription = "Team Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .constrainAs(homeIconRef) {
                            end.linkTo(centerRef.start, margin = 8.dp)
                            centerVerticallyTo(parent)
                        }
                        .size(24.dp))
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
                            text = matchDate,
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
                    Text(text = "${match.homeScore}  -  ${match.awayScore}",
                        lineHeight = 8.sp,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .widthIn(60.dp)
                            .constrainAs(centerRef) {
                                centerTo(parent)
                            })

                    val duration = Duration.between(datetime, currentTime)
                    Text(text = if (match.matchStatus == MatchStatus.InProgress.num) "${duration.toMinutes()}'" else "FT",
                        fontSize = 11.sp,
                        lineHeight = 8.sp,
                        modifier = Modifier.constrainAs(matchTimeRef) {
                            top.linkTo(centerRef.bottom)
                            centerHorizontallyTo(parent)
                        })
                }
                Icon(painter = painterResource(
                    id = Logos.getResourceId(match.away)
                ),
                    contentDescription = "Team Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .constrainAs(awayIconRef) {
                            start.linkTo(centerRef.end, margin = 8.dp)
                            centerVerticallyTo(parent)
                        }
                        .size(24.dp))
                Text(text = match.away,
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
                datetime = "2024-08-16T19:00:00.000Z",
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
                datetime = "2024-08-16T19:00:00.000Z",
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
private fun PlayedAndPredictedMatchPreview() {
    FotLeagueTheme {
        Match(
            match = Match(
                id = 1,
                home = "Liverpool",
                away = "Everton",
                homeScore = 2,
                awayScore = 0,
                matchStatus = 3,
                datetime = "2024-08-16T19:00:00.000Z",
                season = 0,
                gameweek = 1
            ),
            prediction = Prediction(0, 0, 1, 0),
            score = 1,
        ) {}
    }
}

@Preview
@Composable
private fun PlayedAndNotPredictedMatchPreview() {
    FotLeagueTheme {
        Match(
            match = Match(
                id = 1,
                home = "Liverpool",
                away = "Everton",
                homeScore = 2,
                awayScore = 0,
                matchStatus = 3,
                datetime = "2024-08-16T19:00:00.000Z",
                season = 0,
                gameweek = 1
            ),
            prediction = null,
            score = 0,
        ) {}
    }
}