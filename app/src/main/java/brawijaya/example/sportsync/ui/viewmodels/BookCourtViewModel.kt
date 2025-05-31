package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.BookingItem
import brawijaya.example.sportsync.data.models.CourtData
import brawijaya.example.sportsync.data.models.TimeSlot
import brawijaya.example.sportsync.data.repository.CourtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class BookCourtUiState(
    val courtData: CourtData? = null,
    val selectedTimeSlots: List<BookingItem> = emptyList(),
    val totalPrice: Int = 0,
    val isLoading: Boolean = false,
    val showTimeSlotDialog: Boolean = false,
    val availableTimeSlots: List<TimeSlot> = emptyList(),
    val paymentType: PaymentType = PaymentType.FULL,
    val selectedDate: String = "",
    val displayDate: String = "",
    val error: String? = null,
    val navigateToPayment: Boolean = false,
)

enum class PaymentType {
    FULL, DOWN
}

class BookCourtViewModel(
    private val courtRepository: CourtRepository = CourtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookCourtUiState())
    val uiState: StateFlow<BookCourtUiState> = _uiState.asStateFlow()

    fun initializeCourtData(
        courtId: String,
        courtName: String,
        address: String,
        pricePerHour: String,
        date: String,
        initialTimeSlot: String? = null
    ) {
        val courtData = CourtData(
            id = courtId,
            name = courtName,
            address = address,
            pricePerHour = pricePerHour,
            timeSlots = emptyList(),
            isAvailable = true
        )

        val displayDate = try {
            val parsedDate = LocalDate.parse(date)
            parsedDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))
        } catch (e: Exception) {
            date
        }

        val initialBookings = if (initialTimeSlot != null) {
            listOf(
                BookingItem(
                    date = displayDate,
                    timeSlot = initialTimeSlot,
                    price = extractPrice(pricePerHour)
                )
            )
        } else emptyList()

        _uiState.update {
            it.copy(
                courtData = courtData,
                selectedDate = date,
                displayDate = displayDate,
                selectedTimeSlots = initialBookings,
                totalPrice = initialBookings.sumOf { booking -> booking.price }
            )
        }

        loadAvailableTimeSlots(courtId, date)
    }

    private fun loadAvailableTimeSlots(courtId: String, date: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val timeSlotsResult = courtRepository.getTimeSlots()
                if (timeSlotsResult.isFailure) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to load time slots"
                        )
                    }
                    return@launch
                }

                val allTimeSlots = timeSlotsResult.getOrNull() ?: emptyList()

                val availabilityResult = courtRepository.getCourtAvailability(courtId, date)
                if (availabilityResult.isFailure) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to load court availability"
                        )
                    }
                    return@launch
                }

                val availability = availabilityResult.getOrNull() ?: emptyList()
                val availabilityMap = availability.associateBy { it.timeSlotId }

                val availableTimeSlots = allTimeSlots.filter { timeSlot ->
                    val courtAvailability = availabilityMap[timeSlot.id]
                    courtAvailability?.isAvailable ?: true
                }

                _uiState.update {
                    it.copy(
                        availableTimeSlots = availableTimeSlots,
                        isLoading = false,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun showTimeSlotDialog() {
        _uiState.update { it.copy(showTimeSlotDialog = true) }
    }

    fun hideTimeSlotDialog() {
        _uiState.update { it.copy(showTimeSlotDialog = false) }
    }

    fun addTimeSlot(timeSlot: String) {
        val courtData = _uiState.value.courtData ?: return
        val price = extractPrice(courtData.pricePerHour)

        val newBooking = BookingItem(
            date = _uiState.value.displayDate,
            timeSlot = timeSlot,
            price = price
        )

        val updatedBookings = _uiState.value.selectedTimeSlots + newBooking

        _uiState.update {
            it.copy(
                selectedTimeSlots = updatedBookings,
                totalPrice = updatedBookings.sumOf { booking -> booking.price },
                showTimeSlotDialog = false
            )
        }
    }

    fun removeTimeSlot(index: Int) {
        val updatedBookings = _uiState.value.selectedTimeSlots.toMutableList()
        if (index in updatedBookings.indices) {
            updatedBookings.removeAt(index)

            _uiState.update {
                it.copy(
                    selectedTimeSlots = updatedBookings,
                    totalPrice = updatedBookings.sumOf { booking -> booking.price }
                )
            }
        }
    }

    fun setPaymentType(paymentType: PaymentType) {
        _uiState.update { it.copy(paymentType = paymentType) }
    }

    private fun extractPrice(priceString: String): Int {
        return priceString.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 100000
    }
}