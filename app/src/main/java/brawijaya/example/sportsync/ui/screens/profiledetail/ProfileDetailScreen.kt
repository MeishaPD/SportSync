package brawijaya.example.sportsync.ui.screens.profiledetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.ui.screens.profiledetail.components.ProfileDetailContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
                color = Color(0xFFCDE4F4)
            ) {
                TopAppBar(
                    title = {
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = "Julius Caesar",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "juliancaesar@gmail.com",
                                fontSize = 16.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(50.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.julius_caesar),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.clip(CircleShape)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(24.dp)
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
            ProfileDetailContent(
                onViewAll = {
                    navController.navigate(Screen.FriendList.route)
                }
            )
        }
    }
}