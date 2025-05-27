package brawijaya.example.sportsync.data.models

import androidx.annotation.DrawableRes
import brawijaya.example.sportsync.R

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

enum class PaymentMethod(val displayName: String, @DrawableRes val iconRes: Int) {
    OVO("OVO", R.drawable.ovo),
    PAYPAL("PayPal", R.drawable.paypal),
    VISA("VISA", R.drawable.visa),
    QRIS("QRIS", R.drawable.qr),
    GOPAY("GoPay", R.drawable.gopay),
    APPLEPAY("ApplePay", R.drawable.applepay)
}
