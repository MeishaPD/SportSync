package brawijaya.example.sportsync.ui.screens.payment.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import brawijaya.example.sportsync.ui.viewmodels.PaymentViewModel
import brawijaya.example.sportsync.ui.viewmodels.PaymentType
import brawijaya.example.sportsync.data.models.BookingItem
import brawijaya.example.sportsync.data.models.CourtData
import brawijaya.example.sportsync.data.models.PaymentMethod
import brawijaya.example.sportsync.ui.navigation.Screen
import brawijaya.example.sportsync.utils.CurrencyUtils.formatCurrency

@Composable
fun PaymentContent(
    navController: NavController,
    totalAmount: Int = 100000,
    courtName: String = "",
    selectedTimeSlots: List<BookingItem> = emptyList(),
    selectedDate: String = "",
    paymentType: PaymentType = PaymentType.FULL,
    courtData: CourtData? = null,
    viewModel: PaymentViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Calculate the actual payment amount based on payment type
    val paymentAmount = if (paymentType == PaymentType.DOWN) totalAmount / 2 else totalAmount

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (paymentType == PaymentType.DOWN) "Down Payment" else "Total Payment",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = formatCurrency(paymentAmount),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        if (paymentType == PaymentType.DOWN) {
                            Text(
                                text = "Remaining: ${formatCurrency(totalAmount - paymentAmount)}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    TextButton(
                        onClick = { }
                    ) {
                        Text(
                            text = "View Details",
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Payment Method",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Digital Payment",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        tint = Color.Black
                    )
                }
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PaymentMethodItem(
                    method = PaymentMethod.QRIS,
                    isSelected = uiState.selectedPaymentMethod == PaymentMethod.QRIS,
                    onSelect = { viewModel.selectPaymentMethod(PaymentMethod.QRIS) }
                )

                PaymentMethodItem(
                    method = PaymentMethod.OVO,
                    isSelected = uiState.selectedPaymentMethod == PaymentMethod.OVO,
                    onSelect = { viewModel.selectPaymentMethod(PaymentMethod.OVO) }
                )

                PaymentMethodItem(
                    method = PaymentMethod.GOPAY,
                    isSelected = uiState.selectedPaymentMethod == PaymentMethod.GOPAY,
                    onSelect = { viewModel.selectPaymentMethod(PaymentMethod.GOPAY) }
                )

                PaymentMethodItem(
                    method = PaymentMethod.PAYPAL,
                    isSelected = uiState.selectedPaymentMethod == PaymentMethod.PAYPAL,
                    onSelect = { viewModel.selectPaymentMethod(PaymentMethod.PAYPAL) }
                )

                PaymentMethodItem(
                    method = PaymentMethod.VISA,
                    isSelected = uiState.selectedPaymentMethod == PaymentMethod.VISA,
                    onSelect = { viewModel.selectPaymentMethod(PaymentMethod.VISA) }
                )

                PaymentMethodItem(
                    method = PaymentMethod.APPLEPAY,
                    isSelected = uiState.selectedPaymentMethod == PaymentMethod.APPLEPAY,
                    onSelect = { viewModel.selectPaymentMethod(PaymentMethod.APPLEPAY) }
                )
            }
        }

        if (uiState.paymentError != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        text = uiState.paymentError!!,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // Log comprehensive booking details
                    Log.d("PaymentScreen", "=== COMPREHENSIVE BOOKING DETAILS ===")
                    Log.d("PaymentScreen", "--- COURT INFORMATION ---")
                    Log.d("PaymentScreen", "Court Name: $courtName")

                    courtData?.let { court ->
                        Log.d("PaymentScreen", "Court ID: ${court.id}")
                        Log.d("PaymentScreen", "Court Address: ${court.address}")
                        Log.d("PaymentScreen", "Price Per Hour: ${court.pricePerHour}")
                        Log.d("PaymentScreen", "Court Available: ${court.isAvailable}")
                        Log.d("PaymentScreen", "Available Time Slots: ${court.timeSlots.joinToString(", ") { it.time }}")
                    }

                    Log.d("PaymentScreen", "--- BOOKING INFORMATION ---")
                    Log.d("PaymentScreen", "Selected Date: $selectedDate")
                    Log.d("PaymentScreen", "Number of Time Slots: ${selectedTimeSlots.size}")

                    selectedTimeSlots.forEachIndexed { index, booking ->
                        Log.d("PaymentScreen", "Slot ${index + 1}:")
                        Log.d("PaymentScreen", "  - Time: ${booking.timeSlot}")
                        Log.d("PaymentScreen", "  - Date: ${booking.date}")
                        Log.d("PaymentScreen", "  - Price: ${formatCurrency(booking.price)}")
                    }

                    Log.d("PaymentScreen", "--- PAYMENT INFORMATION ---")
                    Log.d("PaymentScreen", "Payment Type: ${paymentType.name}")
                    Log.d("PaymentScreen", "Total Amount: ${formatCurrency(totalAmount)}")
                    Log.d("PaymentScreen", "Amount to Pay Now: ${formatCurrency(paymentAmount)}")
                    Log.d("PaymentScreen", "Selected Payment Method: ${uiState.selectedPaymentMethod.displayName}")

                    Log.d("PaymentScreen", "--- TRANSACTION INFORMATION ---")
                    Log.d("PaymentScreen", "Transaction Date: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}")

                    Log.d("PaymentScreen", "--- BOOKING SUMMARY ---")
                    Log.d("PaymentScreen", "Total Booking Duration: ${selectedTimeSlots.size} hours")
                    Log.d("PaymentScreen", "Total Cost: ${formatCurrency(totalAmount)}")
                    if (paymentType == PaymentType.DOWN) {
                        Log.d("PaymentScreen", "Remaining Balance: ${formatCurrency(totalAmount - paymentAmount)}")
                    }
                    Log.d("PaymentScreen", "=====================================")

                    // Create booking using the new ViewModel method
                    courtData?.let { court ->
                        viewModel.createBooking(
                            courtId = court.id,
                            courtName = courtName,
                            selectedDate = selectedDate,
                            totalAmount = totalAmount,
                            selectedTimeSlots = selectedTimeSlots,
                            paymentType = paymentType,
                            onSuccess = { bookingId ->
                                Log.d("PaymentScreen", "Booking created successfully with ID: $bookingId")

                                // Navigate to payment detail screen
                                navController.navigate(
                                    Screen.PaymentDetail.createRoute(
                                        bookingId = bookingId,
                                        totalAmount = paymentAmount
                                    )
                                )
                            },
                            onError = { error ->
                                Log.e("PaymentScreen", "Booking creation failed: $error")
                            }
                        )
                    } ?: run {
                        Log.e("PaymentScreen", "Court data is null, cannot create booking")
                    }
                },
                enabled = !uiState.isProcessingPayment && courtData != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFCCD78),
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isProcessingPayment) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Processing...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                } else {
                    Text(
                        text = "Pay ${formatCurrency(paymentAmount)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFFFBBB46) else Color.Black,
                shape = RoundedCornerShape(16.dp)
            )
            .selectable(
                selected = isSelected,
                onClick = onSelect
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFF8E1) else Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = method.iconRes),
                    contentDescription = "${method.displayName} icon",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = method.displayName,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFFFBBB46),
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}