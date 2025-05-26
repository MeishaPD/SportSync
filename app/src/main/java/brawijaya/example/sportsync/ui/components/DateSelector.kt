package brawijaya.example.sportsync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DateSelector() {
    val today = LocalDate.now()
    val dates = (0..6).map { today.plusDays(it.toLong()) }
    var selectedDate by remember { mutableStateOf(today.plusDays(2)) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dates.size) { index ->
            val date = dates[index]
            DateItem(
                date = date,
                isSelected = date == selectedDate,
                onClick = { selectedDate = date }
            )
        }
    }
}

@Composable
fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
    val dayNumber = date.dayOfMonth

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(50.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(44.dp))
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(44.dp)
            )
            .background(
                if (isSelected) Color(0xFFFBBB46) else Color.Transparent
            )
            .clickable { onClick() }
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = date.format(dayFormatter),
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = dayNumber.toString(),
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}
