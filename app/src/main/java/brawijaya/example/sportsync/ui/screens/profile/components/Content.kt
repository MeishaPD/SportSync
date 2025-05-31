package brawijaya.example.sportsync.ui.screens.profile.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.R

@Composable
fun ProfileContent() {

    val username = "juliancaesarr"
    val joinedDate = "October 2025"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Julian Caesar",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Text(
            text = "@$username • Joined $joinedDate",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_football),
                        contentDescription = "Football",
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(20.dp)
                            .background(Color(0xFFFCCD78))
                    ) {
                        Text(
                            text = "+2",
                            fontSize = 10.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Text(
                    text = "Community",
                    fontSize = 16.sp
                )
            }

            VerticalDivider(
                thickness = 1.dp,
                color = Color.Black,
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 22.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "15",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Following",
                    fontSize = 16.sp
                )
            }

            VerticalDivider(
                thickness = 1.dp,
                color = Color.Black,
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 22.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "32",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Followers",
                    fontSize = 16.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFCCD78),
                    contentColor = Color.Black
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonAddAlt,
                    contentDescription = "Add Friends"
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "ADD FRIENDS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFFCCD78),
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = "Share"
                )
            }
        }

        Text(
            text = "Overview",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .width(125.dp)
                    .height(45.dp)
                    .background(Color(0xFFFCCD78))
            )

            Spacer(Modifier.width(52.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .width(125.dp)
                    .height(45.dp)
                    .background(Color(0xFFFCCD78))
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .width(125.dp)
                .height(45.dp)
                .background(Color(0xFFFCCD78))
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Leaderboard",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .width(125.dp)
                    .height(45.dp)
                    .background(Color(0xFFFCCD78))
            )

            Spacer(Modifier.width(52.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .width(125.dp)
                    .height(45.dp)
                    .background(Color(0xFFFCCD78))
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .width(125.dp)
                .height(45.dp)
                .background(Color(0xFFFCCD78))
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Latest Activites",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(25.dp))
                .fillMaxWidth()
                .height(45.dp)
                .background(Color(0xFFFCCD78))
        )
    }
}