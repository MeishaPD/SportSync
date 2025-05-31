package brawijaya.example.sportsync.ui.screens.findmatch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.navigation.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import brawijaya.example.sportsync.ui.components.DateSelector
import brawijaya.example.sportsync.ui.screens.findmatch.components.ChallengeCard
import brawijaya.example.sportsync.ui.screens.findmatch.components.SportCategoryFilters
import brawijaya.example.sportsync.ui.viewmodels.ChallengeUiState
import brawijaya.example.sportsync.ui.viewmodels.ChallengeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMatchScreen(
    navController: NavController,
    viewModel: ChallengeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
                color = Color(0xFFCDE4F4),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White, CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search",
                                tint = Color.Black
                            )
                        }

                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White, CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CalendarToday,
                                contentDescription = "Calendar",
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Find Your Opponent",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Check the newest challenges",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    DateSelector(
                        onDateSelected = { date ->
                            viewModel.selectDate(date)
                        }
                    )
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
            FindMatchContent(
                navController = navController,
                viewModel = viewModel,
                uiState = uiState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMatchContent(
    navController: NavController,
    viewModel: ChallengeViewModel,
    uiState: ChallengeUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SportCategoryFilters(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = { category ->
                viewModel.selectCategory(category)
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recent Challenges",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(uiState.filteredChallenges) { challenge ->
                ChallengeCard(
                    title = challenge.declaration,
                    organizer = "By Anonymous",
                    status = "${challenge.type} ${challenge.gender}",
                    sportIcon = R.drawable.julius_caesar,
                    onClick = {
                        navController.navigate("${Screen.DetailChallenge.route}/${challenge.id}")
                    }
                )
            }

            if (uiState.filteredChallenges.isEmpty() && !uiState.isLoading) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = "No challenges found for the selected filters",
                            modifier = Modifier.padding(24.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CreateChallenge.route) },
                containerColor = Color(0xFFFBBB46),
                modifier = Modifier.size(64.dp).clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add Challenge",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}