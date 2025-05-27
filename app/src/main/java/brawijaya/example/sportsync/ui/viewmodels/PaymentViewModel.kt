package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import android.util.Log
import brawijaya.example.sportsync.data.models.BookingItem
import brawijaya.example.sportsync.data.models.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PaymentUiState(
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.QRIS,
    val isProcessingPayment: Boolean = false
)

class PaymentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.value = _uiState.value.copy(selectedPaymentMethod = method)
        Log.d("PaymentViewModel", "Selected payment method: ${method.displayName}")
    }

    fun processPayment(amount: Int, courtName: String, timeSlots: List<BookingItem>, paymentType: PaymentType) {
        _uiState.value = _uiState.value.copy(isProcessingPayment = true)

        Log.d("PaymentViewModel", "Processing payment...")
        Log.d("PaymentViewModel", "Court: $courtName")
        Log.d("PaymentViewModel", "Amount: $amount")
        Log.d("PaymentViewModel", "Payment Type: ${paymentType.name}")
        Log.d("PaymentViewModel", "Method: ${_uiState.value.selectedPaymentMethod.displayName}")
        Log.d("PaymentViewModel", "Time Slots: ${timeSlots.joinToString(", ") { it.timeSlot }}")

        _uiState.value = _uiState.value.copy(isProcessingPayment = false)
    }
}