package brawijaya.example.sportsync.ui.screens.frienddetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import brawijaya.example.sportsync.ui.screens.frienddetail.components.FriendDetailContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendDetailScreen(
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
                                text = "Robert Downey Jr.",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "robertdowneyjr@gmail.com",
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
                                painter = painterResource(id = R.drawable.rdj_profile),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.clip(CircleShape)
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {},
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(0xFFFCCD78),
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PersonRemove,
                                contentDescription = "Unfriend"
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
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            FriendDetailContent()
        }
    }
}