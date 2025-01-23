package com.example.fotleague.screens.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
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
fun PrimaryButton(onClick: () -> Unit, isDisabled: Boolean, text: @Composable () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        enabled = !isDisabled,
        modifier = Modifier
            .width(280.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
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