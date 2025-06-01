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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import brawijaya.example.sportsync.data.models.DummyData
import brawijaya.example.sportsync.data.models.UserProfile
import brawijaya.example.sportsync.ui.components.LatestActivity
import brawijaya.example.sportsync.ui.screens.profiledetail.ProfileDetailScreen

@Composable
fun ProfileDetailContent(
    userProfile: UserProfile,
    onViewAll: () -> Unit,
    onRefresh: () -> Unit = {}
) {
    val progress by remember(userProfile.xp, userProfile.level) {
        derivedStateOf {
            if (userProfile.level > 0) {
                val baseXpPerLevel = 250
                val xpForCurrentLevel = userProfile.level * baseXpPerLevel
                val xpForNextLevel = (userProfile.level + 1) * baseXpPerLevel
                val currentLevelProgress = (userProfile.xp - xpForCurrentLevel).toFloat()
                val levelXpRange = (xpForNextLevel - xpForCurrentLevel).toFloat()

                if (levelXpRange > 0) {
                    (currentLevelProgress / levelXpRange).coerceIn(0f, 1f)
                } else {
                    0f
                }
            } else {
                0f
            }
        }
    }

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
                        text = if (userProfile.level > 0) "Level ${userProfile.level}" else "Level 1",
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

                    Text(
                        text = if (userProfile.xp > 0) "${userProfile.xp} XP" else "0 XP",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
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
                        Text(text = ": ${userProfile.fullName.ifEmpty { "Not set" }}")
                    }
                    Row {
                        Text(
                            text = "Age",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": ${if (userProfile.age > 0) userProfile.age else "Not set"}")
                    }
                    Row {
                        Text(
                            text = "Address",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": ${userProfile.address.ifEmpty { "Not set" }}")
                    }
                    Row {
                        Text(
                            text = "Phone Number",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": ${userProfile.phone.ifEmpty { "Not set" }}")
                    }
                    Row {
                        Text(
                            text = "Favorite Sport",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": ${userProfile.favoriteSport.ifEmpty { "Not set" }}")
                    }
                    Row {
                        Text(
                            text = "Match Played",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(labelWidth)
                        )
                        Text(text = ": ${userProfile.gamesPlayed}")
                    }
                    if (userProfile.rank.isNotEmpty()) {
                        Row {
                            Text(
                                text = "Rank",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.width(labelWidth)
                            )
                            Text(text = ": ${userProfile.rank}")
                        }
                    }
                    if (userProfile.streak > 0) {
                        Row {
                            Text(
                                text = "Streak",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.width(labelWidth)
                            )
                            Text(text = ": ${userProfile.streak}")
                        }
                    }
                }
            }
        }

        // Statistics Section
        if (userProfile.wins > 0 || userProfile.losses > 0) {
            item {
                Text(
                    text = "Statistics",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFE8F5E8),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${userProfile.wins}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                            Text(
                                text = "Wins",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${userProfile.losses}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE57373)
                            )
                            Text(
                                text = "Losses",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val winRate = if (userProfile.wins + userProfile.losses > 0) {
                                (userProfile.wins.toFloat() / (userProfile.wins + userProfile.losses) * 100).toInt()
                            } else 0
                            Text(
                                text = "$winRate%",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2196F3)
                            )
                            Text(
                                text = "Win Rate",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Latest Match",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        item {
            DummyData.latestActivities.take(3).forEach { activity ->
                LatestActivity(activity = activity)
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Friend List",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    if (userProfile.followersCount > 0 || userProfile.followingCount > 0) {
                        Text(
                            text = "Following: ${userProfile.followingCount} • Followers: ${userProfile.followersCount}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.rdj_profile),
                            contentDescription = "Friend Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(60.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Friend ${it + 1}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "friend${it + 1}@example.com",
                                fontSize = 14.sp,
                            )
                        }
                    }

                    Text(
                        text = "Online",
                        fontSize = 12.sp,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp)
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