package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.Booking
import brawijaya.example.sportsync.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PaymentSuccessUiState(
    val isLoading: Boolean = false,
    val booking: Booking? = null,
    val courtName: String? = null,
    val timeSlots: List<String> = emptyList(),
    val error: String? = null
)

class PaymentSuccessViewModel(
    private val bookingRepository: BookingRepository = BookingRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentSuccessUiState())
    val uiState: StateFlow<PaymentSuccessUiState> = _uiState.asStateFlow()

    fun loadBookingDetails(bookingId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val bookingResult = bookingRepository.getBooking(bookingId)
                if (bookingResult.isFailure) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load booking: ${bookingResult.exceptionOrNull()?.message}"
                    )
                    return@launch
                }

                val booking = bookingResult.getOrNull()!!

                val courtResult = bookingRepository.getCourt(booking.courtId)
                val courtName = if (courtResult.isSuccess) {
                    courtResult.getOrNull()?.name ?: "Unknown Court"
                } else {
                    "Unknown Court"
                }

                val timeSlotsResult = bookingRepository.getBookingTimeSlots(bookingId)
                val timeSlotStrings = if (timeSlotsResult.isSuccess) {
                    val bookingTimeSlots = timeSlotsResult.getOrNull() ?: emptyList()
                    val timeSlotDetails = mutableListOf<String>()

                    bookingTimeSlots.forEach { bookingTimeSlot ->
                        val timeSlotResult = bookingRepository.getTimeSlot(bookingTimeSlot.timeSlotId)
                        if (timeSlotResult.isSuccess) {
                            val timeSlot = timeSlotResult.getOrNull()
                            timeSlotDetails.add(formatTime(timeSlot?.time.toString()))
                        }
                    }
                    timeSlotDetails.sorted()
                } else {
                    emptyList()
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    booking = booking,
                    courtName = courtName,
                    timeSlots = timeSlotStrings,
                    error = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "An error occurred: ${e.message}"
                )
            }
        }
    }

    private fun formatTime(timeString: String): String {
        return try {
            val timeParts = timeString.split(":")
            if (timeParts.size >= 2) {
                "${timeParts[0]}:${timeParts[1]}"
            } else {
                timeString
            }
        } catch (e: Exception) {
            timeString
        }
    }
}