package com.example.fotleague.screens.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fotleague.Screen
import com.example.fotleague.screens.auth.components.drawTopAndBottomCurves
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.LightGray
import com.example.fotleague.ui.theme.Primary
import com.example.fotleague.ui.theme.PrimaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.onSignup) {
        if (state.onSignup) {
            navController.navigate(Screen.MatchesScreen.route)
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Sign up", fontWeight = FontWeight.Medium) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = LightGray
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            SignupScreenContent(
                topPadding = paddingValues.calculateTopPadding(),
                username = state.username,
                setUsername = { viewModel.onEvent(SignUpEvent.SetUsername(it)) },
                email = state.email,
                setEmail = { viewModel.onEvent(SignUpEvent.SetEmail(it)) },
                password = state.password,
                setPassword = { viewModel.onEvent(SignUpEvent.SetPassword(it)) },
                onSignUpClick = { viewModel.onEvent(SignUpEvent.SignUp) }
            )
        }

    }
}

@Composable
private fun SignupScreenContent(
    topPadding: Dp,
    username: String,
    email: String,
    setEmail: (String) -> Unit,
    password: String,
    setPassword: (String) -> Unit,
    setUsername: (String) -> Unit,
    onSignUpClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .drawTopAndBottomCurves(topPadding)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {
        Text(text = "Create an account", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                value = username,
                onValueChange = { setUsername(it) },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = DarkGray),
                label = { Text(text = "Username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            TextField(
                value = email,
                onValueChange = { setEmail(it) },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = DarkGray),
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            TextField(
                value = password,
                onValueChange = { setPassword(it) },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = DarkGray),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }

        Button(
            onClick = { onSignUpClick() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .width(280.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(listOf(Primary, PrimaryLight))
                )
        ) {
            Text(text = "Create account", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}


@Preview
@Composable
private fun SignupScreenPreview() {
    FotLeagueTheme {
        SignupScreenContent(
            topPadding = (0).dp,
            username = "",
            setUsername = {},
            email = "",
            setEmail = {},
            password = "",
            setPassword = {},
            onSignUpClick = {}
        )
    }
}