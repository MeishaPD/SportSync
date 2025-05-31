package brawijaya.example.sportsync.ui.screens.createchallenge

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.AccessTime
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import brawijaya.example.sportsync.ui.screens.createchallenge.components.GenderTypeSwitch
import brawijaya.example.sportsync.ui.viewmodels.ChallengeViewModel
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CreateChallengeScreen(
    navController: NavController,
    viewModel: ChallengeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle success state
    LaunchedEffect(uiState.isCreateSuccess) {
        if (uiState.isCreateSuccess) {
            snackbarHostState.showSnackbar("Challenge created successfully!")
            viewModel.resetCreateSuccessState()
            viewModel.resetCreateChallengeForm()
            navController.popBackStack()
        }
    }

    // Handle error messages
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearErrorMessage()
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
                        text = "Create Challenge",
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
            CreateChallengeContent(
                uiState = uiState,
                onChallengeDeclarationChange = viewModel::updateChallengeDeclaration,
                onSportCategoryChange = viewModel::updateSportCategory,
                onGenderChange = viewModel::updateSelectedGender,
                onMatchTypeChange = viewModel::updateSelectedMatchType,
                onDateChange = viewModel::updateCreateSelectedDate,
                onTimeChange = viewModel::updateCreateSelectedTime,
                onDescriptionChange = viewModel::updateDescription,
                onCreateChallenge = viewModel::createChallenge
            )

            // Loading overlay
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFFBBB46),
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CreateChallengeContent(
    uiState: brawijaya.example.sportsync.ui.viewmodels.ChallengeUiState,
    onChallengeDeclarationChange: (String) -> Unit,
    onSportCategoryChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onMatchTypeChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCreateChallenge: () -> Unit
) {
    var sportDropdownExpanded by remember { mutableStateOf(false) }

    val sportCategories =
        listOf("Football", "Basketball", "Tennis", "Badminton", "Volleyball", "Table Tennis")

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            onDateChange(dateFormat.format(selectedCalendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            onTimeChange(timeFormat.format(timeCalendar.time))
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Plan Your Challenge and Let's See Who's Brave Enough to Accept It.",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 28.sp
        )

        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Challenge Declaration",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            OutlinedTextField(
                value = uiState.challengeDeclaration,
                onValueChange = onChallengeDeclarationChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(25.dp),
                textStyle = TextStyle(fontSize = 14.sp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color.Black
                )
            )
        }

        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Sport Category",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Box(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.sportCategory,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    textStyle = TextStyle(fontSize = 14.sp),
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Sport",
                            tint = Color.Black
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black,
                        focusedBorderColor = Color.Black
                    )
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { sportDropdownExpanded = true }
                ) {}

                DropdownMenu(
                    expanded = sportDropdownExpanded,
                    onDismissRequest = { sportDropdownExpanded = false }
                ) {
                    sportCategories.forEach { sport ->
                        DropdownMenuItem(
                            text = { Text(sport) },
                            onClick = {
                                onSportCategoryChange(sport)
                                sportDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Gender",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            GenderTypeSwitch(
                options = listOf("Man", "Woman"),
                selectedOption = uiState.selectedGender,
                onOptionSelected = onGenderChange,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Match Type",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            GenderTypeSwitch(
                options = listOf("Single", "Doubles"),
                selectedOption = uiState.selectedMatchType,
                onOptionSelected = onMatchTypeChange,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                OutlinedTextField(
                    value = uiState.createSelectedDate,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(25.dp),
                    textStyle = TextStyle(fontSize = 14.sp),
                    readOnly = true,
                    placeholder = { Text("Select Date", fontSize = 14.sp, color = Color.Gray) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = "Select Date",
                            tint = Color.Black,
                            modifier = Modifier.clickable {
                                datePickerDialog.show()
                            }
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black,
                        focusedBorderColor = Color.Black
                    )
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Time",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                OutlinedTextField(
                    value = uiState.createSelectedTime,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(25.dp),
                    textStyle = TextStyle(fontSize = 14.sp),
                    readOnly = true,
                    placeholder = { Text("Select Time", fontSize = 14.sp, color = Color.Gray) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = "Select Time",
                            tint = Color.Black,
                            modifier = Modifier.clickable {
                                timePickerDialog.show()
                            }
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Black,
                        focusedBorderColor = Color.Black
                    )
                )
            }
        }

        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Description",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(15.dp),
                textStyle = TextStyle(fontSize = 14.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onCreateChallenge,
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
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Confirm",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}