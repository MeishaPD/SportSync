package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import brawijaya.example.sportsync.data.models.BookingStatus
import brawijaya.example.sportsync.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PaymentDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class PaymentDetailViewModel(
    private val bookingRepository: BookingRepository = BookingRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentDetailUiState())
    val uiState: StateFlow<PaymentDetailUiState> = _uiState.asStateFlow()

    suspend fun updateBookingStatusToCompleted(bookingId: String): Boolean {
        return try {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            val result = bookingRepository.updateBookingStatus(
                bookingId = bookingId,
                status = BookingStatus.COMPLETED
            )

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    error = null
                )
                true
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to update booking status: ${result.exceptionOrNull()?.message}",
                    isSuccess = false
                )
                false
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Error updating booking status: ${e.message}",
                isSuccess = false
            )
            false
        }
    }
}