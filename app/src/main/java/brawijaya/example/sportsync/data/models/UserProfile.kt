package brawijaya.example.sportsync.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val fullName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val age: Int = 0,
    val address: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val favoriteSport: String = "",
    val bio: String = "",
    val profileImageUrl: String = "",
    val joinedDate: String = "",
    val followingCount: Int = 0,
    val followersCount: Int = 0,
    val gamesPlayed: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val level: Int = 0,
    val xp: Int = 0,
    val streak: Int = 0,
    val rank: String = ""
)

@Serializable
data class UpdateProfileRequest(
    val fullName: String?,
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val age: Int?,
    val address: String?,
    val phone: String?,
    val birthDate: String?,
    val favoriteSport: String?,
    val bio: String?,
    val profileImageUrl: String?,
    val followingCount: Int?,
    val followersCount: Int?,
    val gamesPlayed: Int?,
    val level: Int?,
    val xp: Int?,
    val streak: Int?,
    val rank: String?
)

sealed class ProfileResult {
    object Success : ProfileResult()
    data class Error(val message: String) : ProfileResult()
    object Loading : ProfileResult()
}

data class ProfileState(
    val profile: UserProfile = UserProfile(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdating: Boolean = false
)