package brawijaya.example.sportsync.ui.screens.findcourt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brawijaya.example.sportsync.data.models.TimeSlot

@Composable
fun TimeSlotChip(
    timeSlot: TimeSlot,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = when {
                    !timeSlot.isAvailable -> Color(0xFFE0E0E0)
                    isSelected -> Color(0xFFFBBB46)
                    else -> Color(0xFFFCCD78)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = timeSlot.isAvailable) { onClick() }
            .width(80.dp)
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = timeSlot.time,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (timeSlot.isAvailable) Color.Black else Color.Gray
        )
    }
}