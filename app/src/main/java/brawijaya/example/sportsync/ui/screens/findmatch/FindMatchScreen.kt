package brawijaya.example.sportsync.ui.screens.findmatch

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.navigation.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import brawijaya.example.sportsync.ui.components.DateSelector
import brawijaya.example.sportsync.ui.screens.findmatch.components.ChallengeCard
import brawijaya.example.sportsync.ui.screens.findmatch.components.SportCategoryFilters
import brawijaya.example.sportsync.ui.viewmodels.ChallengeUiState
import brawijaya.example.sportsync.ui.viewmodels.ChallengeViewModel
import brawijaya.example.sportsync.utils.LocationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMatchScreen(
    navController: NavController,
    viewModel: ChallengeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var hasRequestedPermission by remember { mutableStateOf(false) }
    var shouldRecheckPermission by remember { mutableStateOf(true) }

    val locationManager = remember { LocationManager(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        viewModel.updateLocationPermissionStatus(hasLocationPermission)

        if (hasLocationPermission) {
            viewModel.getCurrentLocation()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initializeLocationManager(context)
    }

    LaunchedEffect(shouldRecheckPermission) {
        if (shouldRecheckPermission) {
            val currentPermissionStatus = locationManager.hasLocationPermission()
            viewModel.updateLocationPermissionStatus(currentPermissionStatus)
            shouldRecheckPermission = false

            if (currentPermissionStatus && uiState.currentLocation == null) {
                viewModel.getCurrentLocation()
            }
        }
    }

    LaunchedEffect(uiState.hasLocationPermission) {
        if (!hasRequestedPermission && !uiState.hasLocationPermission) {
            hasRequestedPermission = true
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(uiState.hasLocationPermission, uiState.currentLocation) {
        if (uiState.hasLocationPermission && uiState.currentLocation == null) {
            viewModel.getCurrentLocation()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        shouldRecheckPermission = true
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
                color = Color(0xFFCDE4F4),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White, CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search",
                                tint = Color.Black
                            )
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    if (uiState.hasLocationPermission) {
                                        viewModel.getCurrentLocation()
                                    } else {
                                        locationPermissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        if (uiState.hasLocationPermission && uiState.currentLocation != null)
                                            Color.Green.copy(alpha = 0.2f)
                                        else Color.Red.copy(alpha = 0.2f),
                                        CircleShape
                                    )
                                    .padding(8.dp)
                            ) {
                                if (uiState.isLocationLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = Color.Blue
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.LocationOn,
                                        contentDescription = "Location",
                                        tint = if (uiState.hasLocationPermission && uiState.currentLocation != null)
                                            Color.Green
                                        else Color.Red
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.White, CircleShape)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CalendarToday,
                                    contentDescription = "Calendar",
                                    tint = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Find Your Opponent",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isLocationBasedFilterEnabled && uiState.currentLocation != null) {
                                "Challenges near ${uiState.currentLocation?.locationName}"
                            } else if (!uiState.hasLocationPermission) {
                                "Enable location to find nearby challenges"
                            } else {
                                "Check the newest challenges"
                            },
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    DateSelector(
                        onDateSelected = { date ->
                            viewModel.selectDate(date)
                        }
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                currentRoute = Screen.FindMatch.route,
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
            FindMatchContent(
                navController = navController,
                viewModel = viewModel,
                uiState = uiState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMatchContent(
    navController: NavController,
    viewModel: ChallengeViewModel,
    uiState: ChallengeUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SportCategoryFilters(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = { category ->
                viewModel.selectCategory(category)
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recent Challenges",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(uiState.filteredChallenges) { challenge ->
                ChallengeCard(
                    title = challenge.declaration,
                    organizer = "By Anonymous",
                    status = "${challenge.type} ${challenge.gender}",
                    sportIcon = R.drawable.julius_caesar,
                    onClick = {
                        navController.navigate("${Screen.DetailChallenge.route}/${challenge.id}")
                    }
                )
            }

            if (uiState.filteredChallenges.isEmpty() && !uiState.isLoading) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = "No challenges found for the selected filters",
                            modifier = Modifier.padding(24.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CreateChallenge.route) },
                containerColor = Color(0xFFFBBB46),
                modifier = Modifier.size(64.dp).clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add Challenge",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}