package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.BookingItem
import brawijaya.example.sportsync.data.models.CourtData
import brawijaya.example.sportsync.data.models.TimeSlot
import brawijaya.example.sportsync.data.models.toCourtData
import brawijaya.example.sportsync.data.repository.CourtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class BookCourtUiState(
    val court: CourtData? = null,
    val selectedTimeSlots: List<BookingItem> = emptyList(),
    val totalPrice: Int = 0,
    val isLoading: Boolean = false,
    val showTimeSlotDialog: Boolean = false,
    val availableTimeSlots: List<TimeSlot> = emptyList(),
    val paymentType: PaymentType = PaymentType.FULL,
    val selectedDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    val displayDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")),
    val error: String? = null
)

enum class PaymentType {
    FULL, DOWN
}

class BookCourtViewModel(
    private val courtRepository: CourtRepository = CourtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookCourtUiState())
    val uiState: StateFlow<BookCourtUiState> = _uiState.asStateFlow()

    fun loadCourt(courtId: String, initialTimeSlot: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val currentState = _uiState.value

                val result = courtRepository.getCourtsWithAvailability(
                    category = null,
                    date = currentState.selectedDate
                )

                result.fold(
                    onSuccess = { courtsWithAvailability ->
                        val courtWithAvailability = courtsWithAvailability.find {
                            it.court.id == courtId
                        }

                        if (courtWithAvailability != null) {
                            val courtData = courtWithAvailability.toCourtData()

                            val availableTimeSlots = courtWithAvailability.timeSlots
                                .filter { it.isAvailable }
                                .map { it.timeSlot }

                            val initialBookings = if (initialTimeSlot != null) {
                                listOf(
                                    BookingItem(
                                        date = currentState.displayDate,
                                        timeSlot = initialTimeSlot,
                                        price = extractPrice(courtData.pricePerHour)
                                    )
                                )
                            } else emptyList()

                            _uiState.update {
                                it.copy(
                                    court = courtData,
                                    selectedTimeSlots = initialBookings,
                                    totalPrice = initialBookings.sumOf { booking -> booking.price },
                                    availableTimeSlots = availableTimeSlots,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Court not found"
                                )
                            }
                        }
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Failed to load court data"
                            )
                        }
                    }
                )
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
        val court = _uiState.value.court ?: return
        val price = extractPrice(court.pricePerHour)

        val isAlreadySelected = _uiState.value.selectedTimeSlots.any { it.timeSlot == timeSlot }
        if (isAlreadySelected) {
            _uiState.update { it.copy(showTimeSlotDialog = false) }
            return
        }

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