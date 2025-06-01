package brawijaya.example.sportsync.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.data.models.DummyData
import brawijaya.example.sportsync.ui.components.LatestActivity
import brawijaya.example.sportsync.ui.viewmodels.ProfileViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun ProfileContent(
    profileViewModel: ProfileViewModel
) {
    val profileState by profileViewModel.profileState.collectAsState()
    val profile = profileState.profile

    val formattedJoinedDate = try {
        if (profile.joinedDate.isNotEmpty()) {
            val dateTime =
                LocalDateTime.parse(profile.joinedDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            dateTime.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        } else {
            "Unknown"
        }
    } catch (e: DateTimeParseException) {
        try {
            val instant = java.time.Instant.parse(profile.joinedDate)
            val dateTime = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
            dateTime.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        } catch (e2: Exception) {
            "Unknown"
        }
    }

    if (profileState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    profileState.error?.let { error ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = { profileViewModel.refreshProfile() }) {
                    Text("Retry")
                }
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {
        val displayName = when {
            profile.fullName.isNotEmpty() -> profile.fullName
            profile.firstName.isNotEmpty() || profile.lastName.isNotEmpty() ->
                "${profile.firstName} ${profile.lastName}".trim()

            else -> "User"
        }

        Text(
            text = displayName,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        val usernameText =
            if (profile.username.isNotEmpty()) "@${profile.username}" else "No username"
        Text(
            text = "$usernameText • Joined $formattedJoinedDate",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            text = "+${
                                if (profile.gamesPlayed > 0) (profile.gamesPlayed / 10).coerceAtLeast(
                                    1
                                ) else 1
                            }",
                            fontSize = 10.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                Text(text = "Community", fontSize = 16.sp)
            }

            VerticalDivider(
                thickness = 1.dp,
                color = Color.Black,
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 22.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${profile.followingCount}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Following", fontSize = 16.sp)
            }

            VerticalDivider(
                thickness = 1.dp,
                color = Color.Black,
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 22.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${profile.followersCount}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Followers", fontSize = 16.sp)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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
                Icon(imageVector = Icons.Outlined.PersonAddAlt, contentDescription = "Add Friends")
                Spacer(Modifier.width(8.dp))
                Text(text = "ADD FRIENDS", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(Modifier.width(12.dp))

            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFFCCD78),
                    contentColor = Color.Black
                )
            ) {
                Icon(imageVector = Icons.Rounded.Share, contentDescription = "Share")
            }
        }

        Text(text = "Overview", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            OverviewItem(R.drawable.ic_fire, "${profile.streak}", "Week Streak")
            Spacer(Modifier.width(52.dp))
            OverviewItem(R.drawable.ic_lightning, "${profile.xp}", "Total XP")
        }

        OverviewItem(R.drawable.ic_trophy, profile.rank.ifEmpty { "Unranked" }, "Rank")

        Spacer(Modifier.height(16.dp))

        Text(text = "Leaderboard", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            OverviewItem(R.drawable.ic_fire, "${profile.streak}", "Week Streak")
            Spacer(Modifier.width(52.dp))
            OverviewItem(R.drawable.ic_lightning, "${profile.xp}", "Total XP")
        }

        OverviewItem(R.drawable.ic_trophy, profile.rank.ifEmpty { "Unranked" }, "Rank")

        Spacer(Modifier.height(16.dp))

        Text(text = "Latest Activities", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        DummyData.latestActivities.take(1).forEach { activity ->
            LatestActivity(activity = activity)
        }
    }
}

@Composable
fun OverviewItem(iconRes: Int, value: String, label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .width(125.dp)
            .height(45.dp)
            .background(Color(0xFFFCCD78))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = value,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                Text(
                    text = label,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}