package com.example.fotleague.screens.leagues.leaguedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fotleague.R
import com.example.fotleague.ui.navigation.TopBar
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.Gray
import com.example.fotleague.ui.theme.LightGray

@Composable
fun LeagueDetails() {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopBar(text = "Leagues")
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // League info
                Column {
                    // League name and code
                    Row {
                        Text(
                            text = "League name",
                            fontSize = 22.sp,
                            color = LightGray,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .border(1.dp, DarkGray, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.copy_24),
                                contentDescription = "Copy",
                                tint = LightGray
                            )
                            Text(
                                text = "jf8r1G",
                                color = LightGray,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    // League owner
                    Row {
                        Text(text = "Owner: Mahmoud", fontSize = 18.sp, color = LightGray)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = DarkGray)
                }

                // Members table
                Text(text = "Members", color = LightGray, fontSize = 20.sp)
                TableScreen()
            }
        }
    }
}

@Preview
@Composable
fun LeagueDetailsPreview() {
    FotLeagueTheme {
        LeagueDetails()
    }
}

@Composable
fun TableScreen() {
    // Just a fake data... a Pair of Int and String
    val tableData = (1..20).mapIndexed { index, _ ->
        index to "Item $index"
    }
    // Each cell of a column must have the same weight.
    val column1Weight = .15f // 30%
    val column2Weight = .65f // 70%
    val column3Weight = .2f // 70%

    LazyColumn(
        Modifier
            .fillMaxSize()
    ) {
        // Header
        item {
            Row {
                TableCell(text = "Pos.", weight = column1Weight, color = Gray)
                TableCell(text = "Name", weight = column2Weight, color = Gray)
                TableCell(text = "Score", weight = column3Weight, color = Gray)
            }
        }
        // Rows
        items(tableData) {
            val (id, text) = it
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = id.toString(), weight = column1Weight, color = LightGray)
                TableCell(text = text, weight = column2Weight, color = LightGray)
                TableCell(text = id.toString(), weight = column3Weight, color = LightGray)
            }
            HorizontalDivider(color = DarkGray)
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    color: Color
) {
    Text(
        text = text,
        color = color,
        modifier = Modifier
//            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}