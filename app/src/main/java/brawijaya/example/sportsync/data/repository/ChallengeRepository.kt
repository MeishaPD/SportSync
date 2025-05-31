package brawijaya.example.sportsync.data.repository

import brawijaya.example.sportsync.data.SupabaseClient
import brawijaya.example.sportsync.data.models.Challenge
import io.github.jan.supabase.postgrest.*
import io.github.jan.supabase.postgrest.query.*
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    suspend fun getChallengesByCategory(category: String): Result<List<Challenge>> {
        return try {
            val challenges = supabase
                .from("challenge")
                .select {
                    filter {
                        eq("category", category)
                    }
                    order("created_at", order = Order.ASCENDING)
                }
                .decodeList<Challenge>()
            Result.success(challenges)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChallengesByDate(date: String): Result<List<Challenge>> {
        return try {
            val challenges = supabase
                .from("challenge")
                .select {
                    filter {
                        eq("date", date)
                    }
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<Challenge>()
            Result.success(challenges)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChallengesByCategoryAndDate(category: String, date: String): Result<List<Challenge>> {
        return try {
            val challenges = supabase
                .from("challenge")
                .select {
                    filter {
                        eq("category", category)
                        eq("date", date)
                    }
                    order("created_at", order = Order.ASCENDING)
                }
                .decodeList<Challenge>()
            Result.success(challenges)
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
}