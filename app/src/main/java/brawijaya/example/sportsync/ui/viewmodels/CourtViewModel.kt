package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.CourtData
import brawijaya.example.sportsync.data.repository.CourtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CourtUiState(
    val courts: List<CourtData> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = ""
)

class CourtViewModel(
    private val courtRepository: CourtRepository = CourtRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourtUiState())
    val uiState: StateFlow<CourtUiState> = _uiState.asStateFlow()

    init {
        loadCourts()
    }

    fun loadCourts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                courtRepository.getAllCourts().collect { courts ->
                    _uiState.value = _uiState.value.copy(
                        courts = courts,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
}