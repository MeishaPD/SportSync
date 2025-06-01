package brawijaya.example.sportsync.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import brawijaya.example.sportsync.ui.screens.auth.AuthScreen
import brawijaya.example.sportsync.ui.screens.bookcourt.BookCourtScreen
import brawijaya.example.sportsync.ui.screens.createchallenge.CreateChallengeScreen
import brawijaya.example.sportsync.ui.screens.detailchallenge.DetailChallengeScreen
import brawijaya.example.sportsync.ui.screens.editprofile.EditProfileScreen
import brawijaya.example.sportsync.ui.screens.findcourt.FindCourtScreen
import brawijaya.example.sportsync.ui.screens.findmatch.FindMatchScreen
import brawijaya.example.sportsync.ui.screens.frienddetail.FriendDetailScreen
import brawijaya.example.sportsync.ui.screens.friendlist.FriendListScreen
import brawijaya.example.sportsync.ui.screens.gamezone.GameZoneScreen
import brawijaya.example.sportsync.ui.screens.home.HomeScreen
import brawijaya.example.sportsync.ui.screens.onboarding.OnBoardingScreen
import brawijaya.example.sportsync.ui.screens.payment.PaymentScreen
import brawijaya.example.sportsync.ui.screens.paymentdetail.PaymentDetailScreen
import brawijaya.example.sportsync.ui.screens.paymentsuccess.PaymentSuccessScreen
import brawijaya.example.sportsync.ui.screens.profile.ProfileScreen
import brawijaya.example.sportsync.ui.screens.profiledetail.ProfileDetailScreen
import brawijaya.example.sportsync.ui.viewmodels.AuthViewModel
import brawijaya.example.sportsync.utils.NavigationUtils.parsePaymentParams
import java.net.URLDecoder
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
    object BookCourt: Screen("book_court/{courtId}/{courtName}/{address}/{pricePerHour}/{date}?timeSlot={timeSlot}") {
        fun createRoute(
            courtId: String,
            courtName: String,
            address: String,
            pricePerHour: String,
            date: String,
            timeSlot: String? = null
        ): String {
            val encodedCourtId = URLEncoder.encode(courtId, StandardCharsets.UTF_8.toString())
            val encodedCourtName = URLEncoder.encode(courtName, StandardCharsets.UTF_8.toString())
            val encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString())
            val encodedPricePerHour = URLEncoder.encode(pricePerHour, StandardCharsets.UTF_8.toString())
            val encodedDate = URLEncoder.encode(date, StandardCharsets.UTF_8.toString())

            return if (timeSlot != null) {
                val encodedTimeSlot = URLEncoder.encode(timeSlot, StandardCharsets.UTF_8.toString())
                "book_court/$encodedCourtId/$encodedCourtName/$encodedAddress/$encodedPricePerHour/$encodedDate?timeSlot=$encodedTimeSlot"
            } else {
                "book_court/$encodedCourtId/$encodedCourtName/$encodedAddress/$encodedPricePerHour/$encodedDate"
            }
        }
    }
    object Profile: Screen("profile")
    object ProfileDetail: Screen("profile_detail")
    object EditProfile: Screen("edit_profile")
    object FriendList: Screen("friend_list")
    object FriendDetail: Screen("friend_detail")
    object Payment: Screen("payment/{courtId}/{courtName}/{selectedDate}/{paymentType}/{totalAmount}?timeSlots={timeSlots}&courtAddress={courtAddress}&pricePerHour={pricePerHour}&availableTimeSlots={availableTimeSlots}") {
        fun createRoute(
            courtId: String,
            courtName: String,
            selectedDate: String,
            paymentType: String,
            totalAmount: Int,
            timeSlots: String,
            courtAddress: String = "",
            pricePerHour: String = "",
            availableTimeSlots: String = "[]"
        ): String {
            val encodedCourtId = URLEncoder.encode(courtId, StandardCharsets.UTF_8.toString())
            val encodedCourtName = URLEncoder.encode(courtName, StandardCharsets.UTF_8.toString())
            val encodedDate = URLEncoder.encode(selectedDate, StandardCharsets.UTF_8.toString())
            val encodedTimeSlots = URLEncoder.encode(timeSlots, StandardCharsets.UTF_8.toString())
            val encodedAddress = URLEncoder.encode(courtAddress, StandardCharsets.UTF_8.toString())
            val encodedPrice = URLEncoder.encode(pricePerHour, StandardCharsets.UTF_8.toString())
            val encodedAvailableTimeSlots = URLEncoder.encode(availableTimeSlots, StandardCharsets.UTF_8.toString())

            return "payment/$encodedCourtId/$encodedCourtName/$encodedDate/$paymentType/$totalAmount?timeSlots=$encodedTimeSlots&courtAddress=$encodedAddress&pricePerHour=$encodedPrice&availableTimeSlots=$encodedAvailableTimeSlots"
        }
    }
    object PaymentDetail: Screen("payment_detail/{bookingId}/{totalAmount}") {
        fun createRoute(bookingId: String, totalAmount: Int): String {
            return "payment_detail/$bookingId/$totalAmount"
        }
    }
    object PaymentSuccess: Screen("payment_success/{bookingId}/{totalAmount}") {
        fun createRoute(
            bookingId: String,
            totalAmount: Int
        ): String {
            return "payment_success/$bookingId/$totalAmount"
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    if (!authState.isInitialized) {
        return
    }

    val startDestination = when {
        authState.isAuthenticated -> Screen.Home.route
        else -> Screen.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnBoardingScreen(navController = navController)
        }
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
        composable(
            route = "${Screen.DetailChallenge.route}/{challengeId}",
            arguments = listOf(navArgument("challengeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val challengeId = backStackEntry.arguments?.getString("challengeId") ?: ""
            DetailChallengeScreen(
                navController = navController,
                challengeId = challengeId
            )
        }
        composable(Screen.FindCourt.route) {
            FindCourtScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.ProfileDetail.route) {
            ProfileDetailScreen(navController = navController)
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }
        composable(Screen.FriendList.route) {
            FriendListScreen(navController = navController)
        }
        composable(Screen.FriendDetail.route) {
            FriendDetailScreen(navController = navController)
        }
        composable(
            route = Screen.BookCourt.route,
            arguments = listOf(
                navArgument("courtId") { type = NavType.StringType },
                navArgument("courtName") { type = NavType.StringType },
                navArgument("address") { type = NavType.StringType },
                navArgument("pricePerHour") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("timeSlot") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val courtId = URLDecoder.decode(
                backStackEntry.arguments?.getString("courtId") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val courtName = URLDecoder.decode(
                backStackEntry.arguments?.getString("courtName") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val address = URLDecoder.decode(
                backStackEntry.arguments?.getString("address") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val pricePerHour = URLDecoder.decode(
                backStackEntry.arguments?.getString("pricePerHour") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val date = URLDecoder.decode(
                backStackEntry.arguments?.getString("date") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            val timeSlot = backStackEntry.arguments?.getString("timeSlot")?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }

            BookCourtScreen(
                navController = navController,
                courtId = courtId,
                courtName = courtName,
                address = address,
                pricePerHour = pricePerHour,
                date = date,
                timeSlot = timeSlot
            )
        }
        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument("courtId") { type = NavType.StringType },
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
                navArgument("bookingId") { type = NavType.StringType },
                navArgument("totalAmount") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            val totalAmount = backStackEntry.arguments?.getInt("totalAmount") ?: 0

            PaymentDetailScreen(
                navController = navController,
                bookingId = bookingId,
                totalAmount = totalAmount
            )
        }
        composable(
            route = Screen.PaymentSuccess.route,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType },
                navArgument("totalAmount") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            val totalAmount = backStackEntry.arguments?.getInt("totalAmount") ?: 0

            PaymentSuccessScreen(
                navController = navController,
                bookingId = bookingId,
                totalAmount = totalAmount
            )
        }
    }
}