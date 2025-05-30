package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import brawijaya.example.sportsync.data.models.BookingItem
import brawijaya.example.sportsync.data.models.CourtData
import brawijaya.example.sportsync.data.models.TimeSlot
import brawijaya.example.sportsync.data.repository.CourtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BookCourtUiState(
    val court: CourtData? = null,
    val selectedTimeSlots: List<BookingItem> = emptyList(),
    val totalPrice: Int = 0,
    val isLoading: Boolean = false,
    val showTimeSlotDialog: Boolean = false,
    val availableTimeSlots: List<TimeSlot> = emptyList(),
    val paymentType: PaymentType = PaymentType.FULL,
    val selectedDate: String = "Friday, 14 November 2025"
)

enum class PaymentType {
    FULL, DOWN
}

class BookCourtViewModel(
    private val courtRepository: CourtRepository = CourtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookCourtUiState())
    val uiState: StateFlow<BookCourtUiState> = _uiState.asStateFlow()

    fun loadCourt(courtName: String, initialTimeSlot: String? = null) {
        val court = courtRepository.getCourtByName(courtName)
        val initialBookings = if (initialTimeSlot != null && court != null) {
            listOf(
                BookingItem(
                    date = _uiState.value.selectedDate,
                    timeSlot = initialTimeSlot,
                    price = extractPrice(court.pricePerHour)
                )
            )
        } else emptyList()

        _uiState.value = _uiState.value.copy(
            court = court,
            selectedTimeSlots = initialBookings,
            totalPrice = initialBookings.sumOf { it.price },
            availableTimeSlots = court?.timeSlots ?: emptyList()
        )
    }

    fun showTimeSlotDialog() {
        _uiState.value = _uiState.value.copy(showTimeSlotDialog = true)
    }

    fun hideTimeSlotDialog() {
        _uiState.value = _uiState.value.copy(showTimeSlotDialog = false)
    }

    fun addTimeSlot(timeSlot: String) {
        val court = _uiState.value.court ?: return
        val price = extractPrice(court.pricePerHour)

        val newBooking = BookingItem(
            date = _uiState.value.selectedDate,
            timeSlot = timeSlot,
            price = price
        )

        val updatedBookings = _uiState.value.selectedTimeSlots + newBooking

        _uiState.value = _uiState.value.copy(
            selectedTimeSlots = updatedBookings,
            totalPrice = updatedBookings.sumOf { it.price },
            showTimeSlotDialog = false
        )
    }

    fun removeTimeSlot(index: Int) {
        val updatedBookings = _uiState.value.selectedTimeSlots.toMutableList()
        updatedBookings.removeAt(index)

        _uiState.value = _uiState.value.copy(
            selectedTimeSlots = updatedBookings,
            totalPrice = updatedBookings.sumOf { it.price }
        )
    }

    fun setPaymentType(paymentType: PaymentType) {
        _uiState.value = _uiState.value.copy(paymentType = paymentType)
    }

    private fun extractPrice(priceString: String): Int {
        return priceString.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 100000
    }
}
