package brawijaya.example.sportsync.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BookingStatus {
    @SerialName("pending")
    PENDING,
    @SerialName("confirmed")
    CONFIRMED,
    @SerialName("cancelled")
    CANCELLED,
    @SerialName("completed")
    COMPLETED
}

@Serializable
data class Booking(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("court_id")
    val courtId: String,
    val date: String,
    @SerialName("total_price")
    val totalPrice: Double,
    @SerialName("payment_method")
    val paymentMethod: String,
    val status: BookingStatus = BookingStatus.PENDING,
    @SerialName("booking_reference")
    val bookingReference: String? = null,
    val notes: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class BookingTimeSlot(
    val id: String? = null,
    @SerialName("booking_id")
    val bookingId: String,
    @SerialName("time_slot_id")
    val timeSlotId: String,
    val price: Double,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class CreateBookingRequest(
    @SerialName("court_id")
    val courtId: String,
    val date: String,
    @SerialName("total_price")
    val totalPrice: Double,
    @SerialName("payment_method")
    val paymentMethod: String,
    @SerialName("time_slots")
    val timeSlots: List<BookingTimeSlotRequest>,
    val notes: String? = null
)

@Serializable
data class BookingInsertRequest(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("court_id")
    val courtId: String,
    val date: String,
    @SerialName("total_price")
    val totalPrice: Double,
    @SerialName("payment_method")
    val paymentMethod: String,
    val status: String,
    @SerialName("booking_reference")
    val bookingReference: String,
    val notes: String?
)

@Serializable
data class BookingTimeSlotInsertRequest(
    @SerialName("booking_id")
    val bookingId: String,
    @SerialName("time_slot_id")
    val timeSlotId: String,
    val price: Double
)

@Serializable
data class BookingTimeSlotRequest(
    @SerialName("time_slot_id")
    val timeSlotId: String,
    val price: Double
)


@Serializable
data class CourtAvailabilityUpdate(
    @SerialName("is_available")
    val isAvailable: Boolean,

    @SerialName("updated_at")
    val updatedAt: String
)