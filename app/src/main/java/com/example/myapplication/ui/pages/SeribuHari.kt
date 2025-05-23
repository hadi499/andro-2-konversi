package com.example.myapplication.ui.pages

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.time.ZoneId
import java.time.Instant
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeribuHari() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var resultDate by remember { mutableStateOf("") }
    var refreshing by remember { mutableStateOf(false) }

    // Formatter untuk tanggal
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    // Fungsi kalkulasi
    fun calculateThousandDays() {
        resultDate = selectedDate.plusDays(999).format(dateFormatter)
    }

    // Handle refresh
    // Trigger refresh when drag detected
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(1000) // Simulasi loading
            selectedDate = LocalDate.now()
            resultDate = ""
            refreshing = false
        }
    }

    // DatePicker state
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    // UI Layout
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 20 && !refreshing) {
                            refreshing = true
                        }
                    }
                }
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Konten UI
            Text(
                text = "Seribu Hari Converter",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = { showDatePicker = true }) {
                Text("Pilih Tanggal")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tanggal dipilih: ${selectedDate.format(dateFormatter)}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Klik hitung untuk pencarian 1000 hari",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFF14A00)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { calculateThousandDays() }) {
                Text("Hitung")
            }

            Spacer(modifier = Modifier.height(16.dp))


            if (resultDate.isNotEmpty()) {
                Text(
                    text = resultDate,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                )
            }
        }

        // Loading indicator
        if (refreshing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp)
            )
        }
    }

    // DatePicker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        resultDate = ""
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}