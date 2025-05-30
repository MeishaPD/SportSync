package brawijaya.example.sportsync.ui.screens.auth.components

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.utils.DateUtils
import java.util.Calendar

@Composable
fun RegisterContent(
    fullName: String,
    username: String,
    email: String,
    birthOfDate: String,
    phone: String,
    password: String,
    agreeOnTerms: Boolean,
    onFullNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBirthOfDateChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
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
            value = phone,
            onValueChange = onPhoneChange,
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