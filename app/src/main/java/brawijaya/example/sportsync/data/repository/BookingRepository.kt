package brawijaya.example.sportsync.data.repository

import android.util.Log
import brawijaya.example.sportsync.data.SupabaseClient
import brawijaya.example.sportsync.data.models.*
import brawijaya.example.sportsync.utils.BookingUtils
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.coroutineScope
import io.github.jan.supabase.gotrue.auth
import kotlinx.datetime.Clock.System

class BookingRepository {

    private val supabase = SupabaseClient.client

    suspend fun createBooking(request: CreateBookingRequest): Result<String> {
        return try {
            coroutineScope {
                val currentUser = supabase.auth.currentUserOrNull()
                    ?: return@coroutineScope Result.failure(Exception("User not authenticated"))

                val bookingId = BookingUtils.generateBookingId()

                val booking = BookingInsertRequest(
                    id = bookingId,
                    userId = currentUser.id,
                    courtId = request.courtId,
                    date = request.date,
                    totalPrice = request.totalPrice,
                    paymentMethod = request.paymentMethod,
                    status = BookingStatus.PENDING.name.lowercase(),
                    bookingReference = BookingUtils.generateBookingReference(),
                    notes = request.notes
                )

                supabase
                    .from("bookings")
                    .insert(booking)

                val bookingTimeSlots = request.timeSlots.map { timeSlot ->
                    BookingTimeSlotInsertRequest(
                        bookingId = bookingId,
                        timeSlotId = timeSlot.timeSlotId,
                        price = timeSlot.price
                    )
                }

                supabase
                    .from("booking_time_slots")
                    .insert(bookingTimeSlots)

                request.timeSlots.forEach { timeSlot ->
                    val res = updateCourtAvailability(
                        courtId = request.courtId,
                        timeSlotId = timeSlot.timeSlotId,
                        date = request.date,
                        isAvailable = false
                    )
                }

                Result.success(bookingId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBooking(bookingId: String): Result<Booking> {
        return try {
            val booking = supabase
                .from("bookings")
                .select {
                    filter {
                        eq("id", bookingId)
                    }
                }
                .decodeSingle<Booking>()

            Result.success(booking)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingTimeSlots(bookingId: String): Result<List<BookingTimeSlot>> {
        return try {
            val timeSlots = supabase
                .from("booking_time_slots")
                .select {
                    filter {
                        eq("booking_id", bookingId)
                    }
                }
                .decodeList<BookingTimeSlot>()

            Result.success(timeSlots)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Result<Unit> {
        return try {
            supabase
                .from("bookings")
                .update(
                    mapOf("status" to status.name.lowercase())
                ) {
                    filter {
                        eq("id", bookingId)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun updateCourtAvailability(
        courtId: String,
        timeSlotId: String,
        date: String,
        isAvailable: Boolean
    ) {
        try {
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
                val updatePayload = CourtAvailabilityUpdate(
                    isAvailable = isAvailable,
                    updatedAt = System.now().toString()
                )

                val result = supabase
                    .from("court_availability")
                    .update(updatePayload) {
                        filter {
                            eq("court_id", courtId)
                            eq("time_slot_id", timeSlotId)
                            eq("date", date)
                        }
                    }

                Log.d("Supabase", "Update result: $result")
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
        } catch (e: Exception) {
            println("Error updating court availability: ${e.message}")
        }
    }

    suspend fun getCourt(courtId: String): Result<Court> {
        return try {
            val court = supabase
                .from("courts")
                .select {
                    filter {
                        eq("id", courtId)
                    }
                }
                .decodeSingle<Court>()

            Result.success(court)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTimeSlot(timeSlotId: String): Result<TimeSlot> {
        return try {
            val timeSlot = supabase
                .from("time_slots")
                .select {
                    filter {
                        eq("id", timeSlotId)
                    }
                }
                .decodeSingle<TimeSlot>()

            Result.success(timeSlot)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}