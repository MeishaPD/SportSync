package brawijaya.example.sportsync.ui.screens.paymentsuccess

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.ui.screens.paymentsuccess.components.PaymentSuccessContent
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PaymentSuccessScreen(
    navController: NavController,
    orderId: String,
    totalAmount: Int,
    courtName: String = "SM Futsal, Jln Sudimoro",
    bookingDate: String = "14 November 2025",
    bookingTime: String = "10:00 - 10:59"
) {
    val currentDateTime = SimpleDateFormat("d MMMM yyyy HH:mm", Locale.getDefault()).format(Date())

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
            PaymentSuccessContent(
                navController = navController,
                orderId = orderId,
                totalAmount = totalAmount,
                courtName = courtName,
                bookingDate = bookingDate,
                bookingTime = bookingTime,
                currentDateTime = currentDateTime
            )
        }
    }
}