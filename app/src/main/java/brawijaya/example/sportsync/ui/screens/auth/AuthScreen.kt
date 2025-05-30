package brawijaya.example.sportsync.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.ui.screens.auth.components.LoginContent
import brawijaya.example.sportsync.ui.screens.auth.components.RegisterContent
import brawijaya.example.sportsync.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var isLogin by remember { mutableStateOf(true) }
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthOfDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreeOnTerms by remember { mutableStateOf(false) }

    // Handle authentication state changes
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Auth.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(authState.error) {
        authState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isLogin) "Welcome Back" else "Get Started Now",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp)
                )

                Text(
                    text = if (isLogin) "Log in to access your account" else "Create an account or log in\nto explore about SportSync",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(bottom = if (isLogin) 40.dp else 16.dp)
                )

                AuthSwitch(
                    isLogin = isLogin,
                    onRegisterClick = { isLogin = false },
                    onLoginClick = { isLogin = true }
                )

                if (isLogin) {
                    LoginContent(
                        email = email,
                        phone = phone,
                        password = password,
                        onEmailChange = { email = it },
                        onPhoneChange = { phone = it },
                        onPasswordChange = { password = it },
                        onSignUpClick = { isLogin = false },
                        onLoginButtonClick = { isEmailLogin ->
                            if (email.isNotBlank() || phone.isNotBlank()) {
                                viewModel.signIn(
                                    emailOrPhone = if (isEmailLogin) email else phone,
                                    password = password,
                                    isEmail = isEmailLogin
                                )
                            }
                        }
                    )
                } else {
                    RegisterContent(
                        fullName = fullName,
                        username = username,
                        email = email,
                        birthOfDate = birthOfDate,
                        phone = phone,
                        password = password,
                        agreeOnTerms = agreeOnTerms,
                        onFullNameChange = { fullName = it },
                        onUsernameChange = { username = it },
                        onEmailChange = { email = it },
                        onBirthOfDateChange = { birthOfDate = it },
                        onPhoneChange = { phone = it },
                        onPasswordChange = { password = it },
                        onAgreeOnTermsChange = { agreeOnTerms = it },
                        onSignUpClick = {
                            if (agreeOnTerms && fullName.isNotBlank() &&
                                username.isNotBlank() && email.isNotBlank() &&
                                phone.isNotBlank() && password.isNotBlank()) {
                                viewModel.signUp(
                                    fullName = fullName,
                                    username = username,
                                    email = email,
                                    birthDate = birthOfDate,
                                    password = password,
                                    useEmail = true,
                                    phone = phone
                                )
                            }
                        }
                    )
                }
            }

            if (authState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFFCCD78)
                    )
                }
            }
        }
    }
}

@Composable
fun AuthSwitch(
    isLogin: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .height(70.dp)
            .width(336.dp)
            .background(Color(0xFFFCCD78)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .width(141.dp)
                    .height(54.dp)
                    .background(if (isLogin) Color.Transparent else Color(0xFFFBBB46))
                    .clickable {
                        onRegisterClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .width(141.dp)
                    .height(54.dp)
                    .background(if (isLogin) Color(0xFFFBBB46) else Color.Transparent)
                    .clickable {
                        onLoginClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Log In",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}