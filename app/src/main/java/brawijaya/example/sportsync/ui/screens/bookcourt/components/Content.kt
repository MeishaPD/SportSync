package brawijaya.example.sportsync.ui.screens.bookcourt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import brawijaya.example.sportsync.ui.viewmodels.BookCourtViewModel
import brawijaya.example.sportsync.ui.viewmodels.PaymentType
import brawijaya.example.sportsync.utils.CurrencyUtils.formatCurrency

@Composable
fun BookCourtContent(
    courtName: String,
    initialTimeSlot: String? = null,
    viewModel: BookCourtViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(courtName, initialTimeSlot) {
        viewModel.loadCourt(courtName, initialTimeSlot)
    }

    if (uiState.showTimeSlotDialog) {
        TimeSlotDialog(
            timeSlots = uiState.availableTimeSlots,
            onTimeSlotSelected = { timeSlot ->
                viewModel.addTimeSlot(timeSlot)
            },
            onDismiss = {
                viewModel.hideTimeSlotDialog()
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            uiState.court?.let { court ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFCCD78)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = court.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = court.address,
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp),
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Booking Schedule",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Divider(
                        color = Color.Black,
                        thickness = 2.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Text(
                        text = uiState.selectedDate,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        uiState.selectedTimeSlots.forEachIndexed { index, booking ->
                            Column(

                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = booking.timeSlot,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = formatCurrency(booking.price),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    Button(
                                        onClick = { viewModel.removeTimeSlot(index) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFCCD78)
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(42.dp)
                                            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                                            .clip(RoundedCornerShape(16.dp))
                                    ) {
                                        Text(
                                            text = "Delete",
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        )
                                    }

                                    Button(
                                        onClick = { },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFCCD78)
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(42.dp)
                                            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                                            .clip(RoundedCornerShape(16.dp))
                                    ) {
                                        Text(
                                            text = "Edit",
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        )
                                    }
                                }

                            }
                        }
                    }

                    Divider(
                        color = Color.Black,
                        thickness = 2.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.showTimeSlotDialog() }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add Booking",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCCD78)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Payment Summary",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Rental Fee",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = formatCurrency(uiState.totalPrice),
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Additional Service Fee",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Rp0",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    Divider(
                        color = Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = formatCurrency(uiState.totalPrice),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Set Payment",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Divider(
                        color = Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = uiState.paymentType == PaymentType.FULL,
                                    onClick = { viewModel.setPaymentType(PaymentType.FULL) }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = uiState.paymentType == PaymentType.FULL,
                                onClick = { viewModel.setPaymentType(PaymentType.FULL) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFFBBB46)
                                )
                            )
                            Column {
                                Text(
                                    text = "Full Payment",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = formatCurrency(uiState.totalPrice),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = uiState.paymentType == PaymentType.DOWN,
                                    onClick = { viewModel.setPaymentType(PaymentType.DOWN) }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = uiState.paymentType == PaymentType.DOWN,
                                onClick = { viewModel.setPaymentType(PaymentType.DOWN) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFFBBB46)
                                )
                            )
                            Column {
                                Text(
                                    text = "Down Payment",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = formatCurrency(uiState.totalPrice / 2),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 42.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = formatCurrency(uiState.totalPrice),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFBBB46)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(180.dp)
                        .height(48.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(12.dp)
                        ),
                ) {
                    Text(
                        text = "Choose Payment",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
