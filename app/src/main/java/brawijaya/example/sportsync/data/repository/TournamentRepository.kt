package brawijaya.example.sportsync.data.repository

import brawijaya.example.sportsync.data.SupabaseClient
import brawijaya.example.sportsync.data.models.Tournament
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import java.time.LocalDate

class TournamentRepository {
    private val supabase = SupabaseClient.client

    suspend fun getAllTournaments(): Result<List<Tournament>> {
        return try {
            val tournaments = supabase
                .from("tournament")
                .select {
                    order("date", order = Order.ASCENDING)
                    order("time", order = Order.ASCENDING)
                }
                .decodeList<Tournament>()
            Result.success(tournaments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTournamentsByType(type: String): Result<List<Tournament>> {
        return try {
            val tournaments = supabase
                .from("tournament")
                .select {
                    filter {
                        eq("type", type)
                    }
                    order("date", order = Order.ASCENDING)
                    order("time", order = Order.ASCENDING)
                }
                .decodeList<Tournament>()
            Result.success(tournaments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUpcomingTournaments(): Result<List<Tournament>> {
        return try {
            val today = LocalDate.now().toString()
            val tournaments = supabase
                .from("tournament")
                .select {
                    filter {
                        gte("date", today)
                    }
                    order("date", order = Order.ASCENDING)
                    order("time", order = Order.ASCENDING)
                }
                .decodeList<Tournament>()
            Result.success(tournaments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTournamentById(id: String): Result<Tournament> {
        return try {
            val tournament = supabase
                .from("tournament")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<Tournament>()
            Result.success(tournament)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}