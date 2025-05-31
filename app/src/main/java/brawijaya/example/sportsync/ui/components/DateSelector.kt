package brawijaya.example.sportsync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun DateSelector(
    onDateSelected: (String) -> Unit = {}
) {
    val today = LocalDate.now()
    val dates = remember {
        (0..6).map { dayOffset ->
            today.plusDays(dayOffset.toLong())
        }
    }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(dates) { date ->
            DateItem(
                date = date,
                isSelected = selectedDate == date,
                onClick = {
                    selectedDate = if (selectedDate == date) null else date
                    val displayDate = if (selectedDate != null) {
                        selectedDate!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    } else {
                        ""
                    }
                    onDateSelected(displayDate)
                }
            )
        }
    }
}

@Composable
private fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dayName = date.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))
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
            text = dayName,
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