package com.example.fotleague.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fotleague.ui.theme.Gray
import com.example.fotleague.ui.theme.LightGray
import com.example.fotleague.ui.theme.Primary
import com.example.fotleague.ui.theme.PrimaryLight

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDisabled: Boolean = false,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    text: @Composable () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = contentPadding,
        enabled = !isDisabled,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .width(280.dp)
            .heightIn(40.dp)
            .background(
                if (isDisabled)
                    Brush.linearGradient(listOf(Gray, LightGray))
                else
                    Brush.linearGradient(listOf(Primary, PrimaryLight))
            )
    ) {
        text()
    }
}