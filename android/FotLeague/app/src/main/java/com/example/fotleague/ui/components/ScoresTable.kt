package com.example.fotleague.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fotleague.models.UserScore
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.Gray

@Composable
fun ScoresTable(userScores: List<UserScore>) {
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
        itemsIndexed(userScores) { index, userScore ->
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = (index + 1).toString(), weight = column1Weight)
                TableCell(text = userScore.name, weight = column2Weight)
                TableCell(text = userScore.score.toString(), weight = column3Weight)
            }
            HorizontalDivider(color = DarkGray)
        }
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    color: Color = Color.Unspecified
) {
    Text(
        text = text,
        color = color,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
    )
}