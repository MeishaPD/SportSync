package brawijaya.example.sportsync.ui.screens.auth

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.ui.screens.auth.components.LoginContent
import brawijaya.example.sportsync.utils.DateUtils
import java.util.Calendar

@Composable
fun AuthScreen(
    navController: NavController
) {

    val snackbarHostState = remember { SnackbarHostState() }

    var isLogin by remember { mutableStateOf(true) }

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthOfDate by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreeOnTerms by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                onLoginClick = { isLogin = true })

            if (isLogin) {
                LoginContent(
                    email = email,
                    phoneNumber = phoneNumber,
                    password = password,
                    onEmailChange = {
                        email = it
                    },
                    onPhoneNumberChange = {
                        phoneNumber = it
                    },
                    onPasswordChange = {
                        password = it
                    },
                    onSignUpClick = {
                        isLogin = false
                    },
                    onLoginButtonClick = {
                        navController.navigate(Screen.Home.route)
                    }
                )
            } else {
                RegisterContent(
                    fullName = fullName,
                    username = username,
                    email = email,
                    birthOfDate = birthOfDate,
                    phoneNumber = phoneNumber,
                    password = password,
                    agreeOnTerms = agreeOnTerms,
                    onFullNameChange = {
                        fullName = it
                    },
                    onUsernameChange = {
                        username = it
                    },
                    onEmailChange = {
                        email = it
                    },
                    onBirthOfDateChange = {
                        birthOfDate = it
                    },
                    onPhoneNumberChange = {
                        phoneNumber = it
                    },
                    onPasswordChange = {
                        password = it
                    },
                    onAgreeOnTermsChange = {
                        agreeOnTerms = it
                    },
                    onSignUpClick = {
                        navController.navigate(Screen.Home.route)
                    }
                )
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