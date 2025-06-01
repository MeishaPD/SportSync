package brawijaya.example.sportsync.ui.screens.gamezone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import brawijaya.example.sportsync.R
import brawijaya.example.sportsync.ui.components.FilterChip

@Composable
fun TournamentCategoryFilters(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("All", "Badminton", "Soccer", "Mini Soccer", "Basketball", "Volleyball")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFFCCD78), CircleShape)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.filter),
                    contentDescription = "Filter",
                    tint = Color.Black
                )
            }
        }

        items(categories.size) { index ->
            val category = categories[index]
            FilterChip(
                text = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}