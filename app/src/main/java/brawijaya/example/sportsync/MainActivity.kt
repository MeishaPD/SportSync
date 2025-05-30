package brawijaya.example.sportsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import brawijaya.example.sportsync.ui.navigation.AppNavigation
import brawijaya.example.sportsync.ui.theme.SportSyncTheme
import brawijaya.example.sportsync.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()

        var keepSplashScreen = true
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }

        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(1000)

            authViewModel.authState.collect { authState ->
                if (!authState.isLoading) {
                    keepSplashScreen = false
                    return@collect
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            SportSyncTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}