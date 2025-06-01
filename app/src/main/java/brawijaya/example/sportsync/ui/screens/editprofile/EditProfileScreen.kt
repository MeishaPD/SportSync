package brawijaya.example.sportsync.ui.screens.editprofile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.components.BottomNavigation
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.ui.screens.editprofile.components.EditProfileContent
import brawijaya.example.sportsync.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by profileViewModel.profileState.collectAsState()
    val profile = profileState.profile

    var fullName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var favoriteSport by remember { mutableStateOf("") }

    LaunchedEffect(profile) {
        fullName = profile.fullName
        firstName = profile.firstName
        lastName = profile.lastName
        age = if (profile.age > 0) profile.age.toString() else ""
        address = profile.address
        phone = profile.phone
        favoriteSport = profile.favoriteSport
        Log.d("EditProfileScreen:", profile.toString())
    }

    LaunchedEffect(firstName, lastName) {
        if (firstName.isNotEmpty() || lastName.isNotEmpty()) {
            fullName = "$firstName $lastName".trim()
        }
    }

    LaunchedEffect(profileState.isUpdating) {
        if (!profileState.isUpdating && profileState.error == null &&
            (firstName != profile.firstName || lastName != profile.lastName ||
                    age != (if (profile.age > 0) profile.age.toString() else "") ||
                    address != profile.address || phone != profile.phone ||
                    favoriteSport != profile.favoriteSport)) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            Surface(
                shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
                color = Color(0xFFCDE4F4)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Edit Profile",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(top = 16.dp)
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
                .verticalScroll(rememberScrollState())
        ) {
            EditProfileContent(
                firstName = firstName,
                lastName = lastName,
                age = age,
                address = address,
                phone = phone,
                favoriteSport = favoriteSport,
                isLoading = profileState.isLoading,
                isUpdating = profileState.isUpdating,
                error = profileState.error,
                userEmail = profile.email,
                fullName = fullName.ifEmpty { "User" },
                onFirstNameChange = { firstName = it },
                onLastNameChange = { lastName = it },
                onAgeChange = { age = it },
                onAddressChange = { address = it },
                onPhoneChange = { phone = it },
                onFavoriteSportChange = { favoriteSport = it },
                onConfirm = {
                    if (firstName != profile.firstName || lastName != profile.lastName) {
                        profileViewModel.updateFirstAndLastName(firstName, lastName)
                    }

                    if (age.isNotEmpty() && age.toIntOrNull() != profile.age) {
                        age.toIntOrNull()?.let { profileViewModel.updateAge(it) }
                    }
                    if (address != profile.address) {
                        profileViewModel.updateAddress(address)
                    }
                    if (phone != profile.phone) {
                        profileViewModel.updatePhone(phone)
                    }
                    if (favoriteSport != profile.favoriteSport) {
                        profileViewModel.updateFavoriteSport(favoriteSport)
                    }
                },
                onCancel = {
                    navController.popBackStack()
                },
                onClearError = {
                    profileViewModel.clearError()
                }
            )
        }
    }
}