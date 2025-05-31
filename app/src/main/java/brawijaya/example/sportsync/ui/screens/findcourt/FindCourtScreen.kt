package brawijaya.example.sportsync.ui.screens.findcourt

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.components.DateSelector
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.ui.screens.findcourt.components.CourtCard
import brawijaya.example.sportsync.ui.screens.findcourt.components.CourtCategoryFilters
import brawijaya.example.sportsync.ui.viewmodels.CourtViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun FindCourtScreen(
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
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

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Find Your Court",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Book available court slots",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    FindCourtDateSelector()
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                currentRoute = Screen.FindCourt.route,
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
            FindCourtContent(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
private fun FindCourtDateSelector(
    viewModel: CourtViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    DateSelector(
        onDateSelected = { dateString ->
            viewModel.onDateSelected(dateString)
        }
    )
}

@Composable
fun FindCourtContent(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: CourtViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Retry",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.retry()
            }
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CourtCategoryFilters(
            categories = uiState.categories,
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = { category ->
                viewModel.onCategorySelected(category)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Available Courts",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            if (uiState.displayDate.isNotEmpty()) {
                Text(
                    text = uiState.displayDate,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Loading courts...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else if (uiState.courts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "No courts available",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Try selecting a different date or category",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.courts.size) { index ->
                    val court = uiState.courts[index]
                    CourtCard(
                        court = court,
                        onTimeSlotSelected = { courtId, timeSlot ->
                            val encodedCourtName = URLEncoder.encode(
                                court.name,
                                StandardCharsets.UTF_8.toString()
                            )

                            navController.navigate("book_court/$encodedCourtName?timeSlot=$timeSlot&courtId=$courtId&date=${uiState.selectedDate}")
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}