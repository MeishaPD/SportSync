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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import brawijaya.example.sportsync.data.models.Challenge
import brawijaya.example.sportsync.ui.components.ReadOnlyTextField
import brawijaya.example.sportsync.ui.viewmodels.ChallengeViewModel
import androidx.compose.runtime.getValue

@Composable
fun DetailChallengeScreen(
    navController: NavController,
    challengeId: String,
    viewModel: ChallengeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(challengeId) {
        viewModel.getChallengeById(challengeId)
    }

    LaunchedEffect(uiState.isAcceptSuccess) {
        if (uiState.isAcceptSuccess) {
            snackbarHostState.showSnackbar(
                message = "Challenge accepted successfully!",
                duration = SnackbarDuration.Short
            )
            viewModel.resetAcceptSuccessState()
            navController.navigate(Screen.FindMatch.route)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
            viewModel.clearErrorMessage()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearCurrentChallenge()
        }
    }

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
                            contentDescription = "Back",
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.currentChallenge != null -> {
                    DetailChallengeContent(
                        challenge = uiState.currentChallenge!!,
                        isAcceptLoading = uiState.isAcceptLoading,
                        onAcceptChallenge = { viewModel.acceptChallenge(challengeId) }
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Challenge not found")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailChallengeContent(
    challenge: Challenge,
    isAcceptLoading: Boolean,
    onAcceptChallenge: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = challenge.declaration,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 28.sp
        )

        ReadOnlyTextField(
            label = "Challenge Declaration",
            value = challenge.declaration
        )

        ReadOnlyTextField(
            label = "Sport Category",
            value = challenge.category
        )

        ReadOnlyTextField(
            label = "Gender",
            value = challenge.gender
        )

        ReadOnlyTextField(
            label = "Match Type",
            value = challenge.type
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReadOnlyTextField(
                label = "Date",
                value = challenge.getDisplayDate(),
                modifier = Modifier.weight(1f)
            )

            ReadOnlyTextField(
                label = "Time",
                value = challenge.getDisplayTime(),
                modifier = Modifier.weight(1f)
            )
        }

        ReadOnlyTextField(
            label = "Description",
            value = challenge.description ?: "No description provided",
            modifier = Modifier.height(120.dp),
            shape = RoundedCornerShape(15.dp),
            maxLines = Int.MAX_VALUE
        )

        if (challenge.isAccepted()) {
            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Gray
                ),
                enabled = false
            ) {
                Text(
                    text = "Already Accepted",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Button(
                onClick = onAcceptChallenge,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(28.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFBBB46)
                ),
                enabled = !isAcceptLoading
            ) {
                if (isAcceptLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Accept",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}