package brawijaya.example.sportsync.ui.screens.gamezone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameZoneScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
                color = Color(0xFFCDE4F4)
            ) {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "GameZone",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(16.dp),
                    actions = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Notification"
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            BottomNavigation(
                currentRoute = Screen.GameZone.route,
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
            GameZoneContent()
        }
    }
}

@Composable
fun GameZoneContent() {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = "Tournament",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Text(
            text = "Check the newest schedule",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .background(Color.Gray)
            )
            Spacer(Modifier.size(8.dp))
            repeat(3) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .width(90.dp)
                        .height(50.dp)
                        .background(Color.Gray)
                )

                Spacer(Modifier.size(14.dp))
            }
        }

        Text(
            text = "Tournaments Schedule",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "— Mid-year Tournament 2025",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            items(12) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.Gray)
                        .fillMaxWidth()
                        .height(70.dp)
                )

                Spacer(Modifier.size(16.dp))
            }
        }

    }
}