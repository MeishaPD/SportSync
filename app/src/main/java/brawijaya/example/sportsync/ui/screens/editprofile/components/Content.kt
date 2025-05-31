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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.ui.screens.editprofile.EditProfileScreen

@Composable
fun EditProfileContent(

) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
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
                Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Julian Caesar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "juliancaesar@gmail.com",
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD65C71),
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text(
                    text = "Cancel",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))

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
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "First Name",
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Black,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                focusedContainerColor = Color.White
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.width(140.dp).height(40.dp)
                        )

                    }

                    Column {
                        Text(
                            text = "Last Name",
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Black,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                focusedContainerColor = Color.White
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.width(140.dp).height(40.dp)
                        )
                    }
                }

                Text(
                    text = "Age",
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.width(140.dp).height(40.dp)
                )

                Text(
                    text = "Address",
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                )

                Text(
                    text = "Email",
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                )

                Text(
                    text = "Phone Number",
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                )

                Text(
                    text = "Favorite Sport",
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                )

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color(0xFFFCCD78)
                    ),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text(
                        text = "Confirm",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    EditProfileScreen(
        navController = rememberNavController()
    )
}