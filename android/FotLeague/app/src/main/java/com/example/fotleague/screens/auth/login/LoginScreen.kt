package com.example.fotleague.screens.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fotleague.R
import com.example.fotleague.Screen
import com.example.fotleague.ui.components.PrimaryButton
import com.example.fotleague.screens.auth.components.drawTopAndBottomCurves
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.Gray
import com.example.fotleague.ui.theme.LightGray

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            navController.popBackStack(Screen.Matches, false)
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = { TopBar(navController::popBackStack) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            LoginScreenContent(
                topPadding = paddingValues.calculateTopPadding(),
                isLoading = state.isLoading,
                email = state.email,
                setEmail = { viewModel.onEvent(LoginEvent.SetEmail(it)) },
                password = state.password,
                setPassword = { viewModel.onEvent(LoginEvent.SetPassword(it)) },
                isPasswordVisible = state.isPasswordVisible,
                togglePasswordVisibility = { viewModel.onEvent(LoginEvent.TogglePasswordVisibility) },
                isRememberMeChecked = state.rememberMe,
                setIsRememberMeChecked = { viewModel.onEvent(LoginEvent.SetRememberMe(it)) },
                onLogin = { viewModel.onEvent(LoginEvent.Login) },
                onNavigateToSignUp = { navController.navigate(Screen.Auth.Signup) }
            )
        }
    }
}

@Composable
private fun LoginScreenContent(
    topPadding: Dp,
    isLoading: Boolean,
    email: String,
    setEmail: (String) -> Unit,
    password: String,
    setPassword: (String) -> Unit,
    isPasswordVisible: Boolean,
    togglePasswordVisibility: () -> Unit,
    isRememberMeChecked: Boolean,
    setIsRememberMeChecked: (Boolean) -> Unit,
    onLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .drawTopAndBottomCurves(topPadding)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterVertically)
    ) {

        Text(text = "Log in to FotLeague", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (isPasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (isPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = togglePasswordVisibility) {
                        Icon(imageVector = image, description)
                    }
                }
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isRememberMeChecked,
                    onCheckedChange = { setIsRememberMeChecked(it) })
                Text(text = "Stay signed in?")
            }
        }

        PrimaryButton(
            onClick = onLogin,
            isDisabled = isLoading
        ) {
            Text(text = if (!isLoading) "Log in" else "Logging in...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(Modifier.width(280.dp), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(0.3f), color = Gray)
                Text(
                    text = "or continue with",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(0.3f), color = Gray)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SocialLoginButton(iconResourceId = R.drawable.google)
                SocialLoginButton(iconResourceId = R.drawable.facebook)
                SocialLoginButton(iconResourceId = R.drawable.twitter)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Don't have an account?")
            Text(
                text = "Register now",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    onNavigateToSignUp()
                })
        }
    }
}

@Composable
private fun SocialLoginButton(iconResourceId: Int) {
    Button(
        onClick = { /*TODO*/ }, modifier = Modifier
            .size(48.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = LightGray)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconResourceId),
            contentDescription = "Google",
            tint = Color.Unspecified
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onBackArrowClick: () -> Unit) {
    TopAppBar(
        title = { Text("Log in", fontWeight = FontWeight.Medium) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            IconButton(onClick = onBackArrowClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = LightGray
                )
            }
        }
    )
}

@Preview
@Composable
private fun LoginScreenPreview() {
    FotLeagueTheme {
        LoginScreenContent(
            topPadding = (0).dp,
            isLoading = false,
            email = "",
            setEmail = {},
            password = "",
            setPassword = {},
            isPasswordVisible = false,
            togglePasswordVisibility = {},
            isRememberMeChecked = false,
            setIsRememberMeChecked = {},
            onLogin = {},
            onNavigateToSignUp = {}
        )
    }
}