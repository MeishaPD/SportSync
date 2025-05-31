package brawijaya.example.sportsync.utils

import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import brawijaya.example.sportsync.data.models.BookingItem
import brawijaya.example.sportsync.data.models.TimeSlotData
import brawijaya.example.sportsync.data.models.CourtData
import brawijaya.example.sportsync.ui.viewmodels.PaymentType
import androidx.navigation.NavBackStackEntry

data class ParsedPaymentParams(
    val courtId: String,
    val courtName: String,
    val selectedDate: String,
    val paymentType: PaymentType,
    val totalAmount: Int,
    val selectedTimeSlots: List<BookingItem>,
    val courtData: CourtData
)

object NavigationUtils {

    fun decodeUrl(encodedText: String): String {
        return URLDecoder.decode(encodedText, StandardCharsets.UTF_8.toString())
    }

    fun parseBookingItems(json: String): List<BookingItem> {
        return try {
            val gson = Gson()
            val type = object : TypeToken<List<BookingItem>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun parseTimeSlotData(json: String): List<TimeSlotData> {
        return try {
            val gson = Gson()
            val type = object : TypeToken<List<TimeSlotData>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun parsePaymentParams(backStackEntry: NavBackStackEntry): ParsedPaymentParams {
        val courtId = decodeUrl(
            backStackEntry.arguments?.getString("courtId") ?: ""
        )
        val courtName = decodeUrl(
            backStackEntry.arguments?.getString("courtName") ?: ""
        )
        val selectedDate = decodeUrl(
            backStackEntry.arguments?.getString("selectedDate") ?: ""
        )
        val paymentTypeString = backStackEntry.arguments?.getString("paymentType") ?: "FULL"
        val totalAmount = backStackEntry.arguments?.getInt("totalAmount") ?: 0
        val timeSlotsJson = decodeUrl(
            backStackEntry.arguments?.getString("timeSlots") ?: "[]"
        )
        val courtAddress = decodeUrl(
            backStackEntry.arguments?.getString("courtAddress") ?: ""
        )
        val pricePerHour = decodeUrl(
            backStackEntry.arguments?.getString("pricePerHour") ?: ""
        )
        val availableTimeSlotsJson = decodeUrl(
            backStackEntry.arguments?.getString("availableTimeSlots") ?: "[]"
        )

        val selectedTimeSlots = parseBookingItems(timeSlotsJson)
        val availableTimeSlots = parseTimeSlotData(availableTimeSlotsJson)

        val paymentType = try {
            PaymentType.valueOf(paymentTypeString)
        } catch (e: Exception) {
            PaymentType.FULL
        }

        val courtData = CourtData(
            id = courtId,
            name = courtName,
            address = courtAddress,
            pricePerHour = pricePerHour,
            timeSlots = availableTimeSlots,
            isAvailable = true
        )

        return ParsedPaymentParams(
            courtId = courtId,
            courtName = courtName,
            selectedDate = selectedDate,
            paymentType = paymentType,
            totalAmount = totalAmount,
            selectedTimeSlots = selectedTimeSlots,
            courtData = courtData
        )
    }
}