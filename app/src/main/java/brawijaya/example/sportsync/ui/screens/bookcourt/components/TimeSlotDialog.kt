package brawijaya.example.sportsync.ui.screens.bookcourt.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import brawijaya.example.sportsync.data.models.TimeSlot
import brawijaya.example.sportsync.data.models.TimeSlotData
import brawijaya.example.sportsync.ui.screens.findcourt.components.TimeSlotChip

@Composable
fun TimeSlotDialog(
    timeSlots: List<TimeSlot>,
    onTimeSlotSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Select Time Slot",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val chunkedTimeSlots = timeSlots.chunked(3)
                chunkedTimeSlots.forEach { rowSlots ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowSlots.forEach { timeSlot ->
                            val timeSlotData = TimeSlotData(
                                id = timeSlot.id,
                                time = timeSlot.time,
                                isAvailable = timeSlot.isActive
                            )
                            Box(modifier = Modifier.weight(1f)) {
                                TimeSlotChip(
                                    timeSlot = timeSlotData,

                                    isSelected = false,
                                    onClick = {
                                        onTimeSlotSelected(timeSlot.time)
                                    },
                                )
                            }
                        }
                        repeat(3 - rowSlots.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}