package brawijaya.example.sportsync.ui.screens.paymentsuccess

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.ui.screens.paymentsuccess.components.PaymentSuccessContent
import brawijaya.example.sportsync.ui.viewmodels.PaymentSuccessViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PaymentSuccessScreen(
    navController: NavController,
    bookingId: String,
    totalAmount: Int,
    viewModel: PaymentSuccessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentDateTime = SimpleDateFormat("d MMMM yyyy HH:mm", Locale.getDefault()).format(Date())

    // Fetch booking details when screen loads
    LaunchedEffect(bookingId) {
        viewModel.loadBookingDetails(bookingId)
    }

    Scaffold(
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
                color = Color(0xFFCDE4F4),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp)
                        .padding(24.dp),
                ) {}
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
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFFCCD78)
                        )
                    }
                }
                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Error loading booking details",
                                color = Color.Red
                            )
                            Text(
                                text = uiState.error!!,
                                color = Color.Gray
                            )
                            Button(
                                onClick = { viewModel.loadBookingDetails(bookingId) }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                uiState.booking != null -> {
                    PaymentSuccessContent(
                        navController = navController,
                        orderId = bookingId,
                        totalAmount = uiState.booking!!.totalPrice.toInt(),
                        courtName = uiState.courtName ?: "Court",
                        bookingDate = formatBookingDate(uiState.booking!!.date),
                        bookingTime = formatTimeSlots(uiState.timeSlots),
                        currentDateTime = currentDateTime
                    )
                }
            }
        }
    }
}

private fun formatBookingDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}

private fun formatTimeSlots(timeSlots: List<String>): String {
    return if (timeSlots.isNotEmpty()) {
        timeSlots.joinToString(", ")
    } else {
        "Time not available"
    }
}