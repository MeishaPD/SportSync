package brawijaya.example.sportsync.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.Challenge
import brawijaya.example.sportsync.data.repository.ChallengeRepository
import brawijaya.example.sportsync.utils.LocationManager
import brawijaya.example.sportsync.utils.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChallengeUiState(
    val challenges: List<Challenge> = emptyList(),
    val filteredChallenges: List<Challenge> = emptyList(),
    val selectedCategory: String = "Badminton",
    val selectedDate: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentChallenge: Challenge? = null,
    val challengeDeclaration: String = "",
    val sportCategory: String = "",
    val selectedGender: String = "Man",
    val selectedMatchType: String = "Single",
    val createSelectedDate: String = "",
    val createSelectedTime: String = "",
    val description: String = "",
    val isCreateSuccess: Boolean = false,
    val isAcceptSuccess: Boolean = false,
    val isAcceptLoading: Boolean = false,
    val currentLocation: LocationData? = null,
    val isLocationLoading: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val maxDistanceKm: Double = LocationManager.DEFAULT_MAX_DISTANCE_KM,
    val isLocationBasedFilterEnabled: Boolean = true
)

class ChallengeViewModel : ViewModel() {

    private val repository = ChallengeRepository()
    private var locationManager: LocationManager? = null

    private val _uiState = MutableStateFlow(ChallengeUiState())
    val uiState: StateFlow<ChallengeUiState> = _uiState.asStateFlow()

    init {
        loadChallenges()
    }

    fun initializeLocationManager(context: Context) {
        locationManager = LocationManager(context)
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        val hasPermission = locationManager?.hasLocationPermission() ?: false
        _uiState.value = _uiState.value.copy(hasLocationPermission = hasPermission)

        if (hasPermission) {
            getCurrentLocation()
        }
    }

    fun updateLocationPermissionStatus(hasPermission: Boolean) {
        _uiState.value = _uiState.value.copy(hasLocationPermission = hasPermission)
        if (hasPermission) {
            getCurrentLocation()
        }
    }

    fun getCurrentLocation() {
        locationManager?.let { manager ->
            if (!manager.hasLocationPermission()) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Location permission is required to get nearby challenges"
                )
                return
            }

            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLocationLoading = true)

                manager.getCurrentLocation().fold(
                    onSuccess = { locationData ->
                        _uiState.value = _uiState.value.copy(
                            currentLocation = locationData,
                            isLocationLoading = false
                        )
                        loadChallenges()
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLocationLoading = false,
                            errorMessage = "Failed to get location: ${error.message}"
                        )
                        loadAllChallenges()
                    }
                )
            }
        }
    }

    fun loadChallenges() {
        val currentState = _uiState.value

        if (currentState.isLocationBasedFilterEnabled &&
            currentState.currentLocation != null &&
            locationManager != null) {
            loadChallengesNearby()
        } else {
            loadAllChallenges()
        }
    }

    private fun loadChallengesNearby() {
        val currentState = _uiState.value
        val userLocation = currentState.currentLocation
        val manager = locationManager

        if (userLocation == null || manager == null) {
            loadAllChallenges()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            repository.getAvailableChallengesNearby(
                userLocation = userLocation,
                maxDistanceKm = currentState.maxDistanceKm,
                locationManager = manager
            ).fold(
                onSuccess = { challenges ->
                    _uiState.value = _uiState.value.copy(
                        challenges = challenges,
                        filteredChallenges = filterChallenges(challenges),
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load nearby challenges: ${error.message}"
                    )
                    loadAllChallenges()
                }
            )
        }
    }

    private fun loadAllChallenges() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            repository.getAvailableChallenges().fold(
                onSuccess = { challenges ->
                    _uiState.value = _uiState.value.copy(
                        challenges = challenges,
                        filteredChallenges = filterChallenges(challenges),
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            )
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyCategoryAndDateFilter()
    }

    fun selectDate(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        applyCategoryAndDateFilter()
    }

    private fun applyCategoryAndDateFilter() {
        val currentState = _uiState.value
        val filteredChallenges = filterChallenges(currentState.challenges)
        _uiState.value = currentState.copy(filteredChallenges = filteredChallenges)
    }

    private fun filterChallenges(challenges: List<Challenge>): List<Challenge> {
        val currentState = _uiState.value
        return challenges.filter { challenge ->
            val categoryMatch = challenge.category.equals(currentState.selectedCategory, ignoreCase = true)
            val dateMatch = if (currentState.selectedDate.isEmpty()) {
                true
            } else {
                val isoDate = Challenge.convertDisplayDateToIso(currentState.selectedDate)
                challenge.date == isoDate
            }
            categoryMatch && dateMatch
        }
    }

    fun getChallengeById(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getChallengeById(id).fold(
                onSuccess = { challenge ->
                    _uiState.value = _uiState.value.copy(
                        currentChallenge = challenge,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            )
        }
    }

    fun acceptChallenge(challengeId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAcceptLoading = true,
                errorMessage = null
            )

            repository.acceptChallenge(challengeId).fold(
                onSuccess = { updatedChallenge ->
                    _uiState.value = _uiState.value.copy(
                        currentChallenge = updatedChallenge,
                        isAcceptLoading = false,
                        isAcceptSuccess = true
                    )
                    loadChallenges()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isAcceptLoading = false,
                        errorMessage = when {
                            error.message?.contains("User not authenticated") == true ->
                                "Please log in to accept challenges"
                            error.message?.contains("No rows") == true ->
                                "This challenge has already been accepted"
                            else -> "Failed to accept challenge: ${error.message}"
                        }
                    )
                }
            )
        }
    }

    private fun validateCreateChallengeInputs(): Boolean {
        val currentState = _uiState.value
        when {
            currentState.challengeDeclaration.isBlank() -> {
                _uiState.value = currentState.copy(errorMessage = "Challenge declaration is required")
                return false
            }
            currentState.sportCategory.isBlank() -> {
                _uiState.value = currentState.copy(errorMessage = "Sport category is required")
                return false
            }
            currentState.createSelectedDate.isBlank() -> {
                _uiState.value = currentState.copy(errorMessage = "Date is required")
                return false
            }
            currentState.createSelectedTime.isBlank() -> {
                _uiState.value = currentState.copy(errorMessage = "Time is required")
                return false
            }
            currentState.currentLocation == null -> {
                _uiState.value = currentState.copy(errorMessage = "Location is required. Please enable location access and try again.")
                return false
            }
        }
        return true
    }

    fun createChallenge() {
        if (!validateCreateChallengeInputs()) {
            return
        }

        val currentState = _uiState.value
        val location = currentState.currentLocation!!

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

            val challenge = Challenge(
                id = null,
                declaration = currentState.challengeDeclaration,
                category = currentState.sportCategory,
                gender = currentState.selectedGender,
                type = currentState.selectedMatchType,
                date = Challenge.convertDisplayDateToIso(currentState.createSelectedDate),
                time = Challenge.convertDisplayTimeToDatabase(currentState.createSelectedTime),
                description = currentState.description.ifBlank { null },
                latitude = location.latitude,
                longitude = location.longitude,
                location_name = location.locationName
            )

            repository.createChallenge(challenge).fold(
                onSuccess = { createdChallenge ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isCreateSuccess = true,
                        errorMessage = null
                    )
                    loadChallenges()
                },
                onFailure = { error ->
                    Log.d("Create Challenge", "Failed to create challenge: ${error.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to create challenge: ${error.message}"
                    )
                }
            )
        }
    }

    fun resetCreateChallengeForm() {
        _uiState.value = _uiState.value.copy(
            challengeDeclaration = "",
            sportCategory = "",
            selectedGender = "Man",
            selectedMatchType = "Single",
            createSelectedDate = "",
            createSelectedTime = "",
            description = "",
            isCreateSuccess = false,
            errorMessage = null,
            currentLocation = null
        )
    }

    fun resetCreateSuccessState() {
        _uiState.value = _uiState.value.copy(isCreateSuccess = false)
    }

    fun resetAcceptSuccessState() {
        _uiState.value = _uiState.value.copy(isAcceptSuccess = false)
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearCurrentChallenge() {
        _uiState.value = _uiState.value.copy(currentChallenge = null)
    }

    fun updateChallengeDeclaration(declaration: String) {
        _uiState.value = _uiState.value.copy(challengeDeclaration = declaration)
    }

    fun updateSportCategory(category: String) {
        _uiState.value = _uiState.value.copy(sportCategory = category)
    }

    fun updateSelectedGender(gender: String) {
        _uiState.value = _uiState.value.copy(selectedGender = gender)
    }

    fun updateSelectedMatchType(matchType: String) {
        _uiState.value = _uiState.value.copy(selectedMatchType = matchType)
    }

    fun updateCreateSelectedDate(date: String) {
        _uiState.value = _uiState.value.copy(createSelectedDate = date)
    }

    fun updateCreateSelectedTime(time: String) {
        _uiState.value = _uiState.value.copy(createSelectedTime = time)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }
}