package brawijaya.example.sportsync.ui.screens.gamezone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameZoneContent() {
    Column(
        modifier = Modifier.padding(16.dp)
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

        TournamentCategoryFilters()

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

        LazyColumn(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            items(12) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.Gray)
                        .fillMaxWidth()
                        .height(70.dp)
                )

                Spacer(Modifier.size(16.dp))
            }
        }

    }
}