package brawijaya.example.sportsync.data.models

data class TimeSlot(
    val time: String,
    val isAvailable: Boolean = true
)

data class CourtData(
    val name: String,
    val address: String,
    val pricePerHour: String,
    val timeSlots: List<TimeSlot>,
    val isAvailable: Boolean = true
)

data class BookingItem(
    val date: String,
    val timeSlot: String,
    val price: Int
)