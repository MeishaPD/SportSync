package brawijaya.example.sportsync.data.repository

import android.util.Log
import brawijaya.example.sportsync.data.SupabaseClient
import brawijaya.example.sportsync.data.models.Challenge
import brawijaya.example.sportsync.utils.LocationData
import brawijaya.example.sportsync.utils.LocationManager
import io.github.jan.supabase.postgrest.*
import io.github.jan.supabase.postgrest.query.*
import io.github.jan.supabase.gotrue.auth
import java.time.LocalDate
import java.util.Objects.isNull

class ChallengeRepository {

    private val supabase = SupabaseClient.client

    suspend fun createChallenge(challenge: Challenge): Result<Challenge> {
        return try {
            val currentUser = supabase.auth.currentUserOrNull()
            if (currentUser == null) {
                return Result.failure(Exception("User not authenticated"))
            }

            val challengeWithUserId = challenge.copy(created_by = currentUser.id)

            val result = supabase
                .from("challenge")
                .insert(challengeWithUserId) {
                    select()
                }
                .decodeSingle<Challenge>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun acceptChallenge(challengeId: String): Result<Challenge> {
        return try {
            val currentUser = supabase.auth.currentUserOrNull()
            if (currentUser == null) {
                return Result.failure(Exception("User not authenticated"))
            }

            val result = supabase
                .from("challenge")
                .update(
                    mapOf("accepted_by" to currentUser.id)
                ) {
                    filter {
                        eq("id", challengeId)
                        isNull("accepted_by")
                    }
                    select()
                }
                .decodeSingle<Challenge>()

            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChallengeById(id: String): Result<Challenge> {
        return try {
            val challenge = supabase
                .from("challenge")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<Challenge>()
            Result.success(challenge)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvailableChallenges(): Result<List<Challenge>> {
        return try {
            val thirtyDaysAgo = LocalDate.now().minusDays(30).toString()
            val challenges = supabase
                .from("challenge")
                .select {
                    filter {
                        gte("date", thirtyDaysAgo)
                        isNull("accepted_by")
                    }
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<Challenge>()
            Result.success(challenges)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvailableChallengesNearby(
        userLocation: LocationData,
        maxDistanceKm: Double = LocationManager.DEFAULT_MAX_DISTANCE_KM,
        locationManager: LocationManager
    ): Result<List<Challenge>> {
        return try {

            val allChallengesResult = getAvailableChallenges()
            Log.d("Challenge Result", "$allChallengesResult")
            Log.d("Max Distance", "maxDistanceKm = $maxDistanceKm")

            allChallengesResult.fold(
                onSuccess = { challenges ->
                    Log.d("Total Challenges", "Found ${challenges.size} challenges")

                    val nearbyChallenges = challenges.filter { challenge ->
                        if (challenge.latitude == null || challenge.longitude == null) {
                            return@filter false
                        }

                        val distance = locationManager.calculateDistance(
                            userLocation.latitude,
                            userLocation.longitude,
                            challenge.latitude,
                            challenge.longitude
                        )

                        val isInRange = distance <= maxDistanceKm

                        isInRange
                    }.sortedBy { challenge ->
                        if (challenge.latitude != null && challenge.longitude != null) {
                            locationManager.calculateDistance(
                                userLocation.latitude,
                                userLocation.longitude,
                                challenge.latitude,
                                challenge.longitude
                            )
                        } else {
                            Double.MAX_VALUE
                        }
                    }

                    Result.success(nearbyChallenges)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}