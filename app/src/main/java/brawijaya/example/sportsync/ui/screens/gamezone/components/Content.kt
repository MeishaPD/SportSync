package brawijaya.example.sportsync.ui.screens.gamezone.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.ui.viewmodels.TournamentUiState

@Composable
fun GameZoneContent(
    uiState: TournamentUiState,
    onCategorySelected: (String) -> Unit,
    onRefresh: () -> Unit,
    onTournamentClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Tournament",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Text(
            text = "Check the newest schedule",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(Modifier.size(8.dp))

        TournamentCategoryFilters(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = onCategorySelected
        )

        Spacer(Modifier.size(8.dp))

        Text(
            text = "Tournaments Schedule",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "— Mid-year Tournament 2025",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Error loading tournaments",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = uiState.errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = onRefresh) {
                            Text("Try Again")
                        }
                    }
                }
            }

            uiState.filteredTournaments.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "No tournaments found",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Try selecting a different category or check back later",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = onRefresh) {
                            Text("Refresh")
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.filteredTournaments) { tournament ->
                        TournamentCard(
                            team1Name = tournament.team_1,
                            team2Name = tournament.team_2,
                            date = tournament.getDisplayDate(),
                            time = tournament.getFormattedTime(),
                            onClick = {
                                tournament.id?.let { onTournamentClick(it) }
                            }
                        )
                    }
                }
            }
        }
    }
}