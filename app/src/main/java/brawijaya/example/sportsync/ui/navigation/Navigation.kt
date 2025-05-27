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
import java.net.URLDecoder
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
            val encodedCourtName = backStackEntry.arguments?.getString("courtName") ?: ""
            val courtName = URLDecoder.decode(encodedCourtName, StandardCharsets.UTF_8.toString())
            val timeSlot = backStackEntry.arguments?.getString("timeSlot")

            BookCourtScreen(
                navController = navController,
                courtName = courtName,
                timeSlot = timeSlot
            )
        }
    }
}