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
fun RegisterContent(
    fullName: String,
    username: String,
    email: String,
    birthOfDate: String,
    phoneNumber: String,
    password: String,
    agreeOnTerms: Boolean,
    onFullNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBirthOfDateChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onAgreeOnTermsChange: (Boolean) -> Unit,
    onSignUpClick: () -> Unit
) {

    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val dateInteractionSource = remember { MutableInteractionSource() }


    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = DateUtils.formatDate(year, month, dayOfMonth)
            onBirthOfDateChange(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(dateInteractionSource) {
        dateInteractionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    datePickerDialog.show()
                }
            }
        }
    }

    Row(
        modifier = Modifier.padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Full Name",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                shape = RoundedCornerShape(50.dp),
                singleLine = true
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Username",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                shape = RoundedCornerShape(50.dp),
                singleLine = true
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Email",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            shape = RoundedCornerShape(50.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Birth of Date",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = birthOfDate,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(50.dp),
            singleLine = true,
            interactionSource = dateInteractionSource,
            trailingIcon = {
                IconButton(
                    onClick = {
                        datePickerDialog.show()
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarToday,
                        contentDescription = "Pilih Tanggal Lahir",
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable {
                    datePickerDialog.show()
                },
        )

        Text(
            text = "Phone Number",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            shape = RoundedCornerShape(50.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Password",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            shape = RoundedCornerShape(50.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = if (passwordVisible) "Sembunyikan Password" else "Tampilkan Password",
                    )
                }
            }
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Checkbox(
            checked = agreeOnTerms,
            onCheckedChange = onAgreeOnTermsChange,
            modifier = Modifier.scale(0.8f)
        )
        Text(
            text = "I agree to the Terms & Conditions and Privacy Policy",
            fontSize = 11.sp,
            maxLines = 1
        )
    }

    Button(
        onClick = {
            onSignUpClick()
        },
        border = BorderStroke(width = 1.dp, color = Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFCCD78),
            contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(60.dp)
    ) {
        Text(
            text = "Sign Up",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoginContent(
    email: String,
    phoneNumber: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onLoginButtonClick: () -> Unit
) {

    var isEmailLogin by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = !isEmailLogin,
                onClick = { isEmailLogin = false },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFFFAA916)
                ),
            )
            Text(
                text = "Phone Number",
                modifier = Modifier.padding(end = 32.dp),
                fontSize = 14.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isEmailLogin,
                onClick = { isEmailLogin = true },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFFFAA916)
                )
            )
            Text(
                text = "Email",
                fontSize = 14.sp
            )
        }
    }

    Column {
        Text(
            text = if (isEmailLogin) "Email" else "Phone Number",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = if (isEmailLogin) email else phoneNumber,
            onValueChange = {
                if (isEmailLogin) onEmailChange else onPhoneNumberChange
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(50.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = if (isEmailLogin) KeyboardType.Email else KeyboardType.Phone)
        )

        Text(
            text = "Password",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            shape = RoundedCornerShape(50.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                        contentDescription = if (passwordVisible) "Sembunyikan Password" else "Tampilkan Password",
                    )
                }
            }
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = rememberMe,
                onClick = { rememberMe = !rememberMe },
                modifier = Modifier.scale(0.75f)
            )
            Text(
                text = "Remember me",
                fontSize = 12.sp
            )
        }

        TextButton(
            onClick = {}
        ) {
            Text(
                text = "Forget password?",
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }

    Button(
        onClick = {
            onLoginButtonClick()
        },
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFCCD78),
            contentColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth().height(60.dp)
    ) {
        Text(
            text = "Log In",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp)
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Or Log In With",
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(42.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFCCD78),
                contentColor = Color.Black
            ),
            modifier = Modifier.height(40.dp).weight(1f),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Google logo",
                tint = Color.Unspecified
            )
            Text("Google")
        }
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFCCD78),
                contentColor = Color.Black
            ),
            modifier = Modifier.height(40.dp).weight(1f),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.facebook_icon),
                contentDescription = "Facebook logo",
                tint = Color.Unspecified
            )
            Text("Facebook")
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(
            text = "Don't have an account?"
        )
        TextButton(
            onClick = {
                onSignUpClick()
            }
        ) {
            Text(
                text = "Sign Up",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
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