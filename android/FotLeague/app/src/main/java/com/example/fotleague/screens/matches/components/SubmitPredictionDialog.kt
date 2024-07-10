package com.example.fotleague.screens.matches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.fotleague.LocalAuthUser
import com.example.fotleague.LocalNavController
import com.example.fotleague.Route
import com.example.fotleague.ui.Logos
import com.example.fotleague.ui.components.picker.Picker
import com.example.fotleague.ui.components.picker.rememberPickerState
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.Gray
import com.example.fotleague.ui.theme.LightGray

@Composable
fun SubmitPredictionDialog(homeTeam: String, awayTeam: String, onDismiss: () -> Unit) {
    val homeGoals = remember { (0..15).map { it.toString() } }
    val homePickerState = rememberPickerState()
    val awayGoals = remember { (0..15).map { it.toString() } }
    val awayPickerState = rememberPickerState()

    val navController = LocalNavController.current
    val authUser = LocalAuthUser.current

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
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
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (dashRef, homeIconRef, awayIconRef, homeScore, awayScore) = createRefs()
                        Icon(
                            painter = painterResource(
                                id = Logos.getResourceId(homeTeam)
                            ),
                            contentDescription = "Team Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .constrainAs(homeIconRef) {
                                    end.linkTo(homeScore.start, margin = 24.dp)
                                    centerVerticallyTo(parent)
                                }
                                .size(48.dp)
                        )
                        Picker(
                            items = homeGoals,
                            state = homePickerState,
                            visibleItemsCount = 3,
                            textModifier = Modifier.padding(2.dp),
                            modifier = Modifier
                                .width(48.dp)
                                .constrainAs(homeScore) {
                                    end.linkTo(dashRef.start, margin = 8.dp)
                                    centerVerticallyTo(parent)
                                }
                        )
                        Text(
                            text = "-",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .widthIn(min = 16.dp)
                                .constrainAs(dashRef) { centerTo(parent) })
                        Picker(
                            items = awayGoals,
                            state = awayPickerState,
                            visibleItemsCount = 3,
                            textModifier = Modifier.padding(2.dp),
                            modifier = Modifier
                                .width(48.dp)
                                .constrainAs(awayScore) {
                                    start.linkTo(dashRef.end, margin = 8.dp)
                                    centerVerticallyTo(parent)
                                }
                        )
                        Icon(
                            painter = painterResource(
                                id = Logos.getResourceId(awayTeam)
                            ),
                            contentDescription = "Team Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .constrainAs(awayIconRef) {
                                    start.linkTo(awayScore.end, margin = 24.dp)
                                    centerVerticallyTo(parent)
                                }
                                .size(48.dp)
                        )
                    }
                }
                Button(
                    onClick = {
                        if (!authUser.isLoggedIn) {
                            navController.navigate(Route.Auth.route)
                        } else {
                            onDismiss()
                        }
                    },
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
private fun SubmitPredictionModalPreview() {
    FotLeagueTheme {
        SubmitPredictionDialog("Liverpool", "Everton") {}
    }
}