package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.BookingItem
import brawijaya.example.sportsync.data.models.BookingTimeSlotRequest
import brawijaya.example.sportsync.data.models.CreateBookingRequest
import brawijaya.example.sportsync.data.models.PaymentMethod
import brawijaya.example.sportsync.data.repository.BookingRepository
import brawijaya.example.sportsync.data.repository.CourtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PaymentUiState(
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.QRIS,
    val isProcessingPayment: Boolean = false,
    val paymentSuccess: Boolean = false,
    val paymentError: String? = null,
    val bookingId: String? = null
)

class PaymentViewModel(
    private val bookingRepository: BookingRepository = BookingRepository(),
    private val courtRepository: CourtRepository = CourtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun createBooking(
        courtId: String,
        courtName: String,
        selectedDate: String,
        totalAmount: Int,
        selectedTimeSlots: List<BookingItem>,
        paymentType: PaymentType,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessingPayment = true, paymentError = null) }

            try {
                val timeSlotsResult = courtRepository.getTimeSlots()
                if (timeSlotsResult.isFailure) {
                    val error = "Failed to load time slots"
                    _uiState.update {
                        it.copy(
                            isProcessingPayment = false,
                            paymentError = error
                        )
                    }
                    onError(error)
                    return@launch
                }

                val availableTimeSlots = timeSlotsResult.getOrNull() ?: emptyList()
                val timeSlotMap = availableTimeSlots.associateBy { it.time }

                val bookingTimeSlots = selectedTimeSlots.mapNotNull { bookingItem ->
                    val timeSlot = timeSlotMap[bookingItem.timeSlot]
                    if (timeSlot != null) {
                        BookingTimeSlotRequest(
                            timeSlotId = timeSlot.id,
                            price = bookingItem.price.toDouble()
                        )
                    } else {
                        null
                    }
                }

                if (bookingTimeSlots.size != selectedTimeSlots.size) {
                    val error = "Some time slots could not be found"
                    _uiState.update {
                        it.copy(
                            isProcessingPayment = false,
                            paymentError = error
                        )
                    }
                    onError(error)
                    return@launch
                }

                val paymentAmount = if (paymentType == PaymentType.DOWN) {
                    totalAmount / 2.0
                } else {
                    totalAmount.toDouble()
                }

                val createBookingRequest = CreateBookingRequest(
                    courtId = courtId,
                    date = selectedDate,
                    totalPrice = paymentAmount,
                    paymentMethod = PaymentMethod.QRIS.displayName,
                    timeSlots = bookingTimeSlots,
                    notes = "Booking for $courtName - Payment Type: ${paymentType.name}"
                )

                val result = bookingRepository.createBooking(createBookingRequest)

                if (result.isSuccess) {
                    val bookingId = result.getOrNull()!!
                    _uiState.update {
                        it.copy(
                            isProcessingPayment = false,
                            paymentSuccess = true,
                            bookingId = bookingId
                        )
                    }
                    onSuccess(bookingId)
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Failed to create booking"
                    _uiState.update {
                        it.copy(
                            isProcessingPayment = false,
                            paymentError = error
                        )
                    }
                    onError(error)
                }

            } catch (e: Exception) {
                val error = e.message ?: "An unexpected error occurred"
                _uiState.update {
                    it.copy(
                        isProcessingPayment = false,
                        paymentError = error
                    )
                }
                onError(error)
            }
        }
    }
}