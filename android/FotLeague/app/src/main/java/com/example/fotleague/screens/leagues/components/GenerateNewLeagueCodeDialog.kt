package com.example.fotleague.screens.leagues.components

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.fotleague.ui.theme.FotLeagueTheme

@Composable
fun GenerateNewLeagueCodeDialog(
    onDismiss: () -> Unit,
    onGenerateCode: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Generate new league code") },
        text = { Text(text = "The current code will become invalid") },
        confirmButton = {
            TextButton(onClick = onGenerateCode) {
                Text(text = "Generate")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GenerateNewLeagueCodeDialogPreview() {
    FotLeagueTheme {
        GenerateNewLeagueCodeDialog(
            onGenerateCode = {},
            onDismiss = {}
        )
    }
}
