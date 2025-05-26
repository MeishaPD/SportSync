package brawijaya.example.sportsync.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import brawijaya.example.sportsync.ui.screens.auth.AuthScreen
import brawijaya.example.sportsync.ui.screens.creatematch.CreateMatchScreen
import brawijaya.example.sportsync.ui.screens.findmatch.FindMatchScreen
import brawijaya.example.sportsync.ui.screens.gamezone.GameZoneScreen
import brawijaya.example.sportsync.ui.screens.home.HomeScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Auth : Screen("auth")
    object GameZone: Screen("gamezone")
    object FindMatch: Screen("find_match")
    object CreateMatch: Screen("create_match")
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
        composable(Screen.CreateMatch.route) {
            CreateMatchScreen(navController = navController)
        }
    }
}