package brawijaya.example.sportsync.ui.screens.friendlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.ui.screens.friendlist.components.FriendListContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendListScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
                color = Color(0xFFCDE4F4)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Social",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(0xFFFCCD78),
                                contentColor = Color(0xFFCDE4F4)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigation(
                currentRoute = Screen.Profile.route,
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
            FriendListContent()
        }
    }
}