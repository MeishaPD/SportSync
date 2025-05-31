package brawijaya.example.sportsync.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.CourtData
import brawijaya.example.sportsync.data.models.toCourtData
import brawijaya.example.sportsync.data.repository.CourtRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CourtUiState(
    val courts: List<CourtData> = emptyList(),
    val categories: List<String> = listOf("All", "Badminton", "Soccer", "Mini Soccer"),
    val selectedCategory: String = "All",
    val selectedDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    val displayDate: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class CourtViewModel(
    private val repository: CourtRepository = CourtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourtUiState())
    val uiState: StateFlow<CourtUiState> = _uiState.asStateFlow()

    init {
        loadCourts()
    }

    fun loadCourts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val currentState = _uiState.value
                val result = repository.getCourtsWithAvailability(
                    category = if (currentState.selectedCategory == "All") null else currentState.selectedCategory,
                    date = currentState.selectedDate
                )

                result.fold(
                    onSuccess = { courtsWithAvailability ->
                        val courtData = courtsWithAvailability.map { it.toCourtData() }
                        Log.d("Courts", "$courtData")
                        _uiState.update {
                            it.copy(
                                courts = courtData,
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Unknown error occurred"
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

    fun onCategorySelected(category: String) {
        if (_uiState.value.selectedCategory != category) {
            _uiState.update { it.copy(selectedCategory = category) }
            loadCourts()
        }
    }

    fun onDateSelected(displayDate: String) {
        val formattedDate = if (displayDate.isNotEmpty()) {
            try {
                val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val localDate = LocalDate.parse(displayDate, inputFormatter)
                localDate.format(outputFormatter)
            } catch (e: Exception) {
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }
        } else {
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        _uiState.update {
            it.copy(
                selectedDate = formattedDate,
                displayDate = displayDate
            )
        }
        loadCourts()
    }

    fun bookTimeSlot(courtId: String, timeSlotId: String) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                val result = repository.updateCourtAvailability(
                    courtId = courtId,
                    timeSlotId = timeSlotId,
                    date = currentState.selectedDate,
                    isAvailable = false
                )

                result.fold(
                    onSuccess = {
                        loadCourts()
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(error = "Failed to book time slot: ${exception.message}")
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Failed to book time slot: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun retry() {
        loadCourts()
    }
}