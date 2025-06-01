package brawijaya.example.sportsync.ui.screens.editprofile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.R
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun EditProfileContent(
    firstName: String,
    lastName: String,
    age: String,
    address: String,
    phone: String,
    favoriteSport: String,
    isLoading: Boolean = false,
    isUpdating: Boolean = false,
    error: String? = null,
    userEmail: String = "",
    fullName: String = "",
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onFavoriteSportChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onClearError: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            onClearError()
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.size(60.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.julius_caesar),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profile Picture"
                )
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = fullName.ifEmpty { "User" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = userEmail,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD65C71),
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Black),
                enabled = !isUpdating
            ) {
                Text(
                    text = "Cancel",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Your Information",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFCDE4F4),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "First Name",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = onFirstNameChange,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Black,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                focusedContainerColor = Color.White
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isUpdating
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Last Name",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = onLastNameChange,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Black,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                focusedContainerColor = Color.White
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isUpdating
                        )
                    }
                }

                Column {
                    Text(
                        text = "Age",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = age,
                        onValueChange = { newAge ->
                            if (newAge.isEmpty() || newAge.all { it.isDigit() }) {
                                onAgeChange(newAge)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.width(140.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        enabled = !isUpdating
                    )
                }

                Column {
                    Text(
                        text = "Address",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = address,
                        onValueChange = onAddressChange,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isUpdating
                    )
                }

                Column {
                    Text(
                        text = "Phone Number",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = onPhoneChange,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        enabled = !isUpdating
                    )
                }

                Column {
                    Text(
                        text = "Favorite Sport",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = favoriteSport,
                        onValueChange = onFavoriteSportChange,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isUpdating
                    )
                }

                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color(0xFFFCCD78)
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    enabled = !isUpdating && !isLoading
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Black
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = if (isUpdating) "Updating..." else "Confirm",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Loading profile...",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "Confirm Changes",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Are you sure you want to save these changes to your profile?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onConfirm()
                    }
                ) {
                    Text("Save", color = Color(0xFFFCCD78))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    SnackbarHost(hostState = snackbarHostState)
}

@Preview(showBackground = true)
@Composable
fun EditProfileContentPreview() {
    EditProfileContent(
        firstName = "John",
        lastName = "Doe",
        age = "25",
        address = "123 Main St",
        phone = "1234567890",
        favoriteSport = "Football",
        userEmail = "john.doe@example.com",
        fullName = "John Doe",
        onFirstNameChange = {},
        onLastNameChange = {},
        onAgeChange = {},
        onAddressChange = {},
        onPhoneChange = {},
        onFavoriteSportChange = {},
        onConfirm = {},
        onCancel = {},
        onClearError = {}
    )
}