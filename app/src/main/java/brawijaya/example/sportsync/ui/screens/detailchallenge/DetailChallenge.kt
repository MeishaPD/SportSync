package brawijaya.example.sportsync.ui.screens.detailchallenge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.navigation.Screen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import brawijaya.example.sportsync.ui.components.ReadOnlyTextField

@Composable
fun DetailChallengeScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
                color = Color(0xFFCDE4F4),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp)
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White, CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "Detail Challenge",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp),
                        contentAlignment = Alignment.TopEnd,
                    ) {}
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                currentRoute = Screen.FindMatch.route,
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
            DetailChallengeContent()
        }
    }
}

@Composable
fun DetailChallengeContent(
    title: String = "Main ga Santai Bang",
    challengeDeclaration: String = "Badmin 3v2",
    sportCategory: String = "Badminton",
    gender: String = "Man",
    matchType: String = "Single",
    date: String = "25/12/2024",
    time: String = "14:30",
    description: String = "Kalah jual ginjal"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 28.sp
        )

        ReadOnlyTextField(
            label = "Challenge Declaration",
            value = challengeDeclaration
        )

        ReadOnlyTextField(
            label = "Sport Category",
            value = sportCategory
        )

        ReadOnlyTextField(
            label = "Gender",
            value = gender
        )

        ReadOnlyTextField(
            label = "Match Type",
            value = matchType
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReadOnlyTextField(
                label = "Date",
                value = date,
                modifier = Modifier.weight(1f)
            )

            ReadOnlyTextField(
                label = "Time",
                value = time,
                modifier = Modifier.weight(1f)
            )
        }

        ReadOnlyTextField(
            label = "Description",
            value = description,
            modifier = Modifier.height(120.dp),
            shape = RoundedCornerShape(15.dp),
            maxLines = Int.MAX_VALUE
        )

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp).border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(28.dp)
                ),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFBBB46)
            )
        ) {
            Text(
                text = "Accept",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
