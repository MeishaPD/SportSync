package brawijaya.example.sportsync.data.repository

import brawijaya.example.sportsync.data.SupabaseClient
import brawijaya.example.sportsync.data.models.*
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class CourtRepository {

    private val supabase = SupabaseClient.client

    suspend fun getCourts(): Result<List<Court>> {
        return try {
            val courts = supabase
                .from("courts")
                .select {
                    filter {
                        eq("is_available", true)
                    }
                    order("name", order = Order.ASCENDING)
                }
                .decodeList<Court>()
            Result.success(courts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTimeSlots(): Result<List<TimeSlot>> {
        return try {
            val timeSlots = supabase
                .from("time_slots")
                .select {
                    filter {
                        eq("is_active", true)
                    }
                    order("time", order = Order.ASCENDING)
                }
                .decodeList<TimeSlot>()
            Result.success(timeSlots)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourtAvailability(
        courtId: String,
        date: String
    ): Result<List<CourtAvailability>> {
        return try {
            val availability = supabase
                .from("court_availability")
                .select {
                    filter {
                        eq("court_id", courtId)
                        eq("date", date)
                    }
                }
                .decodeList<CourtAvailability>()
            Result.success(availability)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourtsWithAvailability(
        category: String?,
        date: String
    ): Result<List<CourtWithAvailability>> {
        return try {
            coroutineScope {
                val courtsDeferred = async { getCourts() }
                val timeSlotsDeferred = async { getTimeSlots() }

                val courtsResult = courtsDeferred.await()
                val timeSlotsResult = timeSlotsDeferred.await()

                if (courtsResult.isFailure) {
                    return@coroutineScope Result.failure<List<CourtWithAvailability>>(
                        courtsResult.exceptionOrNull() ?: Exception("Failed to fetch courts")
                    )
                }

                if (timeSlotsResult.isFailure) {
                    return@coroutineScope Result.failure<List<CourtWithAvailability>>(
                        timeSlotsResult.exceptionOrNull() ?: Exception("Failed to fetch time slots")
                    )
                }

                val courts = courtsResult.getOrNull() ?: emptyList()
                val timeSlots = timeSlotsResult.getOrNull() ?: emptyList()

                val filteredCourts = if (category != null && category != "All") {
                    courts.filter { court ->
                        court.name.contains(category, ignoreCase = true)
                    }
                } else {
                    courts
                }

                val courtsWithAvailability = filteredCourts.map { court ->
                    async {
                        val availabilityResult = getCourtAvailability(court.id, date)
                        val availability = availabilityResult.getOrNull() ?: emptyList()

                        val availabilityMap = availability.associateBy { it.timeSlotId }

                        val timeSlotsWithAvailability = timeSlots.map { timeSlot ->
                            val courtAvailability = availabilityMap[timeSlot.id]
                            val isAvailable = courtAvailability?.isAvailable ?: true

                            TimeSlotWithAvailability(
                                timeSlot = timeSlot,
                                isAvailable = isAvailable
                            )
                        }

                        CourtWithAvailability(
                            court = court,
                            timeSlots = timeSlotsWithAvailability
                        )
                    }
                }.awaitAll()

                Result.success(courtsWithAvailability)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCourtAvailability(
        courtId: String,
        timeSlotId: String,
        date: String,
        isAvailable: Boolean
    ): Result<Unit> {
        return try {
            val existingAvailability = supabase
                .from("court_availability")
                .select {
                    filter {
                        eq("court_id", courtId)
                        eq("time_slot_id", timeSlotId)
                        eq("date", date)
                    }
                }
                .decodeSingleOrNull<CourtAvailability>()

            if (existingAvailability != null) {
                supabase
                    .from("court_availability")
                    .update({
                        set("is_available", isAvailable)
                        set("updated_at", "now()")
                    }) {
                        filter {
                            eq("court_id", courtId)
                            eq("time_slot_id", timeSlotId)
                            eq("date", date)
                        }
                    }
            } else {
                supabase
                    .from("court_availability")
                    .insert(
                        mapOf(
                            "court_id" to courtId,
                            "time_slot_id" to timeSlotId,
                            "date" to date,
                            "is_available" to isAvailable
                        )
                    )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}