package com.example.fotleague.screens.leagues.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.Gray
import com.example.fotleague.ui.theme.LightGray

@Composable
fun JoinLeagueDialog(
    code: String,
    setCode: (code: String) -> Unit,
    onJoinClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
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
                    text = "Join league",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LightGray
                )
                TextField(
                    value = code,
                    onValueChange = setCode,
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = DarkGray),
                    label = { Text(text = "Code") },
                )
                Button(
                    onClick = onJoinClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Gray),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Join", color = DarkGray)
                }
            }
        }
    }
}

@Preview
@Composable
private fun JoinLeagueDialogPreview() {
    FotLeagueTheme {
        JoinLeagueDialog(
            code = "",
            setCode = {},
            onJoinClick = {},
            onDismiss = {}
        )
    }
}