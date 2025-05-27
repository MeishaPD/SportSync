package brawijaya.example.sportsync.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import brawijaya.example.sportsync.ui.screens.auth.AuthScreen
import brawijaya.example.sportsync.ui.screens.bookcourt.BookCourtScreen
import brawijaya.example.sportsync.ui.screens.createchallenge.CreateChallengeScreen
import brawijaya.example.sportsync.ui.screens.detailchallenge.DetailChallengeScreen
import brawijaya.example.sportsync.ui.screens.findcourt.FindCourtScreen
import brawijaya.example.sportsync.ui.screens.findmatch.FindMatchScreen
import brawijaya.example.sportsync.ui.screens.gamezone.GameZoneScreen
import brawijaya.example.sportsync.ui.screens.home.HomeScreen
import brawijaya.example.sportsync.ui.screens.payment.PaymentScreen
import brawijaya.example.sportsync.ui.screens.paymentdetail.PaymentDetailScreen
import brawijaya.example.sportsync.ui.screens.paymentsuccess.PaymentSuccessScreen
import brawijaya.example.sportsync.utils.NavigationUtils.parseBookCourtParams
import brawijaya.example.sportsync.utils.NavigationUtils.parsePaymentParams
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Auth : Screen("auth")
    object GameZone: Screen("gamezone")
    object FindMatch: Screen("find_match")
    object CreateChallenge: Screen("create_challenge")
    object DetailChallenge: Screen("detail_challenge")
    object FindCourt: Screen("find_court")
    object BookCourt: Screen("book_court/{courtName}?timeSlot={timeSlot}") {
        fun createRoute(courtName: String, timeSlot: String? = null): String {
            return if (timeSlot != null) {
                "book_court/$courtName?timeSlot=$timeSlot"
            } else {
                "book_court/$courtName"
            }
        }
    }
    object Payment: Screen("payment/{courtName}/{selectedDate}/{paymentType}/{totalAmount}?timeSlots={timeSlots}&courtAddress={courtAddress}&pricePerHour={pricePerHour}&availableTimeSlots={availableTimeSlots}") {
        fun createRoute(
            courtName: String,
            selectedDate: String,
            paymentType: String,
            totalAmount: Int,
            timeSlots: String,
            courtAddress: String = "",
            pricePerHour: String = "",
            availableTimeSlots: String = "[]"
        ): String {
            val encodedCourtName = URLEncoder.encode(courtName, StandardCharsets.UTF_8.toString())
            val encodedDate = URLEncoder.encode(selectedDate, StandardCharsets.UTF_8.toString())
            val encodedTimeSlots = URLEncoder.encode(timeSlots, StandardCharsets.UTF_8.toString())
            val encodedAddress = URLEncoder.encode(courtAddress, StandardCharsets.UTF_8.toString())
            val encodedPrice = URLEncoder.encode(pricePerHour, StandardCharsets.UTF_8.toString())
            val encodedAvailableTimeSlots = URLEncoder.encode(availableTimeSlots, StandardCharsets.UTF_8.toString())

            return "payment/$encodedCourtName/$encodedDate/$paymentType/$totalAmount?timeSlots=$encodedTimeSlots&courtAddress=$encodedAddress&pricePerHour=$encodedPrice&availableTimeSlots=$encodedAvailableTimeSlots"
        }
    }
    object PaymentDetail: Screen("payment_detail/{orderId}/{totalAmount}") {
        fun createRoute(orderId: String, totalAmount: Int): String {
            return "payment_detail/$orderId/$totalAmount"
        }
    }

    object PaymentSuccess: Screen("payment_success/{orderId}/{totalAmount}") {
        fun createRoute(
            orderId: String,
            totalAmount: Int
        ): String {
            return "payment_success/$orderId/$totalAmount"
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.GameZone.route) {
            GameZoneScreen(navController = navController)
        }
        composable(Screen.FindMatch.route) {
            FindMatchScreen(navController = navController)
        }
        composable(Screen.CreateChallenge.route) {
            CreateChallengeScreen(navController = navController)
        }
        composable(Screen.DetailChallenge.route) {
            DetailChallengeScreen(navController = navController)
        }
        composable(Screen.FindCourt.route) {
            FindCourtScreen(navController = navController)
        }
        composable(
            route = Screen.BookCourt.route,
            arguments = listOf(
                navArgument("courtName") {
                    type = NavType.StringType
                },
                navArgument("timeSlot") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val params = parseBookCourtParams(backStackEntry)

            BookCourtScreen(
                navController = navController,
                courtName = params.courtName,
                timeSlot = params.timeSlot
            )
        }

        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument("courtName") { type = NavType.StringType },
                navArgument("selectedDate") { type = NavType.StringType },
                navArgument("paymentType") { type = NavType.StringType },
                navArgument("totalAmount") { type = NavType.IntType },
                navArgument("timeSlots") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "[]"
                },
                navArgument("courtAddress") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                },
                navArgument("pricePerHour") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                },
                navArgument("availableTimeSlots") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "[]"
                }
            )
        ) { backStackEntry ->
            val params = parsePaymentParams(backStackEntry)

            PaymentScreen(
                navController = navController,
                courtName = params.courtName,
                selectedDate = params.selectedDate,
                paymentType = params.paymentType,
                totalAmount = params.totalAmount,
                selectedTimeSlots = params.selectedTimeSlots,
                courtData = params.courtData
            )
        }
        composable(
            route = Screen.PaymentDetail.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("totalAmount") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val totalAmount = backStackEntry.arguments?.getInt("totalAmount") ?: 0

            PaymentDetailScreen(
                navController = navController,
                orderId = orderId,
                totalAmount = totalAmount
            )
        }
        composable(
            route = Screen.PaymentSuccess.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("totalAmount") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val totalAmount = backStackEntry.arguments?.getInt("totalAmount") ?: 0

            PaymentSuccessScreen(
                navController = navController,
                orderId = orderId,
                totalAmount = totalAmount
            )
        }

    }
}
