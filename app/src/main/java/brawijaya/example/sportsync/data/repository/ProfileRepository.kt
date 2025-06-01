package brawijaya.example.sportsync.data.repository

import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.user.UserInfo
import brawijaya.example.sportsync.data.SupabaseClient
import brawijaya.example.sportsync.data.models.ProfileResult
import brawijaya.example.sportsync.data.models.UpdateProfileRequest
import brawijaya.example.sportsync.data.models.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Singleton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Singleton
class ProfileRepository @Inject constructor() {

    private val auth = SupabaseClient.client.auth

    fun getUserProfile(): Flow<UserProfile> = flow {
        try {
            val currentUser = auth.currentUserOrNull()
            if (currentUser != null) {
                val profile = parseUserMetadata(currentUser)
                emit(profile)
            } else {
                emit(UserProfile())
            }
        } catch (e: Exception) {
            emit(UserProfile())
        }
    }

    fun updateUserProfile(updateRequest: UpdateProfileRequest): Flow<ProfileResult> = flow {
        try {
            emit(ProfileResult.Loading)

            val currentUser = auth.currentUserOrNull()
            if (currentUser == null) {
                emit(ProfileResult.Error("User not authenticated"))
                return@flow
            }

            val metadataBuilder = buildJsonObject {
                updateRequest.fullName?.let { fullName ->
                    put("full_name", JsonPrimitive(fullName))
                    val nameParts = fullName.trim().split(" ", limit = 2)
                    put("first_name", JsonPrimitive(nameParts[0]))
                    put("last_name", JsonPrimitive(if (nameParts.size > 1) nameParts[1] else ""))
                }
                updateRequest.firstName?.let { firstName ->
                    put("first_name", JsonPrimitive(firstName))
                    val currentLastName = getCurrentUserProfile()?.lastName ?: ""
                    val newFullName =
                        if (currentLastName.isNotEmpty()) "$firstName $currentLastName" else firstName
                    put("full_name", JsonPrimitive(newFullName))
                }
                updateRequest.lastName?.let { lastName ->
                    put("last_name", JsonPrimitive(lastName))
                    val currentFirstName = getCurrentUserProfile()?.firstName ?: ""
                    val newFullName =
                        if (currentFirstName.isNotEmpty()) "$currentFirstName $lastName" else lastName
                    put("full_name", JsonPrimitive(newFullName))
                }
                updateRequest.username?.let { put("username", JsonPrimitive(it)) }
                updateRequest.age?.let { put("age", JsonPrimitive(it)) }
                updateRequest.address?.let { put("address", JsonPrimitive(it)) }
                updateRequest.phone?.let { put("phone", JsonPrimitive(it)) }
                updateRequest.birthDate?.let { put("birth_date", JsonPrimitive(it)) }
                updateRequest.favoriteSport?.let { put("favorite_sport", JsonPrimitive(it)) }
                updateRequest.bio?.let { put("bio", JsonPrimitive(it)) }
                updateRequest.profileImageUrl?.let { put("profile_image_url", JsonPrimitive(it)) }
                updateRequest.followersCount?.let { put("followers_count", JsonPrimitive(it)) }
                updateRequest.followingCount?.let { put("following_count", JsonPrimitive(it)) }
                updateRequest.gamesPlayed?.let { put("games_played", JsonPrimitive(it)) }
                updateRequest.xp?.let { put("xp", JsonPrimitive(it)) }
                updateRequest.level?.let { put("level", JsonPrimitive(it)) }
                updateRequest.rank?.let { put("rank", JsonPrimitive(it)) }

                put(
                    "last_updated",
                    JsonPrimitive(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                )
            }

            auth.updateUser {
                data = metadataBuilder
            }

            emit(ProfileResult.Success)
        } catch (e: Exception) {
            emit(ProfileResult.Error(e.message ?: "Failed to update profile"))
        }
    }

    fun updateProfileField(fieldName: String, value: String): Flow<ProfileResult> = flow {
        try {
            emit(ProfileResult.Loading)

            val updateRequest = when (fieldName) {
                "fullName" -> UpdateProfileRequest(
                    fullName = value,
                    firstName = null,
                    lastName = null,
                    username = null,
                    age = null,
                    address = null,
                    phone = null,
                    birthDate = null,
                    favoriteSport = null,
                    bio = null,
                    profileImageUrl = null,
                    followingCount = null,
                    followersCount = null,
                    gamesPlayed = null,
                    level = null,
                    xp = null,
                    streak = null,
                    rank = null
                )

                "firstName" -> UpdateProfileRequest(
                    fullName = null,
                    firstName = value,
                    lastName = null,
                    username = null,
                    age = null,
                    address = null,
                    phone = null,
                    birthDate = null,
                    favoriteSport = null,
                    bio = null,
                    profileImageUrl = null,
                    followingCount = null,
                    followersCount = null,
                    gamesPlayed = null,
                    level = null,
                    xp = null,
                    streak = null,
                    rank = null
                )

                "lastName" -> UpdateProfileRequest(
                    fullName = null,
                    firstName = null,
                    lastName = value,
                    username = null,
                    age = null,
                    address = null,
                    phone = null,
                    birthDate = null,
                    favoriteSport = null,
                    bio = null,
                    profileImageUrl = null,
                    followingCount = null,
                    followersCount = null,
                    gamesPlayed = null,
                    level = null,
                    xp = null,
                    streak = null,
                    rank = null
                )

                "username" -> UpdateProfileRequest(
                    fullName = null,
                    firstName = null,
                    lastName = null,
                    username = value,
                    age = null,
                    address = null,
                    phone = null,
                    birthDate = null,
                    favoriteSport = null,
                    bio = null,
                    profileImageUrl = null,
                    followingCount = null,
                    followersCount = null,
                    gamesPlayed = null,
                    level = null,
                    xp = null,
                    streak = null,
                    rank = null
                )

                "address" -> UpdateProfileRequest(
                    fullName = null,
                    firstName = null,
                    lastName = null,
                    username = null,
                    age = null,
                    address = value,
                    phone = null,
                    birthDate = null,
                    favoriteSport = null,
                    bio = null,
                    profileImageUrl = null,
                    followingCount = null,
                    followersCount = null,
                    gamesPlayed = null,
                    level = null,
                    xp = null,
                    streak = null,
                    rank = null
                )

                "phone" -> UpdateProfileRequest(
                    fullName = null,
                    firstName = null,
                    lastName = null,
                    username = null,
                    age = null,
                    address = null,
                    phone = value,
                    birthDate = null,
                    favoriteSport = null,
                    bio = null,
                    profileImageUrl = null,
                    followingCount = null,
                    followersCount = null,
                    gamesPlayed = null,
                    level = null,
                    xp = null,
                    streak = null,
                    rank = null
                )

                "birthDate" -> UpdateProfileRequest(
                    fullName = null,
                    firstName = null,
                    lastName = null,
                    username = null,
                    age = null,
                    address = null,
                    phone = null,
                    birthDate = value,
                    favoriteSport = null,
                    bio = null,
                    profileImageUrl = null,
                    followingCount = null,
                    followersCount = null,
                    gamesPlayed = null,
                    level = null,
                    xp = null,
                    streak = null,
                    rank = null
                )

                "favoriteSport" -> UpdateProfileRequest(
                    fullName = null,
                    firstName = null,
                    lastName = null,
                    username = null,
                    age = null,
                    address = null,
                    phone = null,
                    birthDate = null,
                    favoriteSport = value,
                    bio = null,
                    profileImageUrl = null,
                    followingCount = null,
                    followersCount = null,
                    gamesPlayed = null,
                    level = null,
                    xp = null,
                    streak = null,
                    rank = null
                )

                "bio" -> UpdateProfileRequest(
                    fullName = null,
                    firstName = null,
                    lastName = null,
                    username = null,
                    age = null,
                    address = null,
                    phone = null,
                    birthDate = null,
                    favoriteSport = null,
                    bio = value,
                    profileImageUrl = null,
                    followingCount = null,
                    followersCount = null,
                    gamesPlayed = null,
                    level = null,
                    xp = null,
                    streak = null,
                    rank = null
                )

                else -> {
                    emit(ProfileResult.Error("Invalid field name"))
                    return@flow
                }
            }

            updateUserProfile(updateRequest).collect { result ->
                emit(result)
            }
        } catch (e: Exception) {
            emit(ProfileResult.Error(e.message ?: "Failed to update profile field"))
        }
    }

    fun updateAge(age: Int): Flow<ProfileResult> = flow {
        try {
            emit(ProfileResult.Loading)

            val updateRequest = UpdateProfileRequest(
                fullName = null,
                firstName = null,
                lastName = null,
                username = null,
                age = age,
                address = null,
                phone = null,
                birthDate = null,
                favoriteSport = null,
                bio = null,
                profileImageUrl = null,
                followingCount = null,
                followersCount = null,
                gamesPlayed = null,
                level = null,
                xp = null,
                streak = null,
                rank = null
            )

            updateUserProfile(updateRequest).collect { result ->
                emit(result)
            }
        } catch (e: Exception) {
            emit(ProfileResult.Error(e.message ?: "Failed to update age"))
        }
    }

    fun updateOtherData(): Flow<ProfileResult> = flow {
        try {
            emit(ProfileResult.Loading)

            val updateRequest = UpdateProfileRequest(
                fullName = null,
                firstName = null,
                lastName = null,
                username = null,
                age = null,
                address = null,
                phone = null,
                birthDate = null,
                favoriteSport = null,
                bio = null,
                profileImageUrl = null,
                followersCount = 57,
                followingCount = 63,
                gamesPlayed = 134,
                level = 5,
                xp = 1390,
                streak = 3,
                rank = "Gold"
            )

            updateUserProfile(updateRequest).collect { result ->
                emit(result)
            }
        } catch (e: Exception) {
            emit(ProfileResult.Error(e.message ?: "Failed to update followers and following"))
        }
    }

    private fun parseUserMetadata(user: UserInfo): UserProfile {
        val metadata = user.userMetadata

        val storedFullName = getStringFromMetadata(metadata, "full_name")
        val storedFirstName = getStringFromMetadata(metadata, "first_name")
        val storedLastName = getStringFromMetadata(metadata, "last_name")

        val fullName = if (storedFullName.isNotEmpty()) {
            storedFullName
        } else if (storedFirstName.isNotEmpty() || storedLastName.isNotEmpty()) {
            "$storedFirstName $storedLastName".trim()
        } else {
            ""
        }

        val nameParts =
            if (fullName.isNotEmpty()) fullName.trim().split(" ", limit = 2) else emptyList()
        val firstName =
            if (storedFirstName.isNotEmpty()) storedFirstName else (nameParts.getOrNull(0) ?: "")
        val lastName =
            if (storedLastName.isNotEmpty()) storedLastName else (nameParts.getOrNull(1) ?: "")

        return UserProfile(
            fullName = fullName,
            firstName = firstName,
            lastName = lastName,
            username = getStringFromMetadata(metadata, "username"),
            email = user.email ?: "",
            age = getIntFromMetadata(metadata, "age"),
            address = getStringFromMetadata(metadata, "address"),
            phone = getStringFromMetadata(metadata, "phone"),
            birthDate = getStringFromMetadata(metadata, "birth_date"),
            favoriteSport = getStringFromMetadata(metadata, "favorite_sport"),
            bio = getStringFromMetadata(metadata, "bio"),
            profileImageUrl = getStringFromMetadata(metadata, "profile_image_url"),
            joinedDate = user.createdAt.toString(),
            followingCount = getIntFromMetadata(metadata, "following_count"),
            followersCount = getIntFromMetadata(metadata, "followers_count"),
            gamesPlayed = getIntFromMetadata(metadata, "games_played"),
            wins = getIntFromMetadata(metadata, "wins"),
            losses = getIntFromMetadata(metadata, "losses"),
            xp = getIntFromMetadata(metadata, "xp"),
            level = getIntFromMetadata(metadata, "level"),
            streak = getIntFromMetadata(metadata, "streak"),
            rank = getStringFromMetadata(metadata, "rank")
        )
    }

    private fun getStringFromMetadata(metadata: JsonObject?, key: String): String {
        return try {
            metadata?.get(key)?.jsonPrimitive?.content ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun getIntFromMetadata(metadata: JsonObject?, key: String): Int {
        return try {
            metadata?.get(key)?.jsonPrimitive?.int ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun getCurrentUserProfile(): UserProfile? {
        val currentUser = auth.currentUserOrNull()
        return currentUser?.let { parseUserMetadata(it) }
    }

    fun isUserAuthenticated(): Boolean = auth.currentUserOrNull() != null
}