package brawijaya.example.sportsync.ui.screens.profiledetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.ui.screens.profiledetail.ProfileDetailScreen

@Composable
fun ProfileDetailContent(
    onViewAll: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(1450f / 2000f) }
    val labelWidth = 120.dp

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Level 8",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFE0E0E0))
                    ) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxSize(),
                            color = Color(0xFF4CAF50),
                            trackColor = Color.Transparent
                        )
                    }
                }

                Spacer(Modifier.size(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.medal),
                    contentDescription = "Exp Medal",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(45.dp)
                )
            }
        }

        item {
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
                    Row {
                        Text(
                            text = "Full Name",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": Julius Caesar")
                    }
                    Row {
                        Text(
                            text = "Age",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": 45")
                    }
                    Row {
                        Text(
                            text = "Address",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": Rome, Italy")
                    }
                    Row {
                        Text(
                            text = "Phone Number",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": +39 123 4567")
                    }
                    Row {
                        Text(
                            text = "Favorite Sport",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": Gladiator Fights")
                    }
                    Row {
                        Text(
                            text = "Match Played",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": 15")
                    }
                }
            }
        }

        item {
            Text(
                text = "Latest Match",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        items(3) {
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFFCCD78))
            )
            Spacer(Modifier.height(4.dp))
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Friend List",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                TextButton(
                    onClick = { onViewAll() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "View All",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }

        items(3) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF5F5F5),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rdj_profile),
                        contentDescription = "RDJ Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(60.dp)
                    )
                    Column {
                        Text(
                            text = "Robert Downey Jr.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "robertdowneyjr@gmail.com",
                            fontSize = 14.sp,
                        )
                    }
                    Text(
                        text = "Last Match, 28 Okt",
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ProfileDetailScreen(
        navController = rememberNavController()
    )
}