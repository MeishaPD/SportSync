package brawijaya.example.sportsync.data.models

import androidx.annotation.DrawableRes
import brawijaya.example.sportsync.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Court(
    val id: String,
    val name: String,
    val address: String,
    @SerialName("price_per_hour")
    val pricePerHour: Double,
    @SerialName("is_available")
    val isAvailable: Boolean = true,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class TimeSlot(
    val id: String,
    val time: String,
    @SerialName("is_active")
    val isActive: Boolean = true,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class CourtAvailability(
    val id: String,
    @SerialName("court_id")
    val courtId: String,
    @SerialName("time_slot_id")
    val timeSlotId: String,
    val date: String,
    @SerialName("is_available")
    val isAvailable: Boolean = true,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

data class CourtWithAvailability(
    val court: Court,
    val timeSlots: List<TimeSlotWithAvailability>
)

data class TimeSlotWithAvailability(
    val timeSlot: TimeSlot,
    val isAvailable: Boolean
)

data class CourtData(
    val id: String,
    val name: String,
    val address: String,
    val pricePerHour: String,
    val timeSlots: List<TimeSlotData>,
    val isAvailable: Boolean = true
)

data class TimeSlotData(
    val id: String,
    val time: String,
    val isAvailable: Boolean = true
)

fun Court.toPriceString(): String = "Rp ${String.format("%,.0f", pricePerHour)}/hour"

fun CourtWithAvailability.toCourtData(): CourtData {
    return CourtData(
        id = court.id,
        name = court.name,
        address = court.address,
        pricePerHour = court.toPriceString(),
        timeSlots = timeSlots.map { it.toTimeSlotData() },
        isAvailable = court.isAvailable && timeSlots.any { it.isAvailable }
    )
}

fun TimeSlotWithAvailability.toTimeSlotData(): TimeSlotData {
    return TimeSlotData(
        id = timeSlot.id,
        time = timeSlot.time,
        isAvailable = isAvailable && timeSlot.isActive
    )
}

@Serializable
data class BookingItem(
    val date: String,
    val timeSlot: String,
    val price: Int
)

@Serializable
enum class PaymentMethod(val displayName: String, @DrawableRes val iconRes: Int) {
    OVO("OVO", R.drawable.ovo),
    PAYPAL("PayPal", R.drawable.paypal),
    VISA("VISA", R.drawable.visa),
    QRIS("QRIS", R.drawable.qr),
    GOPAY("GoPay", R.drawable.gopay),
    APPLEPAY("ApplePay", R.drawable.applepay)
}
