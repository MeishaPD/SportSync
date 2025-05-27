package brawijaya.example.sportsync.ui.screens.findcourt.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import brawijaya.example.sportsync.data.models.CourtData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CourtCard(
    court: CourtData,
    onTimeSlotSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedTimeSlot by remember { mutableStateOf<String?>(null) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "rotation"
    )

    val timeSlotChips = remember(court.timeSlots) {
        court.timeSlots.map { timeSlot ->
            timeSlot to @Composable {
                TimeSlotChip(
                    timeSlot = timeSlot,
                    isSelected = selectedTimeSlot == timeSlot.time,
                    onClick = {
                        if (timeSlot.isAvailable) {
                            selectedTimeSlot = timeSlot.time
                            onTimeSlotSelected(timeSlot.time)
                        }
                    }
                )
            }
        }
    }

    Column {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(300)
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape =  RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (court.isAvailable) Color(0xFFFCCD78) else Color(0xFFE0E0E0)
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = court.isAvailable) {
                            if (court.isAvailable) {
                                isExpanded = !isExpanded
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = court.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (court.isAvailable) Color.Black else Color.Gray
                        )

                        Text(
                            text = court.address,
                            fontSize = 14.sp,
                            color = if (court.isAvailable) Color.Black.copy(alpha = 0.7f) else Color.Gray,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        if (court.isAvailable) {
                            Text(
                                text = court.pricePerHour,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = "Lihat Jadwal",
                                    fontSize = 12.sp,
                                    color = Color.Black.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expand",
                                    tint = Color.Black,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .rotate(rotationAngle)
                                )
                            }
                        } else {
                            Text(
                                text = "Tidak Tersedia",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = isExpanded && court.isAvailable,
            enter = expandVertically(
                animationSpec = tween(300)
            ),
            exit = shrinkVertically(
                animationSpec = tween(300)
            ),
            modifier = Modifier.offset( y = (-32).dp).zIndex(-10f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    ).border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 32.dp,
                        bottom = 16.dp
                    )
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timeSlotChips.forEach { (timeSlot, chipComposable) ->
                        chipComposable()
                    }
                }
            }
        }
    }
}