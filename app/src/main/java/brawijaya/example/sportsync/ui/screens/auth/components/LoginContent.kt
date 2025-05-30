package brawijaya.example.sportsync.ui.screens.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.R

@Composable
fun LoginContent(
    email: String,
    phone: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onLoginButtonClick: (Boolean) -> Unit
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
            value = if (isEmailLogin) email else phone,
            onValueChange = { newValue ->
                if (isEmailLogin) {
                    onEmailChange(newValue)
                } else {
                    onPhoneChange(newValue)
                }
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
            onLoginButtonClick(isEmailLogin)
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
