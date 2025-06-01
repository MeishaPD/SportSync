package brawijaya.example.sportsync.ui.screens.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.data.models.DummyData
import brawijaya.example.sportsync.data.models.ProfileState
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.components.LatestActivity
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.ui.screens.home.components.BigUpcomingEventsCard
import brawijaya.example.sportsync.ui.screens.home.components.DailyQuestCard
import brawijaya.example.sportsync.ui.screens.home.components.MainUpcomingEventsCard
import brawijaya.example.sportsync.ui.viewmodels.ProfileViewModel
import brawijaya.example.sportsync.utils.LocationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val profileState by profileViewModel.profileState.collectAsState()

    var showUpComingEvents by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    var hasCheckedPermission by remember { mutableStateOf(false) }

    val locationManager = remember { LocationManager(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = granted
    }

    LaunchedEffect(Unit) {
        hasLocationPermission = locationManager.hasLocationPermission()
        hasCheckedPermission = true

        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(hasCheckedPermission) {
        if (hasCheckedPermission && !hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
                color = Color(0xFFCDE4F4)
            ) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(75.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.julius_caesar),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.clip(CircleShape)
                            )
                        }
                    },
                    title = {
                        Column {
                            Text(
                                text = "Hello,\n${profileState.profile.fullName.ifEmpty { "User" }}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(16.dp),
                    actions = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .scale(1.5f)
                                .padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Notifications,
                                contentDescription = "Notification"
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            BottomNavigation(
                currentRoute = Screen.Home.route,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HomeScreenContent(
                profileState = profileState,
                showUpComingEvents = showUpComingEvents,
                onToggleShowUpComingEvents = { showUpComingEvents = !showUpComingEvents }
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    profileState: ProfileState,
    showUpComingEvents: Boolean,
    onToggleShowUpComingEvents: () -> Unit
) {
    val userProfile = profileState.profile

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

    if (profileState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Level ${userProfile.level}",
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

        if (showUpComingEvents) {
            UpComingEventsContent(
                showUpComingEvents = onToggleShowUpComingEvents
            )
        } else {
            MainHomeScreenContent(
                showUpComingEvents = onToggleShowUpComingEvents
            )
        }
    }
}

@Composable
fun UpComingEventsContent(
    showUpComingEvents: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Events",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            IconButton(
                onClick = showUpComingEvents
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Back",
                    modifier = Modifier.rotate(90f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(14.dp)
        ) {
            items(DummyData.upcomingEvents) { event ->
                BigUpcomingEventsCard(event = event)
            }
        }
    }
}

@Composable
fun MainHomeScreenContent(
    showUpComingEvents: () -> Unit
) {
    Text(
        text = "Daily Quest",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 24.dp, top = 28.dp, bottom = 8.dp)
    )

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 24.dp)
    ) {
        items(DummyData.dailyQuests) { quest ->
            DailyQuestCard(quest = quest)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Events",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            IconButton(
                onClick = showUpComingEvents
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "View All"
                )
            }
        }

        TextButton(
            onClick = showUpComingEvents,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.Transparent
            )
        ) {
            Text("View All")
        }
    }

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 24.dp)
    ) {
        items(DummyData.upcomingEvents.take(5)) { event ->
            MainUpcomingEventsCard(event = event)
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 28.dp)
    ) {
        Text(
            text = "Latest Activities",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        DummyData.latestActivities.take(3).forEach { activity ->
            LatestActivity(activity = activity)
        }
    }
}