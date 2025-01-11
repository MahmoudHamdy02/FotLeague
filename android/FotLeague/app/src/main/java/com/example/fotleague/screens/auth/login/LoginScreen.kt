package com.example.fotleague.screens.auth.login

import android.graphics.Matrix
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fotleague.LocalNavController
import com.example.fotleague.R
import com.example.fotleague.Screen
import com.example.fotleague.ui.theme.Background
import com.example.fotleague.ui.theme.DarkGray
import com.example.fotleague.ui.theme.FotLeagueTheme
import com.example.fotleague.ui.theme.Gray
import com.example.fotleague.ui.theme.LightGray
import com.example.fotleague.ui.theme.Primary
import com.example.fotleague.ui.theme.PrimaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            navController.popBackStack(Screen.MatchesScreen.route, false)
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Log in", fontWeight = FontWeight.Medium) },
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
            LoginScreenContent(
                topPadding = paddingValues.calculateTopPadding(),
                email = state.email,
                setEmail = { viewModel.onEvent(LoginEvent.SetEmail(it)) },
                password = state.password,
                setPassword = { viewModel.onEvent(LoginEvent.SetPassword(it)) },
                isRememberMeChecked = state.rememberMe,
                setIsRememberMeChecked = { viewModel.onEvent(LoginEvent.SetRememberMe(it)) },
                onLogin = { viewModel.onEvent(LoginEvent.Login) },
                navController = navController
            )
        }
    }
}

@Composable
private fun LoginScreenContent(
    topPadding: Dp,
    email: String,
    setEmail: (String) -> Unit,
    password: String,
    setPassword: (String) -> Unit,
    isRememberMeChecked: Boolean,
    setIsRememberMeChecked: (Boolean) -> Unit,
    onLogin: () -> Unit,
    navController: NavHostController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .drawWithCache {
                onDrawBehind {
                    val path =
                        PathParser()
                            .parsePathString("M0 0H360V56.7889C192.305 10.8926 75.5466 150.969 0 113.578V0Z")
                            .toPath()
                    val pathSize = path.getBounds().size
                    val matrix = Matrix()
                    matrix.postScale(size.width / pathSize.width, size.width / pathSize.width)
                    path
                        .asAndroidPath()
                        .transform(matrix)

                    val bottomPath =
                        PathParser()
                            .parsePathString("M0 144C42.5887 79.049 184.885 112.783 240 0V144H0Z")
                            .toPath()
                    bottomPath.moveTo(0f, 0f)
                    var bottomPathSize = bottomPath.getBounds().size
                    val bottomMatrix = Matrix()
                    bottomMatrix.postScale(
                        (2f/3f)*size.width / bottomPathSize.width,
                        (2f/3f)*size.width / bottomPathSize.width
                    )
                    bottomPath
                        .asAndroidPath()
                        .transform(bottomMatrix)
                    bottomPathSize = bottomPath.getBounds().size

                    path.translate(Offset(0f, -topPadding.toPx()))
                    bottomPath.translate(Offset(size.width/3f, size.height - bottomPathSize.height))
                    drawPath(path, Primary)
                    drawPath(bottomPath, Primary)
                }
            }
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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isRememberMeChecked,
                    onCheckedChange = { setIsRememberMeChecked(it) })
                Text(text = "Stay signed in?")
            }
        }

        Button(
            onClick = { onLogin() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .width(280.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(listOf(Primary, PrimaryLight))
                )
        ) {
            Text(text = "Log in", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                    navController.navigate(Screen.Auth.SignupScreen.route)
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

@Preview
@Composable
private fun LoginScreenPreview() {
    FotLeagueTheme {
        LoginScreenContent(
            topPadding = (1).dp,
            email = "",
            setEmail = {},
            password = "",
            setPassword = {},
            isRememberMeChecked = false,
            setIsRememberMeChecked = {},
            onLogin = {},
            navController = rememberNavController()
        )
    }
}