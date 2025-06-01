package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.ProfileResult
import brawijaya.example.sportsync.data.models.ProfileState
import brawijaya.example.sportsync.data.models.UpdateProfileRequest
import brawijaya.example.sportsync.data.models.UserProfile
import brawijaya.example.sportsync.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _profileState.value = _profileState.value.copy(isLoading = true, error = null)

                profileRepository.updateOtherData().collect { result ->
                    when (result) {
                        is ProfileResult.Loading -> {
                        }
                        is ProfileResult.Success -> {
                            loadProfileData()
                        }
                        is ProfileResult.Error -> {
                            Log.e("ProfileViewModel", "Failed to update dummy data: ${result.message}")
                            loadProfileData()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Failed to load profile", e)
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    error = "Failed to load profile: ${e.message}"
                )
            }
        }
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                profileRepository.getUserProfile().collect { profile ->
                    Log.d("ProfileViewModel", "Profile loaded: $profile")
                    _profileState.value = ProfileState(
                        profile = profile,
                        isLoading = false,
                        error = null,
                        isUpdating = false
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Failed to load profile data", e)
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    error = "Failed to load profile data: ${e.message}"
                )
            }
        }
    }

    fun updateProfile(updateRequest: UpdateProfileRequest) {
        viewModelScope.launch {
            profileRepository.updateUserProfile(updateRequest).collect { result ->
                when (result) {
                    is ProfileResult.Loading -> {
                        _profileState.value = _profileState.value.copy(
                            isUpdating = true,
                            error = null
                        )
                    }
                    is ProfileResult.Success -> {
                        loadUserProfile()
                    }
                    is ProfileResult.Error -> {
                        _profileState.value = _profileState.value.copy(
                            isUpdating = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun updateFullName(fullName: String) {
        updateSingleField("fullName", fullName)
    }

    fun updateFirstName(firstName: String) {
        updateSingleField("firstName", firstName)
    }

    fun updateLastName(lastName: String) {
        updateSingleField("lastName", lastName)
    }

    fun updateUsername(username: String) {
        updateSingleField("username", username)
    }

    fun updateAge(age: Int) {
        viewModelScope.launch {
            profileRepository.updateAge(age).collect { result ->
                handleUpdateResult(result)
            }
        }
    }

    fun updateAddress(address: String) {
        updateSingleField("address", address)
    }

    fun updatePhone(phone: String) {
        updateSingleField("phone", phone)
    }

    fun updateBirthDate(birthDate: String) {
        updateSingleField("birthDate", birthDate)
    }

    fun updateFavoriteSport(favoriteSport: String) {
        updateSingleField("favoriteSport", favoriteSport)
    }

    fun updateBio(bio: String) {
        updateSingleField("bio", bio)
    }

    fun updateFirstAndLastName(firstName: String, lastName: String) {
        val fullName = "$firstName $lastName".trim()
        updateFullName(fullName)
    }

    private fun updateSingleField(fieldName: String, value: String) {
        viewModelScope.launch {
            profileRepository.updateProfileField(fieldName, value).collect { result ->
                handleUpdateResult(result)
            }
        }
    }

    private fun handleUpdateResult(result: ProfileResult) {
        when (result) {
            is ProfileResult.Loading -> {
                _profileState.value = _profileState.value.copy(
                    isUpdating = true,
                    error = null
                )
            }
            is ProfileResult.Success -> {
                loadUserProfile()
            }
            is ProfileResult.Error -> {
                _profileState.value = _profileState.value.copy(
                    isUpdating = false,
                    error = result.message
                )
            }
        }
    }

    fun clearError() {
        _profileState.value = _profileState.value.copy(error = null)
    }

    fun getCurrentProfile(): UserProfile {
        return _profileState.value.profile
    }

    fun refreshProfile() {
        loadUserProfile()
    }

    fun populateDummyData() {
        viewModelScope.launch {
            profileRepository.updateOtherData().collect { result ->
                when (result) {
                    is ProfileResult.Loading -> {
                        _profileState.value = _profileState.value.copy(isUpdating = true)
                    }
                    is ProfileResult.Success -> {
                        Log.d("ProfileViewModel", "Dummy data populated successfully")
                        loadProfileData() // Reload profile to show updated data
                    }
                    is ProfileResult.Error -> {
                        Log.e("ProfileViewModel", "Failed to populate dummy data: ${result.message}")
                        _profileState.value = _profileState.value.copy(
                            isUpdating = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun isProfileComplete(): Boolean {
        val profile = _profileState.value.profile
        return profile.fullName.isNotEmpty() &&
                profile.age > 0 &&
                profile.address.isNotEmpty() &&
                profile.phone.isNotEmpty() &&
                profile.favoriteSport.isNotEmpty()
    }

    fun getProfileCompletionPercentage(): Int {
        val profile = _profileState.value.profile
        var completed = 0
        val total = 8

        if (profile.fullName.isNotEmpty()) completed++
        if (profile.age > 0) completed++
        if (profile.address.isNotEmpty()) completed++
        if (profile.phone.isNotEmpty()) completed++
        if (profile.favoriteSport.isNotEmpty()) completed++
        if (profile.bio.isNotEmpty()) completed++
        if (profile.birthDate.isNotEmpty()) completed++
        if (profile.username.isNotEmpty()) completed++

        return (completed * 100) / total
    }

    fun hasGameStats(): Boolean {
        val profile = _profileState.value.profile
        return profile.gamesPlayed > 0 || profile.wins > 0 || profile.losses > 0
    }

    fun getWinRate(): Float {
        val profile = _profileState.value.profile
        return if (profile.wins + profile.losses > 0) {
            profile.wins.toFloat() / (profile.wins + profile.losses)
        } else {
            0f
        }
    }

    fun getLevelProgress(): Float {
        val profile = _profileState.value.profile
        if (profile.level <= 0) return 0f

        val baseXpPerLevel = 200
        val xpForCurrentLevel = profile.level * baseXpPerLevel
        val xpForNextLevel = (profile.level + 1) * baseXpPerLevel
        val currentLevelProgress = (profile.xp - xpForCurrentLevel).toFloat()
        val levelXpRange = (xpForNextLevel - xpForCurrentLevel).toFloat()

        return if (levelXpRange > 0) {
            (currentLevelProgress / levelXpRange).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
}