package brawijaya.example.sportsync.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import brawijaya.example.sportsync.data.models.Tournament
import brawijaya.example.sportsync.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TournamentUiState(
    val tournaments: List<Tournament> = emptyList(),
    val filteredTournaments: List<Tournament> = emptyList(),
    val selectedCategory: String = "Badminton",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentTournament: Tournament? = null
)

class TournamentViewModel : ViewModel() {

    private val repository = TournamentRepository()

    private val _uiState = MutableStateFlow(TournamentUiState())
    val uiState: StateFlow<TournamentUiState> = _uiState.asStateFlow()

    init {
        loadTournaments()
    }

    fun loadTournaments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            repository.getUpcomingTournaments().fold(
                onSuccess = { tournaments ->
                    _uiState.value = _uiState.value.copy(
                        tournaments = tournaments,
                        filteredTournaments = filterTournaments(tournaments),
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            )
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilter()
    }

    private fun applyFilter() {
        val currentState = _uiState.value
        val filteredTournaments = filterTournaments(currentState.tournaments)
        _uiState.value = currentState.copy(filteredTournaments = filteredTournaments)
    }

    private fun filterTournaments(tournaments: List<Tournament>): List<Tournament> {
        val selectedCategory = _uiState.value.selectedCategory

        return if (selectedCategory == "All") {
            tournaments
        } else {
            tournaments.filter {
                it.type.equals(selectedCategory, ignoreCase = true)
            }
        }
    }

    fun getTournamentById(id: String) {
        viewModelScope.launch {
            repository.getTournamentById(id).fold(
                onSuccess = { tournament ->
                    _uiState.value = _uiState.value.copy(currentTournament = tournament)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(errorMessage = error.message)
                }
            )
        }
    }

    fun refreshTournaments() {
        loadTournaments()
    }
}